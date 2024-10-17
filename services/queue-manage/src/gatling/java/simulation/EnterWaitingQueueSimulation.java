package simulation;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.UUID;

import static io.gatling.javaapi.core.CoreDsl.bodyString;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.core.OpenInjectionStep.atOnceUsers;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class EnterWaitingQueueSimulation extends Simulation {
    private static final String BASE_URL = "http://localhost:10031/api/v1/waiting-queue";

    private final HttpProtocolBuilder httpProtocolBuilder = http
            .baseUrl(BASE_URL);

    private ScenarioBuilder waitingQueueScenario() {
        return scenario("Enter Waiting Queue Scenario")
                .exec(session -> {
                    String userId = UUID.randomUUID().toString();
                    return session.set("userId", userId);
                })
                .exec(http("대기열 진입")
                        .post(session -> String.format("/%s?performanceId=1", session.getString("userId")))
                        .check(status().is(200))
                        .check(bodyString().saveAs("responseBody"))
                )
                .exec(session -> {
                    String responseBody = session.getString("responseBody");
                    System.out.println("Response Body: " + responseBody);
                    return session;
                })
                .pause(1);
    }

    {
        setUp(
                waitingQueueScenario()
                        .injectOpen(
                                atOnceUsers(1000) // 1000명 동시 접속
                        )
                        .protocols(httpProtocolBuilder)
        );
    }
}