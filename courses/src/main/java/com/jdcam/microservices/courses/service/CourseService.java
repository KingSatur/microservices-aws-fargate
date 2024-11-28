package com.jdcam.microservices.courses.service;

import com.jdcam.microservices.courses.dto.UserDto;
import com.jdcam.microservices.courses.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    List<Course> getCourses();

    Optional<Course> getCourseById(Long id, String token);

    Course save(Course course);

    void delete(Long id);

    Optional<Course> update(Course user, Long id, String token);

    Optional<UserDto> assignUser(Long userId, Long id, String token);

    Optional<UserDto> createUser(UserDto userDto, Long courseId, String token);

    Optional<UserDto> removeUserFromCourse(Long userId, Long courseId, String token);

    void removeAlumnFromCourse(Long userId);
}
