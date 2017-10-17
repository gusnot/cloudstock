package com.gusnot.cloudstock.repository.search;

import com.gusnot.cloudstock.domain.Attribute;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Attribute entity.
 */
public interface AttributeSearchRepository extends ElasticsearchRepository<Attribute, Long> {
}
