package com.gusnot.cloudstock.repository.search;

import com.gusnot.cloudstock.domain.TransferItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TransferItem entity.
 */
public interface TransferItemSearchRepository extends ElasticsearchRepository<TransferItem, Long> {
}
