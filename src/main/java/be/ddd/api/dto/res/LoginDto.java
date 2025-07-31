package be.ddd.api.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginDto {
    private String jwt;
    private String email;
    private String userId;
}
