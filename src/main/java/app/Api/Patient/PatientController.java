package app.Api.Patient;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService service;

    @PostMapping
    public Object create(@RequestBody PatientEntity entity) {
        return service.create(entity);
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
    public Object update(@PathVariable Long id, @RequestBody PatientEntity entity) {
        return service.update(id, entity);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable Long id) {
        return service.delete(id);
    }
}
