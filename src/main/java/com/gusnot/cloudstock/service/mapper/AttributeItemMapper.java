package com.gusnot.cloudstock.service.mapper;

import com.gusnot.cloudstock.domain.*;
import com.gusnot.cloudstock.service.dto.AttributeItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity AttributeItem and its DTO AttributeItemDTO.
 */
@Mapper(componentModel = "spring", uses = {AttributeMapper.class, })
public interface AttributeItemMapper extends EntityMapper <AttributeItemDTO, AttributeItem> {

    @Mapping(source = "attribute.id", target = "attributeId")
    @Mapping(source = "attribute.name", target = "attributeName")
    AttributeItemDTO toDto(AttributeItem attributeItem); 

    @Mapping(source = "attributeId", target = "attribute")
    AttributeItem toEntity(AttributeItemDTO attributeItemDTO); 
    default AttributeItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        AttributeItem attributeItem = new AttributeItem();
        attributeItem.setId(id);
        return attributeItem;
    }
}
