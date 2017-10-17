package com.gusnot.cloudstock.service.mapper;

import com.gusnot.cloudstock.domain.*;
import com.gusnot.cloudstock.service.dto.AppConfigDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity AppConfig and its DTO AppConfigDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AppConfigMapper extends EntityMapper <AppConfigDTO, AppConfig> {
    
    
    default AppConfig fromId(Long id) {
        if (id == null) {
            return null;
        }
        AppConfig appConfig = new AppConfig();
        appConfig.setId(id);
        return appConfig;
    }
}
