package phuong.jsp.chatGroup.service.Impl;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import phuong.jsp.chatGroup.entities.ChatRoomMessages;
import phuong.jsp.chatGroup.entities.Messages;
import phuong.jsp.chatGroup.repository.ChatRoomMessagesRepository;
import phuong.jsp.chatGroup.repository.MessagesRepository;
import phuong.jsp.chatGroup.service.SendMessagesService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class SendMessagesServiceImpl implements SendMessagesService {
    private ChatRoomMessagesRepository chatRoomMessagesRepository;
    private SimpMessagingTemplate messagingTemplate;
    private MessagesRepository messagesRepository;

    @Override
    public void sendMessages(Map<String, String> message, String name) {
        Messages messages = new Messages ();
        messages.setFromUser (name);
        messages.setToUser (message.get ("sendto"));
        messages.setContent (message.get ("content"));
        messages.setReaded (false);
        messages.setDateTime (new Date ());
        messages = messagesRepository.save (messages);
        String d = "/queue/" + messages.getToUser ();
        this.messagingTemplate.convertAndSend (d, messages);
    }

    @Override
    public void sendMessagesToRoom(String sender, String content, String kick, int room) {
        Map<String, Object> message = new HashMap<> ();
        message.put ("sender", sender);
        message.put ("content", content);
        Date now = new Date ();
        if ( kick != null ) message.put ("kick", kick);
        message.put ("date", now);
        String d = "/topic/public." + room;
        ChatRoomMessages chatRoomMessages = new ChatRoomMessages ();
        chatRoomMessages.setToRoom (room);
        chatRoomMessages.setFromUser (sender);
        chatRoomMessages.setTime (now);
        chatRoomMessages.setContent (content);
        chatRoomMessagesRepository.save (chatRoomMessages);
        this.messagingTemplate.convertAndSend (d, message);
    }
}
