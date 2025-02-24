package com.coderscampus.assignment13.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Locations {

    @GetMapping("locations")
    public String showLocations() {
        return "locations";
    }
    
}
