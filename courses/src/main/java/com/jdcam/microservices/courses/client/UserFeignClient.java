package com.jdcam.microservices.courses.client;

import com.jdcam.microservices.courses.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user-service")
public interface UserFeignClient {

    @GetMapping("/user/{id}")
    UserDto getUserById(@PathVariable Long id);

    @PostMapping("/user")
    UserDto createUser(@RequestBody UserDto userDto);

    @GetMapping("/user")
    List<UserDto> getAlumnsByCourse(@RequestParam Iterable<Long> ids);
}
