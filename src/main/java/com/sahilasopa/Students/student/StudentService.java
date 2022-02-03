package com.sahilasopa.Students.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping()
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
        if (studentOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        if (studentRepository.findById(id).isPresent()) {
            studentRepository.deleteById(id);
        }
        throw new IllegalStateException("Invalid id");
    }

    @Transactional
    public void updateStudent(long studentId, String name, String email) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isPresent()) {
            if (name != null && !name.isEmpty()) {
                student.get().setName(name);
            }
            if (email != null && !email.isEmpty()) {
                if (studentRepository.findStudentByEmail(email).isPresent()) {
                    throw new IllegalStateException("invalid email");
                }
                student.get().setEmail(email);
            }
        }
    }
}
