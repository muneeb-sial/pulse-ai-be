package app.Api.SHealth;

import app.Producer.UserEvents.UserCreatedProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class SHealthController {

    private final UserCreatedProducer userCreatedProducer;

    @GetMapping
    public Object getAll() {
        double randomValue = Math.random();
        String str = String.valueOf(randomValue);
        userCreatedProducer.publish(str, Map.of("value", str));
        return Map.of(
                "status", "healthy"
        );
    }

}
