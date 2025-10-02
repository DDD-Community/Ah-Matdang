package be.ddd.api.admin;

import be.ddd.domain.entity.crawling.BeverageSize;
import be.ddd.domain.entity.crawling.CafeBrand;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/beverages")
public class AdminPageController {

    @GetMapping("/add")
    public String beverageAddPage(Model model) {
        model.addAttribute("brands", CafeBrand.values());
        model.addAttribute("sizes", BeverageSize.values());
        return "admin/beverage-add";
    }

    @GetMapping("/list")
    public String beverageListPage() {
        return "admin/beverage-list";
    }
}
