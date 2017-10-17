package com.gusnot.cloudstock.service;

import com.gusnot.cloudstock.service.dto.ProductTypeDTO;
import java.util.List;

/**
 * Service Interface for managing ProductType.
 */
public interface ProductTypeService {

    /**
     * Save a productType.
     *
     * @param productTypeDTO the entity to save
     * @return the persisted entity
     */
    ProductTypeDTO save(ProductTypeDTO productTypeDTO);

    /**
     *  Get all the productTypes.
     *
     *  @return the list of entities
     */
    List<ProductTypeDTO> findAll();

    /**
     *  Get the "id" productType.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ProductTypeDTO findOne(Long id);

    /**
     *  Delete the "id" productType.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the productType corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<ProductTypeDTO> search(String query);
}
