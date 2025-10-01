package ru.mycrg.backend.integration;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class IntegrationTestContainersConfig {

    static Network network = Network.newNetwork();

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("backend_db")
            .withUsername("pgAdmin")
            .withPassword("pdPass")
            .withNetwork(network)
            .withNetworkAliases("postgres")
            .withInitScript("init-db.sql");

    @Container
    static GenericContainer<?> backend = new GenericContainer<>(DockerImageName.parse("javadip-backend:latest"))
            .withExposedPorts(8084)
            .withNetwork(network)
            .withNetworkAliases("backend")
            .withEnv("SPRING_FLYWAY_USER", "pgAdmin")
            .withEnv("SPRING_FLYWAY_PASSWORD", "pdPass")
            .withEnv("SPRING_DATASOURCE_URL", "jdbc:postgresql://postgres:5432/backend_db")
            .withEnv("SPRING_DATASOURCE_USERNAME", "pgAdmin")
            .withEnv("SPRING_DATASOURCE_PASSWORD", "pdPass")
            .dependsOn(postgres)
            .waitingFor(Wait.forHttp("/actuator/health").forPort(8084));

    @Container
    static GenericContainer<?> frontend = new GenericContainer<>(DockerImageName.parse("javadip-frontend:latest"))
            .withExposedPorts(80)
            .withNetwork(network)
            .withNetworkAliases("frontend")
            .waitingFor(Wait.forHttp("/").forPort(80));
}
