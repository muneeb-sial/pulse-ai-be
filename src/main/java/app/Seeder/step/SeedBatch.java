package app.Seeder.step;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/** Chunked saveAll + flush/clear so bulk inserts batch and the persistence context stays small. */
@Component
public class SeedBatch {

    static final int CHUNK = 500;

    @PersistenceContext
    private EntityManager em;

    public <T> void saveAll(JpaRepository<T, Long> repo, List<T> items) {
        for (int from = 0; from < items.size(); from += CHUNK) {
            repo.saveAll(items.subList(from, Math.min(from + CHUNK, items.size())));
            em.flush();
            em.clear();
        }
    }
}
