package phuong.jsp.chatGroup.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import phuong.jsp.chatGroup.entities.User;

import java.util.List;

@Transactional
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUsername(String username);

    User getById(int id);

    @Query(value = "select u.images from  User u where u.username = ?1")
    String getAvataFormUsername(String username);

    boolean existsUserByUsername(String s);

    int removeByUsername(String s);


    @Query("select u.id from User u where u.username=?1")
    int getIdByUsername(String u);

    @Query("select u.username from User u where u.id=?1")
    String getUsernameById(int id);

    @Query("select u.username from User u")
    List<String> listUsername();

    @Query("select u.username from User u  where lower(u.username) like lower(?1)  order by u.username desc")
    List<String> listUserLikeUsernameLimit(String username, Pageable pageable);
}
