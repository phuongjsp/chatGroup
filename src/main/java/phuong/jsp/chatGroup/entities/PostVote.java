package phuong.jsp.chatGroup.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "post_vote")
@Data
@NoArgsConstructor
public class PostVote implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "username")
    private String username;
    @Column(name = "type")
    private String type;
    @Column(name = "post_id")
    private int postId;

    public PostVote(String username, String type, int postId) {
        this.username = username;
        this.type = type;
        this.postId = postId;
    }
}
