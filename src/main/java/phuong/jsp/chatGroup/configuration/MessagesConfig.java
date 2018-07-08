package phuong.jsp.chatGroup.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import phuong.jsp.chatGroup.entities.Chatroom;
import phuong.jsp.chatGroup.entities.Messages;
import phuong.jsp.chatGroup.repository.ChatRoomMembersRepository;
import phuong.jsp.chatGroup.repository.ChatRoomRepository;
import phuong.jsp.chatGroup.repository.MessagesRepository;
import phuong.jsp.chatGroup.repository.UserRepository;

import java.security.Principal;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Configuration
@AllArgsConstructor
public class MessagesConfig {
    private MessagesRepository messagesRepository;
    private UserRepository userRepository;
    private ChatRoomRepository chatRoomRepository;
    private ChatRoomMembersRepository chatRoomMembersRepository;

    public List<Messages> getMessages(Principal principal) {
        List<Messages> mapList = new LinkedList<> ();
        List<String> userNotRead = messagesRepository.listUserHasnotReaded (principal.getName ());
        userNotRead.forEach (s -> mapList
                .add (messagesRepository
                        .getFirstByFromUserAndToUserAndReadedOrderByIdDesc (
                                s, principal.getName (), false
                        )));
        return mapList;
    }

    public List<String> listUser(String username) {
        List<String> result = userRepository.listUsername ();
        result.remove (username);
        return result;
    }

    public List<Chatroom> chatRoomList(Principal principal) {
        Set<Integer> list = chatRoomMembersRepository.getRoomFormUser (principal.getName ());
        List<Chatroom> chatroomSet = new LinkedList<> ();
        list.forEach (integer -> chatroomSet.add (chatRoomRepository.findById (integer).get ()));
        return chatroomSet;
    }

}
