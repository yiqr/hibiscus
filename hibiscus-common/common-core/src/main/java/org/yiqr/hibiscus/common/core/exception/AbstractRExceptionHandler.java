package org.yiqr.hibiscus.common.core.exception;

import com.google.common.base.Throwables;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;
import org.yiqr.hibiscus.common.core.component.LocaleMessage;
import org.yiqr.hibiscus.common.core.jaxrs.R;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.Set;

/**
 * @author rose
 * @create 2023/7/14 18:33
 */
@Slf4j
@RestControllerAdvice
public class AbstractRExceptionHandler extends ResponseEntityExceptionHandler {

    private final LocaleMessage localeMessage;

    protected AbstractRExceptionHandler(LocaleMessage localeMessage) {
        this.localeMessage = localeMessage;
    }

    /**
     * 权限不足
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(final AccessDeniedException ex) {
        this.errorLog(ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(R.of(ErrorCode.FAIL, ex.getMessage()));
    }

    /**
     * 自定义API响应异常
     *
     * @param ex 自定义异常
     * @return ResponseEntity
     */
    @ResponseBody
    @ExceptionHandler({RException.class})
    public ResponseEntity<Object> handleRException(final RException ex) {
        this.errorLog(ex);
        String errorMsg = ex.getMessage();
        String errorCode = StringUtils.substringBetween(ex.getMessage(), "{#", "#}");
        if (StringUtils.isNotBlank(errorCode)) {
            errorMsg = localeMessage.getMessage(errorCode);
        }
        return ResponseEntity.ok().body(R.of(ex.getCode(), errorMsg));
    }

    @ResponseBody
    @ExceptionHandler({UnsatisfiedServletRequestParameterException.class})
    public ResponseEntity<Object> handleRequestParameterException(final UnsatisfiedServletRequestParameterException ex) {
        this.errorLog(ex);
        String errorMsg = String.format("%s Parameters error", Arrays.toString(ex.getParamConditions()));
        return ResponseEntity.ok().body(R.of(ErrorCode.API_ARGUMENT_INVALID, errorMsg));
    }

    /**
     * Controller 参数校验不通过
     *
     * @param ex      异常
     * @param headers Header
     * @param request 请求
     * @return ResponseEntity
     */
    @ResponseBody
    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.warn(ex.getMessage());
        return ResponseEntity.ok().body(onMethodArgumentNotValidException(ex));
    }

    /**
     * 约束不通过
     *
     * @param ex 异常
     * @return ResponseEntity
     */
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        log.warn(ex.getMessage());
        return ResponseEntity.ok().body(onConstraintViolationException(ex));
    }

    /**
     * 事务异常
     *
     * @param ex      异常
     * @param request 请求
     * @return ResponseEntity
     */
    @ResponseBody
    @ExceptionHandler({TransactionSystemException.class})
    public ResponseEntity<Object> handleTransactionSystemException(TransactionSystemException ex, WebRequest request) {
        Throwable cause = Throwables.getRootCause(ex);
        this.errorLog(ex);
        if (cause instanceof ConstraintViolationException exception) {
            return ResponseEntity.ok(onConstraintViolationException(exception));
        }
        return handleExceptionInternal(ex, request);
    }

    /**
     * 并发冲突异常
     *
     * @param ex      异常
     * @param request Web request
     * @return ResponseEntity
     */
    @ResponseBody
    @ExceptionHandler({InvalidDataAccessApiUsageException.class, DataAccessException.class, DataIntegrityViolationException.class})
    protected ResponseEntity<Object> handleConflict(final RuntimeException ex, WebRequest request) {
        this.errorLog(ex);
        return handleExceptionInternal(ex, request);
    }

    /**
     * 运行异常
     *
     * @param ex      异常
     * @param request Web request
     * @return ResponseEntity
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleExceptionInternal(Exception ex, WebRequest request) {
        this.errorLog(ex);
        return handleExceptionInternal(ex,HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * 内部异常处理
     *
     * @param ex 异常
     * @return ResponseEntity
     */
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, HttpStatus status, WebRequest request) {
        this.errorLog(ex);
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, RequestAttributes.SCOPE_REQUEST);
        }
        int errorCode = status.value();
        return ResponseEntity.internalServerError().body(R.of(errorCode, ex.getMessage()));
    }

    private R onConstraintViolationException(ConstraintViolationException ex) {
        StringBuilder errorMessage = new StringBuilder();
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            errorMessage.append(constraintViolation.getMessage());
        }
        return R.of(ErrorCode.API_ARGUMENT_INVALID, errorMessage.toString());
    }

    private R onMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            if (error instanceof FieldError fieldError) {
                errorMessage.append(fieldError.getField());
            }
            errorMessage.append(error.getDefaultMessage()).append(StringUtils.SPACE);
        }
        return R.of(ErrorCode.API_VIOLATION_ERROR, errorMessage.toString());
    }

    private void errorLog(Exception exStack) {
        log.error(Throwables.getStackTraceAsString(exStack));
    }
}
