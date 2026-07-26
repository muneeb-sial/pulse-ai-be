package app.Api.MedicalRecord;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecordEntity, Long> {
}
