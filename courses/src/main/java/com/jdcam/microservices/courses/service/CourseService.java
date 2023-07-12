package com.jdcam.microservices.courses.service;

import com.jdcam.microservices.courses.dto.UserDto;
import com.jdcam.microservices.courses.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    List<Course> getCourses();

    Optional<Course> getCourseById(Long id);

    Course save(Course course);

    void delete(Long id);

    Optional<Course> update(Course user, Long id);

    Optional<UserDto> assignUser(UserDto userDto, Long id);

    Optional<UserDto> createUser(UserDto userDto);

    Optional<UserDto> removeUserFromCourse(Long id, Long courseId);
}
