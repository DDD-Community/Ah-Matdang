package be.ddd.api.admin;

import be.ddd.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController()
@RequestMapping("/admin")
@RequiredArgsConstructor
@Profile("!prod")
public class BeverageBatchAPI {

    private final JobLauncher jobLauncher;
    private final Job beverageJob;
    private final WebClient webClient = WebClient.create();

    @PostMapping("/trigger-beverage-batch")
    public ApiResponse<?> trigger() throws Exception {
        JobParameters params =
                new JobParametersBuilder()
                        .addLong("run.id", System.currentTimeMillis())
                        .toJobParameters();
        JobExecution exec = jobLauncher.run(beverageJob, params);
        return ApiResponse.success("Batch status: " + exec.getStatus());
        /*return webClient
        .get()
        .uri("https://dla6sbxferlsb2jl6wtmjvmioe0hdmdc.lambda-url.ap-northeast-2.on.aws/")
        .retrieve()
        .bodyToMono(String.class);*/
    }
}
