package com.gusnot.cloudstock.repository.search;

import com.gusnot.cloudstock.domain.Sku;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Sku entity.
 */
public interface SkuSearchRepository extends ElasticsearchRepository<Sku, Long> {
}
