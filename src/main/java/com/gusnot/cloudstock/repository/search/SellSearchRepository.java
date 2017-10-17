package com.gusnot.cloudstock.repository.search;

import com.gusnot.cloudstock.domain.Sell;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Sell entity.
 */
public interface SellSearchRepository extends ElasticsearchRepository<Sell, Long> {
}
