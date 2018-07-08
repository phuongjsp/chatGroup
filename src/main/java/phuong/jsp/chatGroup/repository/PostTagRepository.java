package phuong.jsp.chatGroup.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import phuong.jsp.chatGroup.entities.PostTag;

import java.util.List;

public interface PostTagRepository extends CrudRepository<PostTag, Integer> {
    @Transactional
    List<PostTag> findAllByPostId(int postId);

    @Modifying
    void deleteByPostId(int postId);
}
