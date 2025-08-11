package com.example.mySecurityProject.controllers;

import com.example.mySecurityProject.domain.user.User;
import com.example.mySecurityProject.dto.RegistrationUserDto;
import com.example.mySecurityProject.exeptions.UserAlreadyExistExeption;
import com.example.mySecurityProject.exeptions.UserNotFoundExeption;
import com.example.mySecurityProject.exeptions.UsersNotFounExeptions;
import com.example.mySecurityProject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity getUsers() {
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (UsersNotFounExeptions e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @GetMapping("/getById/{userId}")

        public ResponseEntity getUserById(@PathVariable long userId) {
            try {
                return ResponseEntity.ok(userService.getById(userId));
            } catch (UserNotFoundExeption e){
                return ResponseEntity.badRequest().body(e.getMessage());
            } catch (Exception e){
                return ResponseEntity.badRequest().body("Error");
            }
        }

//    @PostMapping("/addUser")
//    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
//        return userService.createNewUser(registrationUserDto);
//    }

}
