package app.Api.MedicalHistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

@Service
public class MedicalHistoryService {

    @Autowired
    private MedicalHistoryRepository repository;

    public Object create(MedicalHistoryEntity entity) {
        return repository.save(entity);
    }

    public Page<MedicalHistoryEntity> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Object getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Object update(Long id, MedicalHistoryEntity newEntity) {
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
