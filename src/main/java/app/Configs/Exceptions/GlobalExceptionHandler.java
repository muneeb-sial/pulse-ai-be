package app.Configs.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public Object handleResponseExceptions(ResponseStatusException ex) {
        return ResponseEntity
                .status(ex.getStatusCode().value())
                .body(Map.of(
                        "status", ex.getStatusCode().value(),
                        "message", ex.getReason() != null ? ex.getReason() : "Internal Server Error"
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationError(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "status", HttpStatus.BAD_REQUEST.value(),
                        "message", "Validation failed",
                        "errors", errors
                ));
    }

    @ExceptionHandler(Exception.class)
    public Object handAllException(Exception ex) {
        System.out.println("============Global Exception===========");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "message", ex.getMessage()
                ));
    }
}
