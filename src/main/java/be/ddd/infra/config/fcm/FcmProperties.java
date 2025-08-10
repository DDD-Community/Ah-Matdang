package be.ddd.infra.config.fcm;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "fcm")
public class FcmProperties {

    @NotBlank(message = "fcm sdk 비어있을경우 에러")
    private String serviceAccountPath;
}
