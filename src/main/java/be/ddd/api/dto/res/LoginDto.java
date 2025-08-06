package be.ddd.api.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class LoginDto {
    private String jwt;
    private Map<String, Object> userInfo; // 전체 사용자 정보
}
