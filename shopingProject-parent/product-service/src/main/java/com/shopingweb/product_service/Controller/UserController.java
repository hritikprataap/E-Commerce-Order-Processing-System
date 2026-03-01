package com.shopingweb.product_service.Controller;

import com.shopingweb.product_service.Service.UserService;
import com.shopingweb.product_service.dto.Userdto;
import com.shopingweb.product_service.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product/login")
public class UserController {


    private final UserService userService;


    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void CreateUser(@RequestBody Userdto user){
        User newUser = new User();
        newUser.setUserName(user.getUserName());
        newUser.setPassword(user.getPassword());
        userService.saveNewUser(newUser);

    }



}
