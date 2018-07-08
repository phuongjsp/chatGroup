package phuong.jsp.chatGroup.repository;

import org.springframework.data.repository.CrudRepository;
import phuong.jsp.chatGroup.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Role findByName(String name);
}
