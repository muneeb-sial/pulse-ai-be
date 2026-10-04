package app.api.patient;

import app.shared.enums.Role;
import app.api.auth.AuthRepository;
import app.api.auth.UserEntity;
import app.api.patient.dtos.PatientCreateDto;
import app.api.patient.dtos.PatientUpdateDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository repository;
    private final AuthRepository userRepository;
    private final PatientMapper patientMapper;

    @Transactional
    public Object create(Long callerUserId, PatientCreateDto dto) {
        PatientEntity patient = PatientEntity.builder()
                .user(resolveUser(dto))
                .name(dto.getName())
                .phone(dto.getPhone())
                .idCard(dto.getIdCard())
                .build();
        return repository.save(patient);
    }

    public Page<PatientEntity> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Object getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id " + id));
    }

    @Transactional
    public Object update(Long callerUserId, Long id, PatientUpdateDto dto) {
        PatientEntity patient = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id " + id));

        // NOTE: this used to reject any patient not owned by the caller, which made the
        // endpoint unusable from a staff/admin console. Access control is deliberately
        // deferred - reinstate it as a role check (ADMIN/STAFF, or self) once roles are
        // actually provisioned. See the "Known gap" note in CLAUDE.md.
        patientMapper.updateEntity(dto, patient);
        return patient;
    }

    public Object delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Patient not found with id " + id);
        }
        repository.deleteById(id);
        return "Deleted ID " + id;
    }

    /**
     * Every patient needs its own user row: `patients.user_id` is NOT NULL and UNIQUE, so
     * linking new patients to the caller would let an account create exactly one patient.
     *
     * An explicit email reuses or creates that account; otherwise a placeholder is derived
     * from the phone number. These provisioned accounts have no password and cannot log in
     * until one is set - they exist to satisfy the relationship.
     */
    private UserEntity resolveUser(PatientCreateDto dto) {
        String email = dto.getEmail() != null && !dto.getEmail().isBlank()
                ? dto.getEmail().trim().toLowerCase()
                : "patient." + dto.getPhone().replaceAll("[^0-9]", "") + "@local";

        Optional<UserEntity> existing = userRepository.findOneByEmail(email);
        if (existing.isPresent()) {
            UserEntity user = existing.get();
            if (repository.findOneByUserId(user.getId()).isPresent()) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "A patient already exists for " + email
                );
            }
            return user;
        }

        return userRepository.save(UserEntity.builder()
                .email(email)
                .isVerified(false)
                .provider("LOCAL")
                .role(Role.PATIENT.name())
                .build());
    }
}
