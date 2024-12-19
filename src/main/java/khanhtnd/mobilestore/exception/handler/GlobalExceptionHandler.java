package khanhtnd.mobilestore.exception.handler;

import khanhtnd.mobilestore.dto.response.Response;
import khanhtnd.mobilestore.exception.CustomException;
import khanhtnd.mobilestore.exception.common.InvalidImageException;
import khanhtnd.mobilestore.exception.common.NotFoundException;
import khanhtnd.mobilestore.utils.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidImageException.class)
    public ResponseEntity<Response<Void>> handleInvalidImageException(InvalidImageException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.<Void>builder()
                        .code(ex.getDescription().getCode())
                        .description(ex.getDescription().getDescription())
                        .build());
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response<Integer>> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Response.<Integer>builder()
                        .code(ex.getDescription().getCode())
                        .description(ex.getDescription().getDescription())
                        .data(ex.getId())
                        .build());
    }
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Response<Void>> handleCustomException(CustomException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.<Void>builder()
                        .code(ex.getDescription().getCode())
                        .description(ex.getDescription().getDescription())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Exception>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.<Exception>builder()
                        .code(Message.MSG_400.getCode())
                        .description(Message.MSG_400.getDescription())
                        .data(ex)
                        .build());
    }
}
