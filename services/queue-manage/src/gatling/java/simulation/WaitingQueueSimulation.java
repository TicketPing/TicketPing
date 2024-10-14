package simulation;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.UUID;

import static com.ticketPing.queue_manage.infrastructure.utils.ConfigHolder.serverPort;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class WaitingQueueSimulation extends Simulation {
    private static final String BASE_URL = "http://localhost:" + serverPort() + "/api/waiting-queue";

    private final HttpProtocolBuilder httpProtocolBuilder = http
            .baseUrl(BASE_URL);

    private ScenarioBuilder waitingQueueQueueScenario() {
        String userId = UUID.randomUUID().toString();
        return scenario("Enter Waiting Queue Scenario")
                .exec(http("대기열 진입")
                    .post("/" + userId + "?performanceName=" + "공연1")
                    .check(status().is(200)))
                .pause(1)
                .exec(http("대기열 상태 조회")
                        .get("/" + userId + "?performanceName=" + "공연1")
                        .check(status().is(200))
                );
    }

    {
        setUp(
                waitingQueueQueueScenario()
                        .injectOpen(rampUsers(1000).during(Duration.ofSeconds(10)))
                        .protocols(httpProtocolBuilder)
        );
    }

}
