/*
 * You can use the following import statements
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.ArrayList;
 *
 */

// Write your code here
package com.example.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
import com.example.school.repository.StudentRepository;
import com.example.school.model.Student;
import com.example.school.model.StudentRowMapper;

@Service
public class StudentH2Service implements StudentRepository {

  @Autowired
  private JdbcTemplate db;

  @Override
  public ArrayList<Student> getStudents() {
    List<Student> studentList = db.query("select * from Student", new StudentRowMapper());
    ArrayList<Student> students = new ArrayList<>(studentList);
    return students;
  }

  @Override
  public Student getStudentById(int studentId) {
    try {
      Student student = db.queryForObject("select *from Student where studentId = ?", new StudentRowMapper(),
          studentId);
      return student;
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public Student addStudent(Student student) {
    db.update("insert into Student(studentName, gender, standard) values(?,?,?)", new StudentRowMapper(),
        student.getStudentName(), student.getGender(), student.getStandard());
    Student addNewStudent = db.queryForObject("select *from Student where studentName =? and gender =? and standard =?", new StudentRowMapper(),
        student.getStudentName(), student.getGender(),student.getStandard());
    return addNewStudent;
  }

  @Override
  public Student updateStudent(int studentId, Student student) {
    if (student.getStudentName() != null) {
      db.update("update Student set studentName = ?, where studentId = ?", new StudentRowMapper(),
          student.getStudentName());
    }
    if (student.getGender() != null) {
      db.update("update Student set gender = ?, where studentId = ?", new StudentRowMapper(), student.getGender());
    }

    if (student.getStandard() != 0) {
      db.update("update Student set standard = ?, where studentId =?", new StudentRowMapper(), student.getStandard());
    }
    return getStudentById(studentId);

  }

  @Override
  public void deleteStudent(int studentId) {
    db.update("delete from Student where studentId =?", new StudentRowMapper(), studentId);
  }

}
