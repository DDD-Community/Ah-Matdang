package be.ddd.api.dto.res;

import be.ddd.application.beverage.dto.BeverageSearchDto;
import java.util.List;

public record BeverageSearchResultDto(
        List<BeverageSearchDto> beverageSearchResults, long likeCount) {}
