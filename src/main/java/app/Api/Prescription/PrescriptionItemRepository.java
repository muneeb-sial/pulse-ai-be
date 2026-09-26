package app.Api.Prescription;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionItemRepository extends JpaRepository<PrescriptionItemEntity, Long> {
}
