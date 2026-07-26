package app.Api.SHealth;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

@RestController
@RequestMapping("/")
public class SHealthController {

    @GetMapping
    public Object getAll() {
        return Map.of(
                "status","healthy"
        );
    }

}
