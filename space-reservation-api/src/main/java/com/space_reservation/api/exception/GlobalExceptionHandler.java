package com.space_reservation.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        ErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .status(404)
                                .error("NOT_FOUND")
                                .message(ex.getMessage())
                                .path(request.getRequestURI())
                                .build()
                );
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(
            BusinessException ex,
            HttpServletRequest request
    ) {

        return ResponseEntity.badRequest()
                .body(
                        ErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .status(400)
                                .error("BUSINESS_RULE")
                                .message(ex.getMessage())
                                .path(request.getRequestURI())
                                .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidation(
            MethodArgumentNotValidException ex
    ) {

        Map<String,String> errors = new HashMap<>();

        for(FieldError error : ex.getBindingResult().getFieldErrors()){

            errors.put(
                    error.getField(),
                    error.getDefaultMessage()
            );
        }

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraint(
            ConstraintViolationException ex
    ){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(
            Exception ex,
            HttpServletRequest request
    ) {

        return ResponseEntity.internalServerError()
                .body(
                        ErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .status(500)
                                .error("INTERNAL_SERVER_ERROR")
                                .message(ex.getMessage())
                                .path(request.getRequestURI())
                                .build()
                );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisabled(
            DisabledException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN) // 403 Forbidden
                .body(
                        ErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.FORBIDDEN.value())
                                .error("FORBIDDEN")
                                .message(ex.getMessage())
                                .path(request.getRequestURI())
                                .build()
                );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED) // 401 Unauthorized
                .body(
                        ErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .error("UNAUTHORIZED")
                                .message(ex.getMessage())
                                .path(request.getRequestURI())
                                .build()
                );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJsonFormat(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        String cleanMessage = "El formato del JSON enviado no es válido o contiene tipos de datos incorrectos.";
        Throwable cause = ex.getCause();

        if (cause != null) {
            String fieldName = null;
            try {
                // 1. Intentar extraer el campo usando getPath() por reflexión (Aplica para la mayoría de errores de Jackson)
                java.lang.reflect.Method getPathMethod = cause.getClass().getMethod("getPath");
                java.util.List<?> pathList = (java.util.List<?>) getPathMethod.invoke(cause);

                if (pathList != null && !pathList.isEmpty()) {
                    Object reference = pathList.get(pathList.size() - 1);
                    java.lang.reflect.Method getFieldNameMethod = reference.getClass().getMethod("getFieldName");
                    fieldName = (String) getFieldNameMethod.invoke(reference);
                }
            } catch (Exception e) {
                // Si la reflexión falla, avanzamos al plan B
            }

            // 2. Plan B: Si la reflexión no encontró el campo, lo escaneamos directamente desde el texto del error
            if (fieldName == null || fieldName.isEmpty()) {
                String msg = cause.getMessage();
                if (msg != null && msg.contains("through reference chain:")) {
                    int lastQuoteIndex = msg.lastIndexOf("[\"");
                    if (lastQuoteIndex != -1) {
                        try {
                            String sub = msg.substring(lastQuoteIndex + 2);
                            fieldName = sub.split("\"\\]")[0];
                        } catch (Exception e) {
                            // Ignorar error de parseo de texto
                        }
                    }
                }
            }

            // 3. Si logramos identificar el campo, armamos el mensaje súper específico
            if (fieldName != null && !fieldName.isEmpty()) {
                // Personalización especial si es una fecha o una hora
                if (fieldName.equals("fecha")) {
                    cleanMessage = "Error de formato en el campo 'fecha': Debe cumplir con el formato 'AAAA-MM-DD' (ej. 2026-06-29).";
                } else if (fieldName.equals("horaInicio") || fieldName.equals("horaFin")) {
                    cleanMessage = String.format("Error de formato en el campo '%s': Debe cumplir con el formato de 24 horas 'HH:mm:ss' (ej. 13:00:00).", fieldName);
                } else {
                    cleanMessage = String.format("Error de formato en el campo '%s': Se esperaba un tipo de dato diferente (ej. un número en lugar de texto).", fieldName);
                }
            }
        }

        return ResponseEntity.badRequest()
                .body(
                        ErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .status(400)
                                .error("INVALID_JSON_FORMAT")
                                .message(cleanMessage)
                                .path(request.getRequestURI())
                                .build()
                );
    }

}