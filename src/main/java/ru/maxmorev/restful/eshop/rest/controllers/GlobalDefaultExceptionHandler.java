package ru.maxmorev.restful.eshop.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.maxmorev.restful.eshop.rest.response.Message;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice

public class GlobalDefaultExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

    public static final String DEFAULT_ERROR_VIEW = "error";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Message handleBadRequest(HttpServletRequest req, Exception ex) {
        //ex.printStackTrace();
        logger.error(ex.getLocalizedMessage(), ex);
        return new Message(Message.ERROR, req.getRequestURL().toString(), ex);
    }

}
