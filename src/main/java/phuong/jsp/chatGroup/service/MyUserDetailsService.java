package phuong.jsp.chatGroup.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import phuong.jsp.chatGroup.entities.Role;
import phuong.jsp.chatGroup.entities.User;
import phuong.jsp.chatGroup.repository.RoleRepository;
import phuong.jsp.chatGroup.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class MyUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername (username);
        org.springframework.security.core.userdetails.User.UserBuilder builder;
        if ( user != null ) {

            builder = org.springframework.security.core.userdetails.User.withUsername (username);
            builder.disabled (!user.isEnable ());
            builder.password (user.getPassword ());
            String[] authorities = user.getRoles ()
                    .stream ().map (a -> a.getName ()).toArray (String[]::new);

            builder.authorities (authorities);
        } else {
            throw new UsernameNotFoundException ("User not found.");
        }
        return builder.build ();

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User register(String username, Set<Integer> roles) {
        User user = new User ();
        user.setUsername (username);
        String password = "123@123A";
        Set<Role> roleSet = new HashSet<> ();
        roles.forEach (integer -> roleSet.add (roleRepository.findById (integer).get ()));
        user.setRoles (roleSet);
        user.setPassword (passwordEncoder.encode (password));
        return userRepository.save (user);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR" +
            " @permissionEvaluator.hasPermissionForUser(authentication, #id)")
    public boolean changePassword(String oldPassword, String newPassword, String username) {
        User user = userRepository.findByUsername (username);
        if ( passwordEncoder.matches (oldPassword, user.getPassword ()) ) {
            System.out.println ("password id true");
            user.setPassword (passwordEncoder.encode (newPassword));
            userRepository.save (user);
            return true;
        }
        return false;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public boolean enableUser(String username, boolean e) {
        User user = userRepository.findByUsername (username);
        if ( user.isEnable () != e ) {
            user.setEnable (e);
            return true;
        }
        return false;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public boolean setRolesOfUser(Set<Role> roles, String username) {
        User user = userRepository.findByUsername (username);
        user.setRoles (roles);
        userRepository.save (user);
        return true;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR" +
            " @permissionEvaluator.hasPermissionForUser(authentication, #id)")
    public Set<Role> roles(String username) {
        return userRepository.findByUsername (username).getRoles ();
    }
}
