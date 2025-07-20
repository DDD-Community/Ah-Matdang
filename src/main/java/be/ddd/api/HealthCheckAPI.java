package be.ddd.api;

import be.ddd.common.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckAPI {

    @GetMapping("/ping")
    public ApiResponse<?> pong() {
        return ApiResponse.success("pong");
    }
}
