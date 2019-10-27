package ru.maxmorev.restful.eshop.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerVerify {
    @NotNull(message = "{validation.customerVerify.id}")
    private Long id;
    @NotBlank(message = "{validation.customerVerify.verifyCode}")
    private String verifyCode;
    private Boolean verified;


}
