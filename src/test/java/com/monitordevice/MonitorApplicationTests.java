package com.monitordevice;

import com.monitordevice.client.MessageSendingDevice;
import com.monitordevice.data.Message;
import com.monitordevice.server.Server;
import com.monitordevice.server.ServerHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
class MonitorApplicationTests {
    @Test
    void totalMessageCountTest() throws IOException, ExecutionException, InterruptedException {
        List<Future<?>> futures = new ArrayList<>();
        ExecutorService executionPool = Executors.newFixedThreadPool(2);

        DummyServer dummyserver = new DummyServer();
        dummyserver.start();
        /*
         * Launching blocking operation '.get()' in order to waiting for each client to be done
         */

        for (int i = 0; i < 1; i++) {
            Long id = UUID.randomUUID().getMostSignificantBits()
                    & Long.MAX_VALUE;
            Runnable runnable = new MessageSendingDevice(id);
            Future<?> future =
                    executionPool.submit(runnable);

            futures.add(future);
        }
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        assertEquals(0, 0);

    }

}

class DummyServer extends Server {
    int messageCount = 0;
    Socket client;

    public DummyServer() throws IOException {
        super(8046);
        this.server = new ServerSocket(8047);
    }

    public void start() throws IOException {
        try {
            acceptConnection();
            ServerHandler clientSock = new ServerHandler(client, this);
            new Thread(clientSock).start();
        } finally {
            try {
                server.close();
                isactive = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Message<?> handleRequest(Message<?> request) {
        messageCount++;
        return null;
    }

    public void acceptConnection() throws IOException {
        this.client = this.server.accept();
    }
}
