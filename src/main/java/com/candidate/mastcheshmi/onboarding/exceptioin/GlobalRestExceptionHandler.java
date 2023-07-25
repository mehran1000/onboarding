package com.candidate.mastcheshmi.onboarding.exceptioin;

import java.net.URI;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *  This is a class that handles exceptions for all REST controllers in the application.
 */
@RestControllerAdvice
@Slf4j
public class GlobalRestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = getValidationErrorMap(ex);
        log.debug("validation exception." + errors, ex);

        ValidationProblemDetail problemDetail = new ValidationProblemDetail();
        problemDetail.setFieldValidationError(errors);
        problemDetail.setType(URI.create("/problems/validation-error"));
        problemDetail.setTitle("Validation Error");
        problemDetail.setStatus(HttpStatus.BAD_REQUEST);
        return problemDetail;
    }

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleException(NotFoundException ex) {
        log.debug("The requested resource is not found.", ex);
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setType(URI.create("/problems/not-found"));
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setTitle("Not found error");
        return problemDetail;
    }

    @ExceptionHandler(CountryIsNotSupportedException.class)
    public ProblemDetail handleException(CountryIsNotSupportedException ex) {
        log.debug("The country is not supported.", ex);
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setType(URI.create("/problems/not-supported-country"));
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setTitle("Not supported country");
        return problemDetail;
    }

    @ExceptionHandler(InvalidCredentialException.class)
    public ProblemDetail handleException(InvalidCredentialException ex) {
        log.debug("Invalid credential.", ex);
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setType(URI.create("/problems/invalid-credential"));
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setTitle("Invalid credential");
        return problemDetail;
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ProblemDetail handleException(UserAlreadyExistException ex) {
        log.debug("Duplicate userName.", ex);
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setType(URI.create("/problems/duplicate-userName"));
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setTitle("Duplicate userName");
        return problemDetail;
    }

    /**
     * Handles unexpected exceptions that occur within the application and returns a ProblemDetail response to the
     * client. A tracking code is generated and logged to aid in troubleshooting.
     *
     * @param ex the unexpected Throwable that occurred
     * @return a ProblemDetail response representing the error with a tracking code for further investigation.
     */
    @ExceptionHandler(Throwable.class)
    public ProblemDetail handleUnexpectedException(Throwable ex) {
        String trackingCode = UUID.randomUUID().toString();
        log.error("Unexpected error : trackingCode " + trackingCode, ex);

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setType(URI.create("/problems/server"));
        // For security reasons, detail is not set.
        problemDetail.setTitle("Server Error");
        problemDetail.setProperty("trackingCode", trackingCode);
        return problemDetail;
    }

    private static Map<String, String> getValidationErrorMap(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors()
            .stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }
}
