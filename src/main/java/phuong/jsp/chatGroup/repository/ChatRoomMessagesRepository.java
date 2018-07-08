package phuong.jsp.chatGroup.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import phuong.jsp.chatGroup.entities.ChatRoomMessages;

import java.util.List;

public interface ChatRoomMessagesRepository extends CrudRepository<ChatRoomMessages, Integer> {

    List<ChatRoomMessages> findAllByToRoomOrderByIdDesc(Integer toRoom, Pageable pageable);

    void deleteAllByToRoom(int room);
}
