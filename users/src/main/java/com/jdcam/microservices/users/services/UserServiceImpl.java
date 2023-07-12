package com.jdcam.microservices.users.services;

import com.jdcam.microservices.users.entity.User;
import com.jdcam.microservices.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return (List<User>) this.userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> getUserById(Long id) {
        return this.userRepository.findById(id);
    }

    @Override
    @Transactional
    public User create(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public Optional<User> update(User user, Long id) {
        return this.getUserById(id).map(userFromDb -> {
            userFromDb.setEmail(user.getEmail());
            userFromDb.setName(user.getName());
            return Optional.of(this.userRepository.save(userFromDb));
        }).orElse(Optional.empty());
    }
}
