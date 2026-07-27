package app.Api.Patient;

import app.Api.Patient.Dtos.PatientCreateDto;
import app.Configs.Security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService service;

    @PostMapping
    public Object create(
            @RequestBody PatientCreateDto entity,
            @AuthenticationPrincipal UserPrincipal user
            ) {
        return service.create(user.getUserId(),entity);
    }

    @GetMapping
    public Object getAll() {
        return service.getAll();
    }

    @GetMapping("/s/{id}")
    public Object getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public Object update(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable Long id,
            @RequestBody PatientEntity entity
    ) {
        return service.update(id, entity);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable Long id) {
        return service.delete(id);
    }
}
