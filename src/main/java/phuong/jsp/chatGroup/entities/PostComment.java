package phuong.jsp.chatGroup.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "post_comment")
@Data
@NoArgsConstructor

public class PostComment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "username")
    private String username;
    @Column(name = "post_id")
    private int postId;
    @Column(name = "content")
    private String content;
    @Column(name = "date_time")
    private Date dateTime;

    public PostComment(String username, int postId, String content, Date dateTime) {
        this.username = username;
        this.postId = postId;
        this.content = content;
        this.dateTime = dateTime;
    }
}
