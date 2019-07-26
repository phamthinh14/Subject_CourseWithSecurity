package com.example.demo;

import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, Long> {
    Iterable<Course> findAllByUsers(User user);

    Iterable<Subject> getAllBySubject(User user);
}
