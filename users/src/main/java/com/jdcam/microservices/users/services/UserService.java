package com.jdcam.microservices.users.services;

import com.jdcam.microservices.users.entity.User;
import com.jdcam.microservices.users.exception.AlreadyExistsEmailException;
import org.bouncycastle.util.Iterable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getUsers();

    Optional<User> getUserById(Long id);

    User create(User user) throws AlreadyExistsEmailException;

    void delete(Long id);

    Optional<User> update(User user, Long id) throws AlreadyExistsEmailException;

    List<User> getUsersByIds(List<Long> idList);

}

