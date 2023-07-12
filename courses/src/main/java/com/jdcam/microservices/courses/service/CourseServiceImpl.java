package com.jdcam.microservices.courses.service;

import com.jdcam.microservices.courses.dto.UserDto;
import com.jdcam.microservices.courses.entity.Course;
import com.jdcam.microservices.courses.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService{

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> getCourses() {
        return (List<Course>) this.courseRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> getCourseById(Long id) {
        return this.courseRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<Course> update(Course course, Long id) {
        return this.getCourseById(id).map(courseFromDb -> {
            courseFromDb.setName(course.getName());
            return Optional.of(this.courseRepository.save(courseFromDb));
        }).orElse(Optional.empty());
    }

    @Override
    public Optional<UserDto> assignUser(UserDto userDto, Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<UserDto> createUser(UserDto userDto) {
        return Optional.empty();
    }

    @Override
    public Optional<UserDto> removeUserFromCourse(Long id, Long courseId) {
        return Optional.empty();
    }

    @Override
    @Transactional
    public Course save(Course course) {
        return this.courseRepository.save(course);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        this.courseRepository.deleteById(id);
    }
}
