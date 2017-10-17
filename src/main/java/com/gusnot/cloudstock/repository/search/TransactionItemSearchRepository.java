package com.gusnot.cloudstock.repository.search;

import com.gusnot.cloudstock.domain.TransactionItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TransactionItem entity.
 */
public interface TransactionItemSearchRepository extends ElasticsearchRepository<TransactionItem, Long> {
}
