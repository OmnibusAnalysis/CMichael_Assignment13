package com.coderscampus.assignment13.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller 
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";
    }
}
    
//     @GetMapping("/users/new")
//     public String newUser(Model model) {
//         User user = new User();
//         user.setAddress(new Address());
//         model.addAttribute("user", user);
//         return "newUser";
//     }

//     @PostMapping("/users/new")


// }
