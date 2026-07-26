package app.Api.Medicine;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

@RestController
@RequestMapping("/medicine")
public class MedicineController {

    @Autowired
    private MedicineService service;

    @PostMapping
    public Object create(@RequestBody MedicineEntity entity) {
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
    public Object update(@PathVariable Long id, @RequestBody MedicineEntity entity) {
        return service.update(id, entity);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable Long id) {
        return service.delete(id);
    }
}
