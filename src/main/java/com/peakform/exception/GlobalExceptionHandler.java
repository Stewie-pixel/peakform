package com.peakform.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(ResourceNotFoundException ex, HttpServletRequest request, Model model) {
        log.warn("Resource not found at {}: {}", request.getRequestURI(), ex.getMessage());
        model.addAttribute("message", "The item you requested could not be found.");
        return "error/generic";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleAny(Exception ex, HttpServletRequest request, Model model) {
        log.error("Unhandled exception at {}", request.getRequestURI(), ex);
        model.addAttribute("message", "Something went wrong on our end. Please try again shortly.");
        return "error/generic";
    }
}
