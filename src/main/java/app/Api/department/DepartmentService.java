package app.api.department;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository repository;

    public Object create(DepartmentEntity entity) {
        return repository.save(entity);
    }

    public Page<DepartmentEntity> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Object getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id " + id));
    }

    public Object update(Long id, DepartmentEntity newEntity) {
        return repository.findById(id).map(existing -> {
            newEntity.setId(id);
            return repository.save(newEntity);
        }).orElseThrow(() -> new EntityNotFoundException("Department not found with id " + id));
    }

    public Object delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Department not found with id " + id);
        }
        repository.deleteById(id);
        return "Deleted ID " + id;
    }
}
