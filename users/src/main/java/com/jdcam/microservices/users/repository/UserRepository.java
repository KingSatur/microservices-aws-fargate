package com.jdcam.microservices.users.repository;

import com.jdcam.microservices.users.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
