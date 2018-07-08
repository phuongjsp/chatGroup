package phuong.jsp.chatGroup.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import phuong.jsp.chatGroup.entities.Messages;

import java.util.List;

public interface MessagesRepository extends CrudRepository<Messages, Integer> {
    @Query("select m from Messages m where (from_user= ?1 and to_user=?2) or (from_user=?2 and to_user=?1) order by id desc")
    List<Messages> findLimitMessagesFormBothUser(String from_user, String to_user, Pageable pageable);

    @Query(nativeQuery = true, value = " select distinct m.from_user as tu from messages m where to_user = ?1 and readed=false ")
    List<String> listUserHasnotReaded(String user);

    Messages getFirstByFromUserAndToUserAndReadedOrderByIdDesc(String fromUser, String toUser, boolean readed);

    @Transactional
    @Modifying //QueryExecutionRequestException: Not supported for DML operations delete or update
    @Query("update Messages set readed = true where from_user=?1 and to_user=?2 ")
    void setOrtherMessagesHasReaded(String fromUser, String toUser);
}
