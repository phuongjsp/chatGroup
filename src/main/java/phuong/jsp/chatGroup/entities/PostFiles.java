package phuong.jsp.chatGroup.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "post_images")
@Data
@NoArgsConstructor
public class PostFiles implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "post_id")
    private int postId;
    @Column(name = "url_file", nullable = false)
    private String urlFile;
    @Column(name = "alt")
    private String alt;
}
