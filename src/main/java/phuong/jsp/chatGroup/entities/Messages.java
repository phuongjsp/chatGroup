package phuong.jsp.chatGroup.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
public class Messages implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    //    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "from_user")
    private String fromUser;
    //    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "to_user")
    private String toUser;
    @Column(name = "date_time")
    private Date dateTime;
    @Column(name = "content")
    private String content;
    @Column(name = "readed", columnDefinition = "TINYINT(1)")
    private boolean readed;
}
