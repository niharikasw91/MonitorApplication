package com.monitordevice.server;

import com.monitordevice.configurations.Config;
import com.monitordevice.data.Message;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** It is going to handle upto 10 client requests at a time.
 * @author Niharika Sweta
 */
@Slf4j
@Getter
@Setter
@Async
public abstract class Server {
  final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(Config.THREAD_COUNT);
  protected int messageCounter;
  protected static int deviceSpecificMessageCount;
  protected static Map<Long, Integer> connectedDevice;

  protected ServerSocket server;
  private int port;
  protected boolean isactive = true;
  private Socket client;

  public Server(int port){
    this.port = port;
  }
  public void start() throws IOException {
    this.server = new ServerSocket(port);

    try {
      while (isActive()) {
        // socket object to receive incoming client connection requests
        acceptConnection();
        // Displaying that new client is connected to server
        log.info("New client connected : {} at port {}" , client.getInetAddress().getHostAddress(), this.port);
        // create a new thread object
        ServerHandler clientSock = new ServerHandler(client,this);
        // This thread will handle the client separately
        new Thread(clientSock).start();
      }
    } finally {
        try {
          server.close();
          isactive = false;
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
  }

  private boolean isActive() {
    return isactive;
  }
  public void acceptConnection() throws IOException {
    //Socket
    this.client = server.accept();
  }

  public abstract Message<?> handleRequest(Message<?> request);
}
