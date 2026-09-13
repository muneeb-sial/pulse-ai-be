package app.Api.Patient;

import app.Api.Auth.AuthRepository;
import app.Api.Auth.UserEntity;
import app.Api.Patient.Dtos.PatientCreateDto;
import app.Api.Patient.Dtos.PatientUpdateDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PatientService {


    private final PatientRepository repository;
    private final AuthRepository userRepository;
    private final PatientMapper patientMapper;

    public Object create(Long userId, PatientCreateDto patientCreateDto) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        PatientEntity patient = PatientEntity.builder().user(user).name(patientCreateDto.getName()).build();
        return repository.save(patient);
    }

    public Page<PatientEntity> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Object getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public Object update(Long userId, Long id, PatientUpdateDto patientUpdateDto) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        Optional<PatientEntity> patientEntity = this.repository.findById(id);
        if (patientEntity.isEmpty() || !patientEntity.get().getUser().getId().equals(userId)) {
            throw new EntityNotFoundException("Patient not found");
        }

        this.patientMapper.updateEntity(patientUpdateDto, patientEntity.get());
        return patientEntity.get();
    }

    public Object delete(Long id) {
        repository.deleteById(id);
        return "Deleted ID " + id;
    }
}
