package ru.maxmorev.restful.eshop.rest.exception;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.maxmorev.restful.eshop.rest.response.Message;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

    /**
     * Other errors
     * @param req
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Message handleBadRequest(HttpServletRequest req, Exception ex) {
        logger.error(ex.getLocalizedMessage(), ex);
        Message responseMessage = new Message(Message.ERROR, req.getRequestURL().toString(), "Internal server error", Collections.EMPTY_LIST);
        return responseMessage;
    }

    /**
     * Processing HibernateException
     * @param req
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = org.springframework.dao.DataAccessException.class)
    @ResponseBody
    public Message handleHibernateException(HttpServletRequest req, Exception ex) {
        Message responseMessage = new Message(Message.ERROR, req.getRequestURL().toString(), "Internal storage error", Collections.EMPTY_LIST);
        logger.error("HibernateException {}", ex);
        return responseMessage;
    }

    /**
     * Validation errors
     * @param req request method
     * @param ex MethodArgumentNotValidException
     * @return @see ru.maxmorev.restful.eshop.rest.response.Message
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Message validationException(HttpServletRequest req, MethodArgumentNotValidException ex) {
        logger.debug("Errors: {}", ex.getBindingResult().getAllErrors());
        List<Message.ErrorDetail> fieldsErrorDetails = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> new Message.ErrorDetail(e.getField(), e.getDefaultMessage()))
                .collect(Collectors.toList());
        Message responseMessage = new Message(Message.ERROR, req.getRequestURL().toString(), "Validation error", fieldsErrorDetails);
        return responseMessage;
    }

}
