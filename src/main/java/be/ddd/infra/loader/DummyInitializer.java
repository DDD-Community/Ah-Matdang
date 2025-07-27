package be.ddd.infra.loader;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DummyInitializer implements ApplicationRunner {

    private final IntakeHistoryDummyInitializer intakeHistoryDummyInitializer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        intakeHistoryDummyInitializer.insertDummyIntakeHistories();
    }
}
