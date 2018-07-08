package phuong.jsp.chatGroup.repository;

import org.springframework.data.repository.CrudRepository;
import phuong.jsp.chatGroup.entities.Chatroom;

public interface ChatRoomRepository extends CrudRepository<Chatroom, Integer> {
    Chatroom getFirstByName(String name);
}
