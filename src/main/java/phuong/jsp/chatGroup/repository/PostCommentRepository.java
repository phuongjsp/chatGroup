package phuong.jsp.chatGroup.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import phuong.jsp.chatGroup.entities.PostComment;

import java.util.List;

@Transactional
public interface PostCommentRepository extends CrudRepository<PostComment, Integer> {
    List<PostComment> findAllByPostId(int postId);

    List<PostComment> findAllByPostIdAndIdBeforeOrderByIdDesc(int postId, int maxId, Pageable pageable);

    List<PostComment> findAllByPostIdOrderByIdDesc(int postId, Pageable pageable);

    @Modifying
    void deleteByPostId(int postId);

    Integer countAllByPostId(int postId);
}
