package phuong.jsp.chatGroup.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import phuong.jsp.chatGroup.entities.PostImages;

import java.util.List;

@Transactional
public interface PostImagesRepository extends CrudRepository<PostImages, Integer> {
    List<PostImages> findAllByPostId(int postId);

    @Modifying
    void deleteByPostId(int postId);


}
