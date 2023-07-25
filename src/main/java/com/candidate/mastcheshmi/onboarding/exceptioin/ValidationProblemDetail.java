package com.candidate.mastcheshmi.onboarding.exceptioin;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.springframework.http.ProblemDetail;

@Data
public class ValidationProblemDetail extends ProblemDetail {

    /**
     * A map from filed name to validation error.
     */
    private Map<String, String> fieldValidationError = new HashMap<>();
}
