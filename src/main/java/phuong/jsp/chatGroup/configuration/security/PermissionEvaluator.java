package phuong.jsp.chatGroup.configuration.security;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import phuong.jsp.chatGroup.entities.User;
import phuong.jsp.chatGroup.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.io.Serializable;
import java.util.Objects;

@Component
@AllArgsConstructor
public class PermissionEvaluator implements org.springframework.security.access.PermissionEvaluator {

    private static final Logger LOG = LoggerFactory.getLogger (PermissionEvaluator.class);
    private final UserRepository userRepository;


    @Override
    public boolean hasPermission(Authentication authentication, Object o, Object o1) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false;
    }


    public boolean hasPermissionForUser(Authentication authentication, Integer userId) {
        if ( userId == null ) return false;
        if ( userRepository.getById (userId) == null ) {//TODO can xem lai
            throw new EntityNotFoundException (
                    String.format ("User with id %d not found", userId));
        }
        User authenticationUser = userRepository.findByUsername (authentication.getName ());
        if ( Objects.equals (authenticationUser.getId (), userId) ) {
            return true;
        }
        LOG.error ("User with id {} does not have permission", userId);
        return false;
    }


}