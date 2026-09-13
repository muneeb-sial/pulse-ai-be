package app.Api.Appointment;

import app.Shared.Enum.AppointmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService service;

    @PostMapping
    public Object create(@RequestBody AppointmentEntity entity) {
        return service.create(entity);
    }

    @GetMapping
    public Object getAll(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long doctorId,
            Pageable pageable
    ) {
        if (patientId != null) {
            return service.findByPatientId(patientId, pageable);
        }
        if (doctorId != null) {
            return service.findByDoctorId(doctorId, pageable);
        }
        return service.getAll(pageable);
    }

    @GetMapping("/s/{id}")
    public Object getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable Long id, @RequestBody AppointmentEntity entity) {
        return service.update(id, entity);
    }

    @PatchMapping("/{id}/status")
    public Object updateStatus(@PathVariable Long id, @RequestBody AppointmentStatus status) {
        return service.updateStatus(id, status);
    }

    @PatchMapping("/{id}/cancel")
    public Object cancel(@PathVariable Long id) {
        return service.cancelAppointmentId(id);
    }

    @PatchMapping("/{id}/reschedule")
    public Object reschedule(
            @PathVariable Long id,
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime
    ) {
        return service.rescheduleAppointmentId(id, startTime, endTime);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable Long id) {
        return service.delete(id);
    }
}
