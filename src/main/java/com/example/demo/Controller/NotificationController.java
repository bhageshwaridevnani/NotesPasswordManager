//package com.example.demo.Controller;
//
//import com.corundumstudio.socketio.SocketIONamespace;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class NotificationController {
//    @Autowired
//    private  SocketIONamespace socketIONamespace;
//
//    public NotificationController(SocketIONamespace socketIONamespace) {
//        this.socketIONamespace = socketIONamespace;
//    }
//
//    @PostMapping("/sendNotification")
//    public void sendNotification(@RequestBody String message) {
//        socketIONamespace.getBroadcastOperations().sendEvent("notification", message);
//    }
//}
