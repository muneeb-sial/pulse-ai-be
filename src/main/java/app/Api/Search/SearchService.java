package app.Api.Search;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SearchService {

    /** Searchable index aliases. Public = any authenticated caller, the rest need ADMIN (see CLAUDE.md role gap). */
    static final Set<String> OPEN_TYPES = Set.of("doctors", "departments", "medicines");
    static final Set<String> RESTRICTED_TYPES = Set.of(
            "patients", "appointments", "medical_histories", "medical_records", "prescriptions");

    private static final String[] TEXT_FIELDS = {
            "name", "name._2gram", "name._3gram", "genericName", "manufacturer", "specialization",
            "qualification", "biography", "departmentName", "patientName", "doctorName",
            "title", "diagnosis", "medicineNames", "city"
    };

    private final ElasticsearchOperations es;

    public record Hit(String type, Long id, String title, float score) {}

    public List<Hit> search(String q, String type, boolean isAdmin, int page, int size) {
        List<String> types = resolveTypes(type, isAdmin);

        NativeQuery query = NativeQuery.builder()
                .withQuery(b -> b.multiMatch(m -> m
                        .query(q)
                        .fields(List.of(TEXT_FIELDS))
                        .fuzziness("AUTO")))
                .withPageable(PageRequest.of(page, size))
                .build();

        return es.search(query, Map.class, IndexCoordinates.of(types.toArray(String[]::new)))
                .getSearchHits().stream()
                .map(this::toHit)
                .toList();
    }

    private List<String> resolveTypes(String type, boolean isAdmin) {
        if (type == null || type.isBlank()) {
            return isAdmin
                    ? Stream.concat(OPEN_TYPES.stream(), RESTRICTED_TYPES.stream()).toList()
                    : List.copyOf(OPEN_TYPES);
        }
        if (OPEN_TYPES.contains(type) || (isAdmin && RESTRICTED_TYPES.contains(type))) {
            return List.of(type);
        }
        if (RESTRICTED_TYPES.contains(type)) {
            throw new org.springframework.security.access.AccessDeniedException("Not allowed to search " + type);
        }
        throw new IllegalArgumentException("Unknown search type: " + type);
    }

    @SuppressWarnings("unchecked")
    private Hit toHit(SearchHit<Map> hit) {
        Map<String, Object> src = hit.getContent();
        String index = hit.getIndex() == null ? "" : hit.getIndex();
        String type = index.contains("_v") ? index.substring(0, index.lastIndexOf("_v")) : index;
        Object title = src.getOrDefault("name", src.getOrDefault("title", src.get("diagnosis")));
        Object id = src.get("id");
        return new Hit(type, id == null ? null : Long.valueOf(id.toString()),
                title == null ? null : title.toString(), hit.getScore());
    }
}
