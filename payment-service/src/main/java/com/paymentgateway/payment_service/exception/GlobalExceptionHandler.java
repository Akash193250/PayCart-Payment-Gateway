package com.paymentgateway.payment_service.exception;

import com.paymentgateway.payment_service.dto.ErrorResponse;
import com.stripe.exception.StripeException;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.HttpStatusCodeException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

        private final ObjectMapper objectMapper;

        public GlobalExceptionHandler(ObjectMapper objectMapper) {
                this.objectMapper = objectMapper;
        }

        @ExceptionHandler(StripeException.class)
        public ResponseEntity<ErrorResponse> handleStripeException(
                        StripeException exception,
                        HttpServletRequest request) {

                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                "Payment Error",
                                exception.getMessage(),
                                request.getRequestURI());

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(response);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgument(
                        IllegalArgumentException exception,
                        HttpServletRequest request) {

                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                "Bad Request",
                                exception.getMessage(),
                                request.getRequestURI());

                return ResponseEntity
                                .badRequest()
                                .body(response);
        }

        @ExceptionHandler(HttpStatusCodeException.class)
        public ResponseEntity<ErrorResponse> handleServiceException(
                        HttpStatusCodeException exception,
                        HttpServletRequest request) {

                HttpStatus status = HttpStatus.resolve(
                                exception.getStatusCode().value());

                if (status == null) {
                        status = HttpStatus.INTERNAL_SERVER_ERROR;
                }

                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                status.value(),
                                status.getReasonPhrase(),
                                extractMessage(exception),
                                request.getRequestURI());

                return ResponseEntity
                                .status(status)
                                .body(response);
        }

        private String extractMessage(
                        HttpStatusCodeException exception) {

                try {

                        JsonNode json = objectMapper.readTree(
                                        exception.getResponseBodyAsString());

                        if (json.has("message")) {
                                return json.get("message").asText();
                        }

                } catch (Exception ignored) {
                }

                return "Request to another service failed";
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGeneralException(
                        Exception exception,
                        HttpServletRequest request) {

                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Internal Server Error",
                                "Something went wrong while processing the request",
                                request.getRequestURI());

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(response);
        }
}