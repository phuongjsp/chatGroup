package phuong.jsp.chatGroup.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import phuong.jsp.chatGroup.entities.Messages;
import phuong.jsp.chatGroup.repository.*;
import phuong.jsp.chatGroup.service.SendMessagesService;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class MessagesBothUserController {
    private SimpMessagingTemplate messagingTemplate;
    private MessagesRepository messagesRepository;
    private UserRepository userRepository;
    private ChatRoomMessagesRepository chatRoomMessagesRepository;
    private ChatRoomRepository chatRoomRepository;
    private ChatRoomMembersRepository chatRoomMembersRepository;
    private SendMessagesService sendMessagesService;

    @MessageMapping("/message")
    public void processMessageFromClient(
            @Payload Map<String, String> message, Principal principal) throws Exception {
        sendMessagesService.sendMessages (message, principal.getName ());
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage ();
    }

    @RequestMapping(path = {"/getoldMessages"}, method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Messages> addAjaxSuccess(@RequestBody Map<String, Object> map) {
        Pageable topTen = PageRequest.of ((Integer) map.get ("min"), (Integer) map.get ("max"));
        return messagesRepository.findLimitMessagesFormBothUser (
                map.get ("from_user").toString (),
                map.get ("to_user").toString (),//can to use principal.getName ()
                topTen
        );
    }

    @RequestMapping(path = {"/setReaded"}, method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String setReadedForMessage(@RequestBody Map<String, Object> map) {
        Messages messages = messagesRepository.findById ((Integer) map.get ("id")).get ();
        messagesRepository.setOrtherMessagesHasReaded (messages.getFromUser (), messages.getToUser ());
        return messages.getFromUser ();
    }


}

