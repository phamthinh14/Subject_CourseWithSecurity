package com.example.demo;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String subjectName;

    @OneToMany(mappedBy = "subject")
    public Set<Course> courses;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }


}
