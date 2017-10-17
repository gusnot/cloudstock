package com.gusnot.cloudstock.repository.search;

import com.gusnot.cloudstock.domain.AppConfig;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the AppConfig entity.
 */
public interface AppConfigSearchRepository extends ElasticsearchRepository<AppConfig, Long> {
}
