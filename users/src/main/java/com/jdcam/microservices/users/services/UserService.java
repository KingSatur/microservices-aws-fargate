package com.jdcam.microservices.users.services;

import com.jdcam.microservices.users.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getUsers();

    Optional<User> getUserById(Long id);

    User create(User user);

    void delete(Long id);

    Optional<User> update(User user, Long id);

}
