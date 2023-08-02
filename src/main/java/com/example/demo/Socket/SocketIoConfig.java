package com.example.demo.Socket;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;;
@org.springframework.context.annotation.Configuration
public class SocketIoConfig {

    @Value("${socketio.server.host}")
    private String host;

    @Value("${socketio.server.port}")
    private int port;


    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setPort(port);
        System.out.println("socket connection created.");
        return new SocketIOServer(config);
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }

}
