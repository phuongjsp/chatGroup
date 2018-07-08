package phuong.jsp.chatGroup.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "post_tag")
@Data
@NoArgsConstructor
public class PostTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "username")
    private String username;
    @Column(name = "post_id")
    private int postId;

    public PostTag(String username, int postId) {
        this.username = username;
        this.postId = postId;
    }
}
