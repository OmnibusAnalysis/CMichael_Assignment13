package com.coderscampus.assignment13.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class info {

    @GetMapping("info")
    public String branchInfo() {
        return "info";
    }
    
}
