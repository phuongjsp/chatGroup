package phuong.jsp.chatGroup.service;

import java.util.Map;

public interface SendMessagesService {
    void sendMessages(Map<String, String> message, String name);

    void sendMessagesToRoom(String sender, String content, String kick, int room);
}
