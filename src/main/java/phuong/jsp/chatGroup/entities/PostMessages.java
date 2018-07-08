package phuong.jsp.chatGroup.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post_messages")
@Data
@NoArgsConstructor
public class PostMessages implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "username", nullable = false, length = 50)
    private String username;
    @Column(name = "title", nullable = false, length = 120)
    private String title;
    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "date_time")
    private Date dateTime;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "post_vote", joinColumns = {
            @JoinColumn(name = "post_id")
    }, inverseJoinColumns = {
            @JoinColumn(name = "id")
    })
    @Fetch(value = FetchMode.SUBSELECT)
    private List<PostVote> postVotes;

    @OneToMany(cascade = {CascadeType.REMOVE})
    @JoinTable(name = "post_tag", joinColumns = {
            @JoinColumn(name = "post_id")
    }, inverseJoinColumns = {
            @JoinColumn(name = "id")
    })
    @Fetch(value = FetchMode.SELECT)

    private List<PostTag> postTags;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "post_images", joinColumns = {
            @JoinColumn(name = "post_id")
    }, inverseJoinColumns = {
            @JoinColumn(name = "id")
    })
    @Fetch(value = FetchMode.SUBSELECT)
    private List<PostImages> postImages;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "post_files", joinColumns = {
            @JoinColumn(name = "post_id")
    }, inverseJoinColumns = {
            @JoinColumn(name = "id")
    })
    @Fetch(value = FetchMode.SUBSELECT)
    private List<PostFiles> postFiles;
}
