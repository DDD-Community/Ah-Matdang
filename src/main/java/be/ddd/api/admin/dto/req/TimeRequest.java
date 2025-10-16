package be.ddd.api.admin.dto.req;

import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

public record TimeRequest(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fixedTime) {}
