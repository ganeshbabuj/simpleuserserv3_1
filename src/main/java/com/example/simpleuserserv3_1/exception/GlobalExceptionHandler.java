package com.example.simpleuserserv3_1.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public void handleServiceException(HttpServletResponse res, ServiceException e) throws IOException {
        log.error(e.getMessage(), e);
        res.sendError(e.getHttpStatus().value(), e.getMessage());
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public void handleOptimisticLockingFailureException(HttpServletResponse res, OptimisticLockingFailureException e) throws IOException {
        log.error(e.getMessage(), e);
        res.sendError(HttpStatus.CONFLICT.value(), "Stale or Inconsistent data sent in Request");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public void handle(HttpServletResponse res, HttpMessageNotReadableException e) throws IOException {
        log.error(e.getMessage(), e);
        res.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public void handleNotFoundException(HttpServletResponse res, Exception e) throws IOException {
        log.error(e.getMessage(), e);
        res.sendError(HttpStatus.NOT_FOUND.value(), "Not Found");
    }

    @ExceptionHandler(Exception.class)
    public void handleException(HttpServletResponse res, Exception e) throws IOException {
        log.error(e.getMessage(), e);
        res.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error");
    }

}
