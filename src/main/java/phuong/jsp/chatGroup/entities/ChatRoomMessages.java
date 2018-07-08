package phuong.jsp.chatGroup.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "chat_room_messages")
@Data
@NoArgsConstructor
@ToString
public class ChatRoomMessages implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "from_user")
    private String fromUser;
    @Column(name = "to_room")
    private int toRoom;
    @Column(name = "content")
    private String content;
    @Column(name = "time")
    private Date time;
}
