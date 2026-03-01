package it.univr.track.controller.web;

import it.univr.track.entity.TrackData;
import it.univr.track.entity.enumeration.Role;
import it.univr.track.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserWebController {
    private UserRepository userRepository;

    @RequestMapping("/")
    public String login(){
        return "redirect:/singIn";
    }

    //crete user account
    @RequestMapping("/singUp")
    public String singUp() {
        return "singUp";
    }

    //authentication (users)
    @RequestMapping("/singIn")
    public String singIn() {
        return "singIn";
    }

    @PostMapping("/singIn")
    public void onSignIn() {
        TrackData trackData = new TrackData();
        trackData.setUsername("test");
        trackData.setPassword("password");
        trackData.setRole(Role.USER);
        userRepository.save(trackData);
    }

    //edit user profile
    @RequestMapping("/profile")
    public String profile() {
        return "profile";
    }

}
