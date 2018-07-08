package phuong.jsp.chatGroup.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "role")
@Data
@NoArgsConstructor
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name", unique = true, nullable = false, length = 32)
    private String name;
    @Column(name = "description")
    private String description;


    public boolean equalsdao(Role r) {
        if ( this.getId ().equals (r.getId ()) ) return true;
        return this.getName ().equals (r.getName ());
    }

}
