package app.Api.Search;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

@Service
public class SearchService {

    @Autowired
    private SearchRepository repository;

    public Object create(SearchEntity entity) {
        return repository.save(entity);
    }

    public Object getAll() {
        return repository.findAll();
    }

    public Object getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Object update(Long id, SearchEntity newEntity) {
        return repository.findById(id).map(existing -> {
            newEntity.setId(id);
            return repository.save(newEntity);
        }).orElse(null);
    }

    public Object delete(Long id) {
        repository.deleteById(id);
        return "Deleted ID " + id;
    }
}
