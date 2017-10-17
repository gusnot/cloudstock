package com.gusnot.cloudstock.repository.search;

import com.gusnot.cloudstock.domain.Transfer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Transfer entity.
 */
public interface TransferSearchRepository extends ElasticsearchRepository<Transfer, Long> {
}
