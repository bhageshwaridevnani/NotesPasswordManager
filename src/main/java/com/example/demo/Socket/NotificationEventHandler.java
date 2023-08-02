package com.example.demo.Socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.example.demo.Controller.BaseController;
import com.example.demo.DTO.NotificationDTO;
import com.example.demo.Model.EntityNotification;
import com.example.demo.Model.EntitySocketConnection;
import com.example.demo.Model.EntityUser;
import com.example.demo.Repositroy.SocketConnectionRepository;
import com.example.demo.Repositroy.UserRepository;;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class NotificationEventHandler extends BaseController {

    @Autowired
    private final SocketIOServer server;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SocketConnectionRepository socketConnectionRepository;

    @Autowired
    private MongoTemplate mongoTemplate;


    public NotificationEventHandler(SocketIOServer server) {
        this.server = server;
    }

    @OnConnect
    public void onConnect(SocketIOClient client) {
        logger.info("web socket connection nap: " + client.getNamespace().getName() + " and url:" + client.getHandshakeData().getUrl());
        logger.info("web socket connection: " + client.getSessionId());
        logger.info("web socket connection: single param" + client.getHandshakeData().getSingleUrlParam("userId"));
        // Logic to handle WebSocket client connection
        System.out.println("A new client connected: " + client.getSessionId());
        authorizeClient(client.getHandshakeData(), client.getSessionId());
    }

    @OnEvent(value = "notification/list")
    public void onEventGetNotificationList(SocketIOClient client, AckRequest request) {
        logger.info("getNotificationList Event is called!!");
        HandshakeData handshakeData = client.getHandshakeData();
        long userId = Long.parseLong(handshakeData.getSingleUrlParam("userId"));
        try {
            Criteria criteria = new Criteria();
            criteria.and(EntityNotification.Fields.userId).is(userId)
                    .and(EntityNotification.Fields.deleted).is(false);
            List<NotificationDTO> notificationList = mongoTemplate.find(Query.query(criteria), NotificationDTO.class, "entityNotification");
            request.sendAckData(okSuccessResponse(notificationList, "common_success"));
        } catch (Exception e) {
            request.sendAckData(okFailResponse(e.getMessage()));
        }
    }


    private void authorizeClient(HandshakeData handshakeData, UUID sessionId) {
        long userId = Long.parseLong(handshakeData.getSingleUrlParam("userId"));
        String namespace = handshakeData.getSingleUrlParam("X-TYPE");
        EntityUser entityUser = userRepository.findByUserId(userId);
        if (entityUser != null) {
            EntitySocketConnection socketConnection = new EntitySocketConnection(
                    sessionId.getMostSignificantBits(),
                    sessionId.getLeastSignificantBits(),
                    sessionId.toString(),
                    new Date(),
                    userId);
            if (StringUtils.isNotEmpty(namespace)) {
                socketConnection.setNameSpace(namespace);
            }
            socketConnectionRepository.save(socketConnection);
        } else {
            logger.error("Socket Authentication failed.");
        }
    }

    public void sendNotificationToUser(EntityUser user) {
        Criteria criteria = new Criteria();
        criteria.and(EntitySocketConnection.Fields.userId).is(user.getUserId())
                .and(EntitySocketConnection.Fields.nameSpace).is("NOTIFICATION");
        EntitySocketConnection entitySocketConnection = mongoTemplate.findOne(Query.query(criteria), EntitySocketConnection.class);
        if (entitySocketConnection != null) {
            String message = "Hello " + user.getUserName() + ", this is a reminder that you set your master password 30 days ago. As part of our commitment to ensuring the security of your account, we kindly request that you change your password.";
            String title = "Master Password";
            EntityNotification entityNotification = new EntityNotification(user.getUserId(), message, title, false);
            mongoTemplate.save(entityNotification);
            NotificationDTO notificationDTO = new NotificationDTO(message, title, false);
            SocketIOClient client = server.getClient(new UUID(entitySocketConnection.getMostSigBits(), entitySocketConnection.getLeastSigBits()));
            if (client != null) {
                client.sendEvent("notificationEvent", notificationDTO);
            }
        }
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        logger.info("socket disconnect, sessionId:" + client.getSessionId());
        socketConnectionRepository.deleteBySessionId(client.getSessionId().toString());
    }
}
