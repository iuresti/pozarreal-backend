package org.uresti.pozarreal.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "dropbox")
@Data
public class DropBoxConfig {
    private String accessToken;
    private String clientIdentifier;
}
