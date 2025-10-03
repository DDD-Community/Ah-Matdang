package be.ddd.api.member.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FCMTokenUpdateRequest {

    @NotBlank(message = "FCM 토큰은 필수입니다.")
    private String fcmToken;

    public FCMTokenUpdateRequest(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
