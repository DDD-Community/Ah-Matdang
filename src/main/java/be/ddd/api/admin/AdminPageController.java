package be.ddd.api.admin;

import be.ddd.api.admin.dto.res.BeverageSizeDto;
import be.ddd.api.admin.dto.res.CafeBrandDto;
import be.ddd.domain.entity.crawling.BeverageSize;
import be.ddd.domain.entity.crawling.CafeBrand;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/beverages")
public class AdminPageController {

    @GetMapping("/add")
    public String beverageAddPage(Model model) {
        model.addAttribute(
                "brands",
                Arrays.stream(CafeBrand.values())
                        .map(CafeBrandDto::new)
                        .collect(Collectors.toList()));
        model.addAttribute(
                "sizes",
                Arrays.stream(BeverageSize.values())
                        .map(BeverageSizeDto::new)
                        .collect(Collectors.toList()));
        return "admin/beverage-add";
    }

    @GetMapping("/list")
    public String beverageListPage(Model model) {
        model.addAttribute(
                "brands",
                Arrays.stream(CafeBrand.values())
                        .map(CafeBrandDto::new)
                        .collect(Collectors.toList()));
        model.addAttribute(
                "sizes",
                Arrays.stream(BeverageSize.values())
                        .map(BeverageSizeDto::new)
                        .collect(Collectors.toList()));
        return "admin/beverage-list";
    }
}
