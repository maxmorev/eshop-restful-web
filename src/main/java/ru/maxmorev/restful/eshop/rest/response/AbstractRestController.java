package ru.maxmorev.restful.eshop.rest.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

public class AbstractRestController {

    private final Logger logger = LoggerFactory.getLogger(AbstractRestController.class);

    public void processBindingResult(BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            logger.info("!!!! bindingResult.hasErrors() !!!!");
            String errorContent = "";
            int index = 0;
            for(ObjectError error: bindingResult.getAllErrors()){
                errorContent += ++index+". " + error.getDefaultMessage()+"\n";
                logger.info(error.getDefaultMessage());
            }
            throw new RuntimeException( errorContent );
        }
    }
}
