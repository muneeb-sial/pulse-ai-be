package app.api.notification;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;

    public Page<NotificationEntity> getAllForUser(Long userId, Pageable pageable) {
        return repository.findAllByUser_Id(userId, pageable);
    }

    public long getUnreadCount(Long userId) {
        return repository.countByUser_IdAndReadFalse(userId);
    }

    public Object markAsRead(Long userId, Long id) {
        var notification = repository.findByIdAndUser_Id(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));
        notification.setRead(true);
        repository.save(notification);
        return Map.of("message", "notification marked as read");
    }
}
