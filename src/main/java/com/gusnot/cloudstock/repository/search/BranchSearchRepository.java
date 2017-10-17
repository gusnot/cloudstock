package com.gusnot.cloudstock.repository.search;

import com.gusnot.cloudstock.domain.Branch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Branch entity.
 */
public interface BranchSearchRepository extends ElasticsearchRepository<Branch, Long> {
}
