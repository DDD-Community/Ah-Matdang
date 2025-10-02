package be.ddd.application.admin;

import be.ddd.api.admin.dto.res.BeverageDetailResponseDto;
import be.ddd.domain.repo.CafeBeverageRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminBeverageQueryService {

    private final CafeBeverageRepository cafeBeverageRepository;

    public List<BeverageDetailResponseDto> getAllBeverages() {
        return cafeBeverageRepository.findAllWithDetails().stream()
                .map(BeverageDetailResponseDto::new)
                .collect(Collectors.toList());
    }
}
