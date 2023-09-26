package com.jdcam.microservices.users.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "courses-service")
public interface CourseClient {

    @DeleteMapping("/course/remove-user/{userId}")
    void removeUserFromCourse(@PathVariable long userId);

}
