package com.jdcam.microservices.users.services;

import com.jdcam.microservices.users.entity.User;
import com.jdcam.microservices.users.exception.AlreadyExistsEmailException;
import com.jdcam.microservices.users.proxy.CourseClient;
import com.jdcam.microservices.users.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CourseClient courseClient;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, CourseClient userClient,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.courseClient = userClient;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
    public User create(User user) throws AlreadyExistsEmailException {
        Optional<User> userWithEmail = this.userRepository.findByEmail(user.getEmail());
        if (userWithEmail.isPresent()) {
            throw new AlreadyExistsEmailException("Email already registered");
        }
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }


    @Override
    @Transactional
    public void delete(Long id) {
        this.userRepository.deleteById(id);
        this.courseClient.removeUserFromCourse(id);
    }

    @Override
    @Transactional
    public Optional<User> update(User user, Long id) throws AlreadyExistsEmailException {
        Optional<User> userWithEmail = this.userRepository.findByEmail(user.getEmail());
        if (userWithEmail.isPresent()) {
            throw new AlreadyExistsEmailException("Email already registered");
        }
        return this.getUserById(id).map(userFromDb -> {
            userFromDb.setEmail(user.getEmail());
            userFromDb.setName(user.getName());
            userFromDb.setPassword(this.bCryptPasswordEncoder.encode(userFromDb.getPassword()));
            return Optional.of(this.userRepository.save(userFromDb));
        }).orElse(Optional.empty());
    }

    @Override
    public List<User> getUsersByIds(List<Long> idList) {
        return (List<User>) this.userRepository.findAllById(idList);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

}
