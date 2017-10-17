package com.gusnot.cloudstock.repository.search;

import com.gusnot.cloudstock.domain.BillItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BillItem entity.
 */
public interface BillItemSearchRepository extends ElasticsearchRepository<BillItem, Long> {
}
