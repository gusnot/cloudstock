package com.gusnot.cloudstock.service.mapper;

import com.gusnot.cloudstock.domain.*;
import com.gusnot.cloudstock.service.dto.SkuDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Sku and its DTO SkuDTO.
 */
@Mapper(componentModel = "spring", uses = {ProductMapper.class, })
public interface SkuMapper extends EntityMapper <SkuDTO, Sku> {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    SkuDTO toDto(Sku sku); 

    @Mapping(source = "productId", target = "product")
    Sku toEntity(SkuDTO skuDTO); 
    default Sku fromId(Long id) {
        if (id == null) {
            return null;
        }
        Sku sku = new Sku();
        sku.setId(id);
        return sku;
    }
}
