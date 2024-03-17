package br.com.bortolattolucas.salesapi.resources.exception;

import br.com.bortolattolucas.salesapi.services.exceptions.DataIntegrityException;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> handleValidationError(MethodArgumentNotValidException e, HttpServletRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError) err).getField();
            String errorMessage = err.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Error.builder()
                        .message("Campo(s) inválido(s) semanticamente")
                        .fields(fieldErrors)
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Error> handleValidationError(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        fieldErrors.put(e.getPropertyName(), e.getMessage());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(Error.builder()
                        .message("Campo inválido sintaticamente")
                        .fields(fieldErrors)
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Error> handleObjectNotFound(ObjectNotFoundException e, HttpServletRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        fieldErrors.put("id", e.getIdentifier().toString());

        Error error = Error.builder()
                .message("Objeto não encontrado")
                .fields(fieldErrors)
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DataIntegrityException.class)
    public ResponseEntity<Error> handleStateConflict(DataIntegrityException e, HttpServletRequest request) {
        Error error = Error.builder()
                .message(e.getMessage())
                .fields(e.getFieldErrors())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
