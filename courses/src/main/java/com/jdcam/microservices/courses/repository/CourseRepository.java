package com.jdcam.microservices.courses.repository;

import com.jdcam.microservices.courses.entity.Course;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, Long> {
}
