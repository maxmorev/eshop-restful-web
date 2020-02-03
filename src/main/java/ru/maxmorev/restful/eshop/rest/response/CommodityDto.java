package ru.maxmorev.restful.eshop.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import ru.maxmorev.restful.eshop.entities.Commodity;
import ru.maxmorev.restful.eshop.entities.CommodityImage;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommodityDto extends CommodityInfoDto {


    private List<CommodityBranchDto> branches;

    @Builder
    public CommodityDto(Long id, String name, String shortDescription, String overview, Date dateOfCreation, CommodityTypeDto type, List<String> images, List<CommodityBranchDto> branches) {
        super(id, name, shortDescription, overview, dateOfCreation, type, images);
        this.branches = branches;
    }

    public static CommodityDto of(Commodity c, List<CommodityBranchDto> branches) {
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
                .branches(branches)
                .build();
    }


    @JsonIgnore
    public String getLastImageUri() {
        int size = getImages().size();
        if (size > 0) {
            return getImages().get(size - 1);
        } else {
            return "";
        }
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
