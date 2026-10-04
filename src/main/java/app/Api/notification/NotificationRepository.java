package app.api.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    Page<NotificationEntity> findAllByUser_Id(Long userId, Pageable pageable);

    Optional<NotificationEntity> findByIdAndUser_Id(Long id, Long userId);

    long countByUser_IdAndReadFalse(Long userId);
}
