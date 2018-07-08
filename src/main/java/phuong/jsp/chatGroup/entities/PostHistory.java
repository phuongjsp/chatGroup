package phuong.jsp.chatGroup.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "post_histories")
@Data
@NoArgsConstructor
public class PostHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "title", nullable = false, length = 120)
    private String title;
    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "date_time")
    private Date dateTime;
    @Column(name = "post_id")
    private int postId;

    public PostHistory(String title, String content, Date dateTime, int postId) {
        this.title = title;
        this.content = content;
        this.dateTime = dateTime;
        this.postId = postId;
    }
}
