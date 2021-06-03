package org.uresti.pozarreal.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pozarreal")
@Data
public class PozarrealConfig {
    private String datasourceUrl;
}
