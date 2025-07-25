package be.ddd;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        properties = {"spring.profiles.active=test", "spring.main.lazy-initialization=true"})
class DddApplicationTests {

    @Test
    void contextLoads() {}
}
