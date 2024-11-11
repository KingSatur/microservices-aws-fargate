package com.jdcam.microservices.courses.entity;

import com.jdcam.microservices.courses.dto.UserDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@Setter
@Getter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "course_id")
    private List<CourseUser> courseUsers;

    @Transient
    private List<UserDto> users;

    @NotEmpty
    private String name;

    public Course(){
        this.courseUsers = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public void addCourseUser(CourseUser courseUser){
        this.courseUsers.add(courseUser);
    }

    public void removeCourseUser(CourseUser courseUser){
        this.courseUsers.remove(courseUser);
    }
}
