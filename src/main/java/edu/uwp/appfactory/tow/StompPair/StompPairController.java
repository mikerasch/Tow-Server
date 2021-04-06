//package edu.uwp.appfactory.tow.StompPair;
//
//public class StompPairController {
//}








//    @MessageMapping("/message")
//    @SendToUser("/queue/reply")
//    public String processMessageFromClient(
//            @Payload String message,
//            Principal principal) throws Exception {
//        return gson
//                .fromJson(message, Map.class)
//                .get("name").toString();
//    }


//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.simp.annotation.SendToUser;
//
//@MessageMapping("/greetings")
//@SendToUser("/queue/greetings")
//public String reply(@Payload String message,
//        Principal user) {
//        System.out.println(message);
//        System.out.println(user);
//        return  "Hello " + message;
//        }


//import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
//import org.springframework.messaging.simp.annotation.SendToUser;
//
//@MessageExceptionHandler
//@SendToUser("/queue/errors")
//public String handleException(Throwable exception) {
//        return exception.getMessage();
//        }