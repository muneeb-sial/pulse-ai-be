package app.Api.Medicine;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRepository extends JpaRepository<MedicineEntity, Long> {
}
