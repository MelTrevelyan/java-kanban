package test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import server.HttpTaskServer;

import java.io.IOException;

public class HttpTaskServerTest {

    private HttpTaskServer taskServer;

    @BeforeEach
    public void beforeEach() throws IOException {
        taskServer = new HttpTaskServer();
        taskServer.start();
    }

    @AfterEach
    public void afterEach() {
        taskServer.stop();
    }
}
