package org.zerock.groom_tone.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<ErrorResponse> IllegalAccessExceptionHender(
            IllegalAccessException e,
            HttpServletRequest request
    ) {

        log.error("Unexpected RuntimeException occurred: ", e);

        ErrorResponse errorResponse = ErrorResponse.of(
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> IllegalArgumentExceptionHandler(
            IllegalArgumentException e,
            HttpServletRequest request) {

        log.error("Unexpected RuntimeException occurred: ", e);

        ErrorResponse errorResponse = ErrorResponse.of(
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    /**
     * 모든 예외를 처리하는 핸들러
     *
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception e,
            HttpServletRequest request) {

        log.error("Unexpected Exception occurred: ", e);
        ErrorResponse errorResponse = ErrorResponse.of(
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}
