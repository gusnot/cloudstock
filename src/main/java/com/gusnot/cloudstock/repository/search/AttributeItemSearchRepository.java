package com.gusnot.cloudstock.repository.search;

import com.gusnot.cloudstock.domain.AttributeItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the AttributeItem entity.
 */
public interface AttributeItemSearchRepository extends ElasticsearchRepository<AttributeItem, Long> {
}
