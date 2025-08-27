package be.ddd;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        properties = {
            // 이미 끄던 것들 + Batch 오토설정도 같이 끔
            "spring.autoconfigure.exclude="
                    + "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,"
                    + "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,"
                    + "org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration,"
                    + "org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration",
            // (선택) 테스트에서 배치 잡 자동 실행 방지
            "spring.batch.job.enabled=false",
            // (선택) FCM 같은 외부 의존성도 테스트에선 끄기
            "fcm.enabled=false"
        })
class DddApplicationTests {

    @Test
    void contextLoads() {}
}
