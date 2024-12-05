package ru.gav19770210.javapro.task05.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.gav19770210.javapro.task05.entities.UserEntity;
import ru.gav19770210.javapro.task05.services.UserService;

import java.util.List;

@RestController
@RequestMapping(value = "user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{id}/get")
    public ResponseEntity<UserEntity> getUserById(@PathVariable("id") Long id) {
        var userGet = userService.getUserById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userGet);
    }

    @GetMapping(value = "/get-all")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        var users = userService.getAllUsers();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(users);
    }

    @GetMapping(value = "/{name}/get-by-name")
    public ResponseEntity<UserEntity> getUserByName(@PathVariable("name") String name) {
        var userGet = userService.getUserByName(name);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userGet);
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserEntity> createUser(@RequestBody @Validated UserEntity userEntity) {
        var userCreate = userService.createUser(userEntity);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userCreate);
    }

    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserEntity> updateUser(@RequestBody @Validated UserEntity userEntity) {
        var userUpdate = userService.updateUser(userEntity);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userUpdate);
    }

    @DeleteMapping(value = "/{id}/delete")
    public HttpStatus deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return HttpStatus.OK;
    }
}
