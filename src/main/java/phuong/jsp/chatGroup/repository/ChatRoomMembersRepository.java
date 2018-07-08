package phuong.jsp.chatGroup.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import phuong.jsp.chatGroup.entities.ChatRoomMember;

import java.util.List;
import java.util.Set;

public interface ChatRoomMembersRepository extends CrudRepository<ChatRoomMember, Integer> {

    @Query(nativeQuery = true, value = "select distinct c.username from chat_room_members c where c.room_chat_id = ?1")
    List<String> getMemberFormRoom(int id);

    @Query(nativeQuery = true, value = "select distinct c.room_chat_id from chat_room_members c where c.username = ?1")
    Set<Integer> getRoomFormUser(String username);

    boolean existsByUsernameAndRoomChatId(String username, int chatRoom);

    @Query(nativeQuery = true, value = "select c.is_create from chat_room_members c where c.username = ?1 and c.room_chat_id = ?2")
    boolean isCreate(String username, int room);

    ChatRoomMember getFirstByUsernameAndRoomChatId(String username, int romChatId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "Update chat_room_members c SET c.is_create=?3 WHERE c.username=?1 and c.room_chat_id = ?2")
    void updateIsCreate(String username, int room, boolean isCreate);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from chat_room_members  WHERE username=?1 and room_chat_id = ?2")
    void kickUser(String username, int room);


}
