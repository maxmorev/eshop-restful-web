package ru.maxmorev.restful.eshop.rest.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.maxmorev.restful.eshop.entities.CommodityImage;
import ru.maxmorev.restful.eshop.entities.CommodityInfo;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class CommodityInfoDto {
    private Long id;
    private String name;
    private String shortDescription;
    private String overview;
    private Date dateOfCreation;
    private CommodityTypeDto type;
    private List<String> images;

    public static CommodityInfoDto of(CommodityInfo c){
            return CommodityDto.builder()
                    .id(c.getId())
                    .name(jsonStr(c.getName()))
                    .shortDescription(jsonStr(c.getShortDescription()))
                    .overview(jsonStr(c.getOverview()))
                    .dateOfCreation(c.getDateOfCreation())
                    .type(CommodityTypeDto.of(c.getType()))
                    .images(c.getImages()
                            .stream()
                            .sorted()
                            .map(CommodityImage::getUri)
                            .collect(Collectors.toList()))
                    .build();
    }

    protected static String jsonStr(String s){
        return s.replace("'", "");
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
