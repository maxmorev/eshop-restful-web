package ru.maxmorev.restful.eshop.rest.response;

import lombok.Builder;
import lombok.Value;
import ru.maxmorev.restful.eshop.entities.CommodityBranchAttributeSet;

@Value
@Builder
public class AttrubuteDto {
    private String name;
    private String value;

    public static AttrubuteDto of(CommodityBranchAttributeSet a) {
        return AttrubuteDto.builder()
                .name(a.getAttribute().getName())
                .value(a.getAttributeValue().getValue().toString())
                .build();
    }
}
