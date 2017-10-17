package com.gusnot.cloudstock.service.mapper;

import com.gusnot.cloudstock.domain.*;
import com.gusnot.cloudstock.service.dto.AttributeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Attribute and its DTO AttributeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AttributeMapper extends EntityMapper <AttributeDTO, Attribute> {
    
    @Mapping(target = "attributeItems", ignore = true)
    Attribute toEntity(AttributeDTO attributeDTO); 
    default Attribute fromId(Long id) {
        if (id == null) {
            return null;
        }
        Attribute attribute = new Attribute();
        attribute.setId(id);
        return attribute;
    }
}
