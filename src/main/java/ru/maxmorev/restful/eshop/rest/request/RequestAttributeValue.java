package ru.maxmorev.restful.eshop.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Enums;
import lombok.Data;
import ru.maxmorev.restful.eshop.annotation.AttributeDataType;
import ru.maxmorev.restful.eshop.validation.CheckAttributeValueDuplicationForType;
import ru.maxmorev.restful.eshop.validation.CheckCommodityTypeId;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@CheckCommodityTypeId(message = "{validation.CommodityAttribute.typeId.valid}")
@CheckAttributeValueDuplicationForType(message = "{validation.commodity.attribute.value.duplication.type}")
public class RequestAttributeValue {

    @NotNull(message = "{validation.CommodityAttribute.typeId.NotNull.message}")
    private Long typeId;
    @NotBlank(message = "{validation.CommodityAttribute.name.NotBlank.message}")
    @Size(min=2, max=16, message = "{validation.CommodityAttribute.name.size.message}")
    private String name;
    @NotBlank(message = "{validation.CommodityAttribute.dataType.NotBlank.message}")
    private String dataType;
    private String measure;
    @NotBlank(message = "{validation.CommodityAttributeValue.value.NotBlank.message}")
    @Size(min=1, max=16, message = "{validation.CommodityAttributeValue.value.size.message}")
    private String value;

    @JsonIgnore
    @AssertTrue(message = "{validation.CommodityAttribute.dataType.valid}")
    public boolean isCommodityAttributeDataTypeValid(){
        return Enums.getIfPresent(AttributeDataType.class, dataType).isPresent();
    }

    public void setName(String name) {
        this.name = Objects.nonNull(name)? name.toLowerCase().trim():null;
    }

    public void setValue(String value) {

        this.value = Objects.nonNull(value)?value.toLowerCase().trim():null;
    }

    public void setMeasure(String measure) {
        this.measure = Objects.nonNull(measure)?measure.toLowerCase().trim():null;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

}

