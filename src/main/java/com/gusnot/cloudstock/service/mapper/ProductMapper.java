package com.gusnot.cloudstock.service.mapper;

import com.gusnot.cloudstock.domain.*;
import com.gusnot.cloudstock.service.dto.ProductDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Product and its DTO ProductDTO.
 */
@Mapper(componentModel = "spring", uses = {ProductTypeMapper.class, AttributeMapper.class, })
public interface ProductMapper extends EntityMapper <ProductDTO, Product> {

    @Mapping(source = "productType.id", target = "productTypeId")
    @Mapping(source = "productType.name", target = "productTypeName")
    ProductDTO toDto(Product product); 

    @Mapping(source = "productTypeId", target = "productType")
    Product toEntity(ProductDTO productDTO); 
    default Product fromId(Long id) {
        if (id == null) {
            return null;
        }
        Product product = new Product();
        product.setId(id);
        return product;
    }
}
