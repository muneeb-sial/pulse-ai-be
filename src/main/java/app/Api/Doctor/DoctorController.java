package app.Api.Doctor;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService service;

    @PostMapping
    public Object create(@RequestBody DoctorEntity entity) {
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
    public Object update(@PathVariable Long id, @RequestBody DoctorEntity entity) {
        return service.update(id, entity);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable Long id) {
        return service.delete(id);
    }
}
