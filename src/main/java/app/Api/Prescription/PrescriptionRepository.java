package app.Api.Prescription;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<PrescriptionEntity, Long> {
}
