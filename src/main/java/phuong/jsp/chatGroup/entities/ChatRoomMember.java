package phuong.jsp.chatGroup.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "chat_room_members")
@Data
@NoArgsConstructor
public class ChatRoomMember implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    //    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "room_chat_id")
    private int roomChatId;
    @Column(name = "username")
    private String username;
    @Column(name = "time_add")
    private Date time_add;
    @Column(name = "is_create")
    private boolean isCreate;
}
