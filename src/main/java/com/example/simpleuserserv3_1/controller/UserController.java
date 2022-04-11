package com.example.simpleuserserv3_1.controller;

import com.example.simpleuserserv3_1.resource.User;
import com.example.simpleuserserv3_1.resource.UserCollection;
import com.example.simpleuserserv3_1.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/v1/account")
public class UserController {

    @Value("${env.specific.property1: NOT_SET}")
    private String property1;

    @Value("${env.specific.property2: NOT_SET}")
    private String property2;

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @ResponseStatus(code = HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public User read(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }


    @GetMapping(value = "/users")
    @ResponseStatus(code = HttpStatus.OK)
    public UserCollection search(@RequestParam(name = "first_name", required = false) Optional<String> firstName,
                                 @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize,
                                 @RequestParam(name = "page", required = false, defaultValue = "1") int page) {

        System.out.println("Looking for: " + firstName);
        UserCollection userCollection = userService.findUsers(firstName, pageSize, page);
        return userCollection;

    }

    @GetMapping(value = "/display-properties")
    @ResponseStatus(code = HttpStatus.OK)
    public String displayProperties() {
        return property1 + " | " + property2;
    }



}


