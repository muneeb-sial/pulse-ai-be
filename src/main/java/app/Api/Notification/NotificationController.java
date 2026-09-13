package app.Api.Notification;

import app.Configs.Security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @GetMapping
    public Object getAll(@AuthenticationPrincipal UserPrincipal user, Pageable pageable) {
        return service.getAllForUser(user.getUserId(), pageable);
    }

    @GetMapping("/unread-count")
    public Object getUnreadCount(@AuthenticationPrincipal UserPrincipal user) {
        return Map.of("count", service.getUnreadCount(user.getUserId()));
    }

    @PatchMapping("/{id}/read")
    public Object markAsRead(@AuthenticationPrincipal UserPrincipal user, @PathVariable Long id) {
        return service.markAsRead(user.getUserId(), id);
    }
}
