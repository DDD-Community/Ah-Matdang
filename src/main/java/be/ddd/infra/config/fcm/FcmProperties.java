package be.ddd.infra.config.fcm;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@ConfigurationProperties(prefix = "fcm")
@Validated
public class FcmProperties {

    @NotBlank(message = "FCM 설정값 비어있음")
    private String serviceAccountJsonB64;
}
