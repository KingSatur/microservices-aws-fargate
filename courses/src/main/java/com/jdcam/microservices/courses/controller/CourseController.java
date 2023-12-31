package com.jdcam.microservices.courses.controller;

import com.jdcam.microservices.courses.dto.UserDto;
import com.jdcam.microservices.courses.entity.Course;
import com.jdcam.microservices.courses.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/")
    public List<Course> getCourses(){
        return this.courseService.getCourses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable("id") Long id){
        return this.courseService.getCourseById(id).map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    public ResponseEntity<?> createCourse(@Valid @RequestBody Course course, BindingResult bindingResult){
        if(bindingResult.hasErrors()) return handleValidationsErrors(bindingResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.courseService.save(course));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCourse(@PathVariable("id") Long id){
        return this.courseService.getCourseById(id).map(userToDelete -> {
            this.courseService.delete(id);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    private static ResponseEntity<Map<String, String>> handleValidationsErrors(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }


    @PutMapping("/{id}")
    public ResponseEntity updateCourse(@Valid @RequestBody Course course, BindingResult bindingResult,
                                       @PathVariable("id") Long id){
        if(bindingResult.hasErrors()) return handleValidationsErrors(bindingResult);
        return this.courseService.update(course, id)
                .map(userUpdate -> ResponseEntity.ok(userUpdate))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{courseId}/assign/{userId}")
    public ResponseEntity assignUser(@PathVariable("courseId") Long id, @PathVariable("userId") Long userId){
        return this.courseService.assignUser(userId, id)
                .map(userDto1 -> ResponseEntity.status(HttpStatus.CREATED).body(userDto1))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{courseId}/create-user/")
    public ResponseEntity createUser(@PathVariable("courseId") Long id, @RequestBody UserDto userDto){
        return this.courseService.createUser(userDto, id)
                .map(userDto1 -> ResponseEntity.status(HttpStatus.CREATED).body(userDto1))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{courseId}/remove-user/{userId}")
    public ResponseEntity createUser(@PathVariable Long courseId, @PathVariable Long userId){
        return this.courseService.removeUserFromCourse(userId, courseId)
                .map(userDto1 -> ResponseEntity.status(HttpStatus.CREATED).body(userDto1))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/remove-user/{userId}")
    public ResponseEntity removeUser(@PathVariable long userId){
        this.courseService.removeAlumnFromCourse(userId);
        return ResponseEntity.noContent().build();
    }
}
