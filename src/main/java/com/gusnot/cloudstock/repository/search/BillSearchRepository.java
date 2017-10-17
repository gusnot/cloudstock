package com.gusnot.cloudstock.repository.search;

import com.gusnot.cloudstock.domain.Bill;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Bill entity.
 */
public interface BillSearchRepository extends ElasticsearchRepository<Bill, Long> {
}
