package app.api.medicalrecord;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository repository;

    public Object create(MedicalRecordEntity entity) {
        return repository.save(entity);
    }

    public Page<MedicalRecordEntity> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Object getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medical record not found with id " + id));
    }

    public Object update(Long id, MedicalRecordEntity newEntity) {
        return repository.findById(id).map(existing -> {
            newEntity.setId(id);
            return repository.save(newEntity);
        }).orElseThrow(() -> new EntityNotFoundException("Medical record not found with id " + id));
    }

    public Object delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Medical record not found with id " + id);
        }
        repository.deleteById(id);
        return "Deleted ID " + id;
    }
}
