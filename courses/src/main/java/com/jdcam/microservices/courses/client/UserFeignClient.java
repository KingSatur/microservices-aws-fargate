package com.jdcam.microservices.courses.client;

import com.jdcam.microservices.courses.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "users-svc")
public interface UserFeignClient {

    @GetMapping("/{id}")
    UserDto getUserById(@PathVariable Long id, @RequestHeader(value = "Authorization", required = true) String token);

    @PostMapping("/")
    UserDto createUser(@RequestBody UserDto userDto,
            @RequestHeader(value = "Authorization", required = true) String token);

    @GetMapping("/")
    List<UserDto> getAlumnsByCourse(@RequestParam Iterable<Long> ids,
            @RequestHeader(value = "Authorization", required = true) String token);
}
