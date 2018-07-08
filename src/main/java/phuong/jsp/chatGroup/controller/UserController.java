package phuong.jsp.chatGroup.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import phuong.jsp.chatGroup.configuration.Layout;
import phuong.jsp.chatGroup.entities.Role;
import phuong.jsp.chatGroup.entities.User;
import phuong.jsp.chatGroup.repository.RoleRepository;
import phuong.jsp.chatGroup.repository.UserRepository;
import phuong.jsp.chatGroup.service.MyUserDetailsService;

import java.security.Principal;
import java.util.*;

@Controller
@Slf4j
@AllArgsConstructor
public class UserController {
    private MyUserDetailsService myUserDetailsService;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    //todo Only admin has role add new user or when do something then add

    @RequestMapping(path = {"/avatar"}, method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, String> getAvatar(@RequestBody String username) {
        Map<String, String> map = new HashMap<> ();
        map.put ("src", userRepository.getAvataFormUsername (username));
        return map;
    }

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @Layout(value = "default", title = "Đăng ký")
    @GetMapping("register")
    public String register(Model model) {
        model.addAttribute ("roles", roleRepository.findAll ());
        return "user/register";
    }

    @Layout(value = "default", title = "Đăng ký")
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String RegisterResult(@RequestParam("username") String username,
                                 @RequestParam("roles") Set<Integer> roles,
                                 Model model) {
        myUserDetailsService.register (username, roles);
        model.addAttribute ("mess", "Register Success user " + username + " has password is 123@123A");
        return "redirect:/listuser";
    }

    @Layout(value = "default", title = "Change password")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR" + "@permissionEvaluator.hasPermissionForUser(authentication, #id)")
    @GetMapping("/changepassword")
    public String changePassword() {
        return "user/changepassword";
    }

    @PostMapping("/changepassword")
    public String changePasswordResult(
            @RequestParam("oldpassword") String oldPassword,
            @RequestParam("newpassword") String newPassword,
            Principal principal,
            Model model) {
        if ( myUserDetailsService.changePassword (oldPassword, newPassword, principal.getName ()) ) {
            model.addAttribute ("message", "Update Password Success !");
            return "index";
        }

        model.addAttribute ("error", true);
        return "redirect:/changepassword";
    }

    @Layout(value = "default", title = "Đăng ký")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/updateroles/{username}")
    public String updateRoles(@PathVariable("username") String username,
                              Model model) {
        Set<Role> roles1 = myUserDetailsService.roles (username);
        model.addAttribute ("rolesOfUser", roles1);
        Iterable<Role> roles2 = roleRepository.findAll ();
        Iterator iterator = roles2.iterator ();
        while (iterator.hasNext ()) {
            Role role1 = (Role) iterator.next ();
            roles1.forEach (role2 -> {
                if ( role2.equalsdao (role1) ) iterator.remove ();
            });
        }
        model.addAttribute ("rolesOutOfUser", roles2);
        return "user/updateroles";
    }

    @Layout(value = "default", title = "Result")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/updateroles/{username}")
    public String updateRolesResult(@PathVariable("username") String username,
                                    @RequestParam("roles") Set<Integer> roles,
                                    Model model) {
        Set<Role> roleSet = new HashSet<> ();
        roles.forEach (integer -> roleSet.add (roleRepository.findById (integer).get ()));
        myUserDetailsService.setRolesOfUser (roleSet, username);
        model.addAttribute ("mess", "Update Roles of " + username + " Successed !");
        return "redirect:/listuser";
    }

    @Layout(value = "default", title = "Result")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/listuser")
    public String listUser(Model model) {
        model.addAttribute ("list", userRepository.findAll ());
        return "user/listUser";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/listuser/delete/{username}")
    public String removeUser(@PathVariable("username") String username,
                             Model model) {
        if ( userRepository.removeByUsername (username) == 1 )
            model.addAttribute ("mess", "Revoce User " + username + " successed !");
        else model.addAttribute ("mess", "Revoce User " + username + " failed !");
        return "redirect:/listuser";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/listuser/changeAble/{username}")
    public String changeAbleUser(@PathVariable("username") String username,
                                 Model model) {
        User user = userRepository.findByUsername (username);
        user.setEnable (!user.isEnable ());
        userRepository.save (user);
        return "redirect:/listuser";
    }

}
