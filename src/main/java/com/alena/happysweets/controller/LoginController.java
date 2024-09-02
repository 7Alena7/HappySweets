package com.alena.happysweets.controller;

import com.alena.happysweets.global.GlobalData;
import com.alena.happysweets.model.Role;
import com.alena.happysweets.model.User;
import com.alena.happysweets.repository.RoleRepository;
import com.alena.happysweets.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
//Controller class for login operations
@Controller
public class LoginController {
    Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final BCryptPasswordEncoder BCryptPasswordEncoder;
    UserRepository userRepository;
    RoleRepository roleRepository;
    @Autowired
    public LoginController(BCryptPasswordEncoder BCryptPasswordEncoder, UserRepository userRepository, RoleRepository roleRepository){
        this.BCryptPasswordEncoder = BCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/login")
    public String login(){
        GlobalData.cart.clear();
        return "login";
    }
    @GetMapping("/register")
    public String registerGet(){
        return "register";
    }
    @PostMapping("/register")
    public String registerPost(@ModelAttribute("user")User user, HttpServletRequest request)throws ServletException{
        String password = user.getPassword();
        user.setPassword(BCryptPasswordEncoder.encode(password));
        List<Role>roles = new ArrayList<>();
        Optional<Role> role = roleRepository.findById(2);
        if(role.isPresent()){
        roles.add(role.get());
        }else{
            logger.warn("Role not found, redirecting to 404");
            return "404";
        }
        user.setRoles(roles);
        userRepository.save(user);
        logger.info("New user with USER_ROLE has been created");
        request.login(user.getEmail(), password);
        return "redirect:/";
    }
}
