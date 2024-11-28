package com.jdcam.microservices.courses.service;

import com.jdcam.microservices.courses.client.UserFeignClient;
import com.jdcam.microservices.courses.dto.UserDto;
import com.jdcam.microservices.courses.entity.Course;
import com.jdcam.microservices.courses.entity.CourseUser;
import com.jdcam.microservices.courses.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService{

    private final CourseRepository courseRepository;
    private final UserFeignClient userFeignClient;

    public CourseServiceImpl(CourseRepository courseRepository, UserFeignClient userFeignClient) {
        this.courseRepository = courseRepository;
        this.userFeignClient = userFeignClient;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> getCourses() {
        return (List<Course>) this.courseRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> getCourseById(Long id, String token) {
        return this.courseRepository.findById(id).map(course -> {
            if(course.getCourseUsers().size() > 0){
                List<Long> alumnsIds = course.getCourseUsers().stream()
                        .map(courseUser -> courseUser.getUserId()).collect(Collectors.toList());
                List<UserDto> alumns =  this.userFeignClient.getAlumnsByCourse(alumnsIds, token);
                course.setUsers(alumns);
            }
            return course;
        });
    }

    @Override
    @Transactional
    public Optional<Course> update(Course course, Long id, String token) {
        return this.getCourseById(id, token).map(courseFromDb -> {
            courseFromDb.setName(course.getName());
            return Optional.of(this.courseRepository.save(courseFromDb));
        }).orElse(Optional.empty());
    }

    @Override
    @Transactional
    public Optional<UserDto> assignUser(Long userId, Long id, String token) {
        return this.courseRepository.findById(id).map(course -> {
            UserDto userFromService = this.userFeignClient.getUserById(userId, token);
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(userFromService.getId());
            course.addCourseUser(courseUser);
            this.courseRepository.save(course);
            return Optional.of(userFromService);
        }).orElse(Optional.empty());
    }

    @Override
    @Transactional
    public Optional<UserDto> createUser(UserDto userDto, Long courseId, String token) {
        return this.courseRepository.findById(courseId).map(course -> {
            UserDto userCreatedFromService = this.userFeignClient.createUser(userDto, token);
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(userCreatedFromService.getId());
            course.addCourseUser(courseUser);
            this.courseRepository.save(course);
            return Optional.of(userCreatedFromService);
        }).orElse(Optional.empty());
    }

    @Override
    @Transactional
    public Optional<UserDto> removeUserFromCourse(Long userId, Long courseId, String token) {
        return this.courseRepository.findById(courseId).map(course -> {
            UserDto userData = this.userFeignClient.getUserById(userId, token);
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(userData.getId());
            course.removeCourseUser(courseUser);
            this.courseRepository.save(course);
            return Optional.of(userData);
        }).orElse(Optional.empty());
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

    @Override
    @Transactional
    public void removeAlumnFromCourse(Long userId){
        this.courseRepository.deleteAlumnFromCourse(userId);
    }
}
