package org.uresti.pozarreal.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "pozarreal")
@Data
public class PozarrealConfig {
    private String datasourceUrl;

    private FeeConfig fees;
}
