package app.Api.Patient;

import app.Api.Auth.AuthRepository;
import app.Api.Auth.UserEntity;
import app.Api.Patient.Dtos.PatientCreateDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PatientService {


    private final PatientRepository repository;
    private final AuthRepository userRepository;

    public Object create(Long userId, PatientCreateDto patientCreateDto) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        PatientEntity patient = PatientEntity.builder()
                .user(user)
                .name(patientCreateDto.getName())
                .build();

        return repository.save(patient);
    }

    public Object getAll() {
        return repository.findAll();
    }

    public Object getById(Long id) {
        return repository.findById(id).orElse(null);
    }

//    TO-DO
    public Object update(
            Long userId,
            Long id,
            PatientEntity newEntity
    ) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

//        Optional<PatientEntity> patientEntity = this.repository.findOneByUserId(userId);
//        if (patientEntity.isEmpty()){
//            throw new EntityNotFoundException("Patient not found");
//        }
        return user;

    }

    public Object delete(Long id) {
        repository.deleteById(id);
        return "Deleted ID " + id;
    }
}
