package phuong.jsp.chatGroup.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import phuong.jsp.chatGroup.entities.PostVote;

import java.util.List;

@Transactional
public interface PostVoteRepository extends CrudRepository<PostVote, Integer> {
    @Modifying
    @Query(nativeQuery = true, value = "insert post_vote(post_id, username, type )" +
            " values ( ?1 , ?2 , ?3 ) on duplicate key update " +
            "type = ?3")
    void insertOrUpdate(int postId, String username, String type);

    List<PostVote> findAllByPostId(int postId);

    @Modifying
    void deleteByPostIdAndUsername(int postId, String username);

    @Modifying
    void deleteByPostId(int postId);

    boolean existsByPostIdAndUsernameAndType(int postId, String username, String type);

    Integer countAllByPostIdAndType(int postId, String type);
}
