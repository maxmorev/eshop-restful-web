package ru.maxmorev.restful.eshop.rest.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Value;
import ru.maxmorev.restful.eshop.entities.CommodityImage;
import ru.maxmorev.restful.eshop.entities.Purchase;

import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
public class PurchaseDto {
    private Integer amount;//amount of items
    private Long branchId;//branchId
    private Long commodityId;
    private String name;
    private String shortDescription;
    private String overview;
    private Date dateOfCreation;
    private String type;
    private List<String> images;
    private Float price; //price for 1 item in branch
    private Currency currency; //current price currency
    private List<AttributeDto> attributes;

    public static PurchaseDto of(Purchase p) {
        return PurchaseDto.builder()
                .amount(p.getAmount())
                .branchId(p.getBranch().getId())
                .commodityId(p.getBranch().getCommodity().getId())
                .name(p.getBranch().getCommodity().getName())
                .shortDescription(p.getBranch().getCommodity().getShortDescription())
                .overview(p.getBranch().getCommodity().getOverview())
                .dateOfCreation(p.getBranch().getCommodity().getDateOfCreation())
                .type(p.getBranch().getCommodity().getType().getName())
                .images(p.getBranch()
                        .getCommodity()
                        .getImages()
                        .stream()
                        .sorted()
                        .map(CommodityImage::getUri)
                        .collect(Collectors.toList()))
                .price(p.getBranch().getPrice())
                .currency(p.getBranch().getCurrency())
                .attributes(p.getBranch()
                        .getAttributeSet()
                        .stream()
                        .map(AttributeDto::of)
                        .collect(Collectors.toList()))
                .build();
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
