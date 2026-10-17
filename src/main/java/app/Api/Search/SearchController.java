package app.api.search;

import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
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

    /** Type-ahead for the search box. Under {@value SearchService#MIN_SUGGEST_LENGTH} chars it answers [] without touching ES. */
    @GetMapping("/suggest")
    public ResponseEntity<List<SearchService.Hit>> suggest(
            @RequestParam(defaultValue = "") String q,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "8") int size
            ) {
        boolean isAdmin = true; // same role gap as search()
        List<SearchService.Hit> hits = service.suggest(q, type, isAdmin, Math.max(1, Math.min(size, 10)));
        // keystrokes repeat prefixes (typing, backspacing), so let the browser reuse recent answers
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(Duration.ofSeconds(30)).cachePrivate())
                .body(hits);
    }

    @GetMapping("/reindex")
    // @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Integer> reindex() {
        return indexer.reindexAll();
    }
}
