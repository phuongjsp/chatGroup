package phuong.jsp.chatGroup.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import phuong.jsp.chatGroup.configuration.Layout;
import phuong.jsp.chatGroup.entities.ChatRoomMember;
import phuong.jsp.chatGroup.entities.ChatRoomMessages;
import phuong.jsp.chatGroup.repository.*;
import phuong.jsp.chatGroup.service.SendMessagesService;

import java.security.Principal;
import java.util.*;

@Controller
@AllArgsConstructor
public class ChatRoomController {
    private MessagesRepository messagesRepository;
    private UserRepository userRepository;
    private ChatRoomMessagesRepository chatRoomMessagesRepository;
    private ChatRoomRepository chatRoomRepository;
    private ChatRoomMembersRepository chatRoomMembersRepository;
    private SendMessagesService sendMessagesService;

    @Layout(value = "default", title = "Chat Room")
    @GetMapping("/chat/{room}")
    public String chatExample(@PathVariable("room") Integer room,
                              Principal principal,
                              Model model) {
        if ( !chatRoomMembersRepository
                .existsByUsernameAndRoomChatId
                        (principal.getName (), room) ) {
            model.addAttribute ("error", "User cannot join Room");
            return "403";
        }
        model.addAttribute ("isCreate", chatRoomMembersRepository.isCreate (principal.getName (), room));
        model.addAttribute ("userInRoom", chatRoomMembersRepository.getMemberFormRoom (room));
        Pageable topTen = PageRequest.of (0, 10);
        List<ChatRoomMessages> chatRoomMessages = chatRoomMessagesRepository.findAllByToRoomOrderByIdDesc (room, topTen);
        Collections.reverse (chatRoomMessages);
        model.addAttribute ("oldMessages", chatRoomMessages);
        model.addAttribute ("room", room);
        return "chatroom";
    }

    @Layout(value = "default", title = "Chat Room")
    @GetMapping("/updateRoom/{room}/{username}")
    public String updateRoom(@PathVariable("username") String username,
                             @PathVariable("room") int room,
                             Principal principal,
                             Model model) throws Exception {
        if ( !chatRoomMembersRepository.isCreate (principal.getName (), room) ) {
            model.addAttribute ("error", "Access is denied");
            return "403";
        } else {
            chatRoomMembersRepository.updateIsCreate (principal.getName (), room, false);
            chatRoomMembersRepository.updateIsCreate (username, room, true);
            Map<String, String> messages = new HashMap<> ();
            String content = "Hi.Phòng chat " + chatRoomRepository.findById (room).get ().getName ()
                    + " đã được nhường cho bạn làm trưởng phòng rồi nha,để sử dụng các chức năng của chủ phòng phiền lòng bạn tải lại trang  !";
            messages.put ("content", content);
            messages.put ("sendto", username);
            sendMessagesService.sendMessages (messages, principal.getName ());
            sendMessagesService.sendMessagesToRoom (username, "Đã được lên làm chủ phòng !", null, room);
            return "redirect:/chat/" + room;
        }
    }

    @GetMapping("/quit/{room}")
    public String quitChatRoom(@PathVariable("room") int room,
                               Principal principal, Model model) {
        chatRoomMembersRepository.kickUser (principal.getName (), room);
        List<String> stringList = chatRoomMembersRepository.getMemberFormRoom (room);
        if ( stringList.isEmpty () ) {
            chatRoomMessagesRepository.deleteAllByToRoom (room);
            chatRoomRepository.deleteById (room);
        } else {
            chatRoomMembersRepository.updateIsCreate
                    (stringList.get (0), room, true);
        }
        sendMessagesService.sendMessagesToRoom (principal.getName (), "Đã thoát ra khỏi phòng !", null, room);
        model.addAttribute ("mess", "Quit Room success !");

        return "redirect:/";
    }


    @RequestMapping(path = {"/lisUserLikeName"}, method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<String> lisUserLikeName(@RequestBody Map<String, Object> map) {
        String username = (String) map.get ("username");
        int room = (int) map.get ("room");
        Pageable topfive = PageRequest.of (0, 5);
        List<String> lstUser = userRepository.listUserLikeUsernameLimit ("%" + username + "%", topfive);
        Iterator iterator = lstUser.listIterator ();
        while (iterator.hasNext ()) {
            if ( chatRoomMembersRepository.existsByUsernameAndRoomChatId (iterator.next ().toString (), room) )
                iterator.remove ();
        }

        return lstUser;
    }

    @RequestMapping(path = {"/addUserintoTheRoom"}, method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public boolean addUserintoTheRoom(@RequestBody Map<String, Object> map, Principal principal) {
        int room = (int) map.get ("room");
        String newuser = (String) map.get ("username");
        System.out.println (map);
        if ( !chatRoomMembersRepository.isCreate (principal.getName (), room) ) return false;
        ChatRoomMember chatRoomMember = new ChatRoomMember ();
        chatRoomMember.setUsername (newuser);
        chatRoomMember.setRoomChatId (room);
        chatRoomMember.setCreate (false);
        chatRoomMembersRepository.save (chatRoomMember);
        String content = "User " + newuser + " has Join";
        sendMessagesService.sendMessagesToRoom (principal.getName (), content, null, room);
        String userMessages = "Hi.Ban da duoc them vao phong chat " + chatRoomRepository.findById (room).get ().getName () +
                " !";
        Map<String, String> message = new HashMap<> ();
        message.put ("sendto", newuser);
        message.put ("content", userMessages);
        sendMessagesService.sendMessages (message, principal.getName ());
        return true;
    }

    @MessageMapping("/chat.sendMessage.{room}")
    public void sendMessage(@DestinationVariable Integer room,
                            @Payload Map<String, Object> message, Principal principal) {
        sendMessagesService.sendMessagesToRoom (principal.getName (), message.get ("content").toString (), null, room);
    }

    @RequestMapping(path = {"/getoldMessagesChatRoom"}, method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ChatRoomMessages> addAjaxChatRoom(@RequestBody Map<String, Object> map) {
        Pageable topTen = PageRequest.of ((Integer) map.get ("min"), (Integer) map.get ("max"));
        List<ChatRoomMessages> chatRoomMessages = chatRoomMessagesRepository.findAllByToRoomOrderByIdDesc ((Integer) map.get ("room"), topTen);
        Collections.reverse (chatRoomMessages);
        return chatRoomMessages;
    }

    @RequestMapping(path = {"/kickUser"}, method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public boolean kickUserOutOfRoom(@RequestBody Map<String, Object> map,
                                     Principal principal,
                                     Model model) {
        int room = (int) map.get ("room");
        String username = map.get ("username").toString ();
        if ( !chatRoomMembersRepository.isCreate (principal.getName (), room) ||
                !chatRoomMembersRepository
                        .existsByUsernameAndRoomChatId
                                (username, room) ) {
            model.addAttribute ("error", "Access is denied");
            return false;
        } else {
            chatRoomMembersRepository.kickUser (username, room);
            Map<String, String> messages = new HashMap<> ();
            String content = "Hi.Do một vài vấn để mình đã kick " + username + " ra khỏ phòng chát " + chatRoomRepository.findById (room).get ().getName ()
                    + " !";
            messages.put ("content", content);
            messages.put ("sendto", username);
            sendMessagesService.sendMessagesToRoom (principal.getName (), content, username, room);
            sendMessagesService.sendMessages (messages, principal.getName ());
            return true;
        }
    }
}
