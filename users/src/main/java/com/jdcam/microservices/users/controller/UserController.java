package com.jdcam.microservices.users.controller;

import com.jdcam.microservices.users.entity.User;
import com.jdcam.microservices.users.exception.AlreadyExistsEmailException;
import com.jdcam.microservices.users.services.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
public class UserController {

    private final UserService userService;
    private final ApplicationContext context;

    public UserController(UserService userService, ApplicationContext context) {
        this.userService = userService;
        this.context = context;
    }

    @GetMapping("/")
    public List<User> getUsers(@RequestParam(value = "ids", required = false) List<Long> ids){
        return ids != null && ids.size() > 0 ? this.userService.getUsersByIds(ids) : this.userService.getUsers();
    }

    @GetMapping("/crash")
    public void crash(){
        ((ConfigurableApplicationContext)context).close();
    }   

    @GetMapping("/greet")
    public List<?> greet(){
       return List.of("null", "null", "hol32232332", "null222", "12", "h21", "123123asd", "123123zxc", "122343", "422");
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id){
        return this.userService.getUserById(id).map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createUser(@RequestBody User user) throws AlreadyExistsEmailException {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.create(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody User user,
                                           @PathVariable("id") Long id) throws AlreadyExistsEmailException {
        return this.userService.update(user, id)
                .map(userUpdate -> ResponseEntity.ok(userUpdate))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") long id){
        return this.userService.getUserById(id).map(user -> {
            this.userService.delete(id);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

}
