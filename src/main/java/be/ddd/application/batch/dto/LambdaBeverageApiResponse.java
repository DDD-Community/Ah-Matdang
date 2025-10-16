package be.ddd.application.batch.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LambdaBeverageApiResponse {
    private List<LambdaBeverageDto> data;
}
