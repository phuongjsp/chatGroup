package phuong.jsp.chatGroup.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import phuong.jsp.chatGroup.entities.PostMessages;

import java.util.List;

@Transactional
public interface PostMessagesRepository extends CrudRepository<PostMessages, Integer> {

    @Query(value = "select p.username from PostMessages p where  p.id = ?1")
    String isCreatePost(int postId);

    @Query(value = "select p from PostMessages p order by p.id desc ")
    List<PostMessages> findAllOrderById(Pageable pageable);


    PostMessages save(PostMessages postMessages);

}
