package com.example.demo;

import org.springframework.data.repository.CrudRepository;

public interface SubjectRepository extends CrudRepository<Subject, Long> {

    Subject findBysubjectName(String subject_name);

//    Iterable<Course> findAllByUsers(User user);


}
