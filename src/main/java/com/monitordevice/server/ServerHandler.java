package com.monitordevice.server;

import com.monitordevice.client.MessageType;
import com.monitordevice.data.Message;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Handles multiple client connection.
 * @author Niharika Sweta
 */
@Slf4j
public class ServerHandler implements Runnable {

  private final Socket clientSocket;
  private final Server server;

  public ServerHandler(Socket socket, Server server) {
    this.clientSocket = socket;
    this.server = server;
  }

  @SneakyThrows
  public void run() {
    Message<?> request = null;
    try {
      ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
      ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

      do {
          request = (Message<?>) in.readObject();

          Message<?> response = server.handleRequest(request);
          out.writeObject(response);

      } while (request.getMessageType() != MessageType.STOP_SEND);
    } catch (Exception e) {
      log.info("Connection interrupted abruptly : "+ e);
    } finally {
      try {
        clientSocket.close();
      /*  server.connectedDevice.remove(request.getDeviceId());
        log.info("Connected devices {}" , server.getConnectedDevice().size());
        if(server.getConnectedDevice().size() == 0){
          server.setMessageCounter(0);
        }*/
      } catch (IOException e) {
        log.error(e.getMessage());
      }
    }
  }
}
