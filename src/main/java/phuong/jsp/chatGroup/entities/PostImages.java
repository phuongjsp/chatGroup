package phuong.jsp.chatGroup.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "post_images")
@Data
@NoArgsConstructor
public class PostImages implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "post_id")
    private int postId;
    @Column(name = "url", nullable = false, length = 120)
    private String url;
    @Column(name = "name", nullable = false, length = 50)
    private String name;
}
