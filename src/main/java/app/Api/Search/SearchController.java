package app.api.search;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService service;
    private final SearchIndexer indexer;

    @GetMapping
    public List<SearchService.Hit> search(
            @RequestParam String q,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            // Authentication auth
            ) {
        boolean isAdmin = true;
        // boolean isAdmin = auth
        //                     .getAuthorities()
        //                     .stream()
        //                     .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return service.search(q, type, isAdmin, page, Math.min(size, 100));
    }

    @GetMapping("/reindex")
    // @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Integer> reindex() {
        return indexer.reindexAll();
    }
}
