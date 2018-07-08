package phuong.jsp.chatGroup.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import phuong.jsp.chatGroup.entities.PostHistory;

import java.util.List;

@Transactional
public interface PostHistoryRepository extends CrudRepository<PostHistory, Integer> {
    List<PostHistory> findAllByPostId(int postId);

    @Modifying
    void deleteByPostId(int postId);
}
