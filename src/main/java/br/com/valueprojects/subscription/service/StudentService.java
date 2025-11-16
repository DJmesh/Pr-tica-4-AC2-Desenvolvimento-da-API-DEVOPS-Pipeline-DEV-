package br.com.valueprojects.subscription.service;

import br.com.valueprojects.subscription.dto.StudentDTO;
import br.com.valueprojects.subscription.entity.Student;
import br.com.valueprojects.subscription.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional(readOnly = true)
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(StudentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudante não encontrado com ID: " + id));
        return StudentDTO.fromEntity(student);
    }

    @Transactional
    public StudentDTO createStudent(StudentDTO studentDTO) {
        Student student = studentDTO.toEntity();
        student = studentRepository.save(student);
        return StudentDTO.fromEntity(student);
    }

    @Transactional
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudante não encontrado com ID: " + id));

        student.setName(studentDTO.getName());
        if (studentDTO.getPlan() != null) {
            br.com.valueprojects.subscription.vo.Plan plan = "PREMIUM".equalsIgnoreCase(studentDTO.getPlan()) 
                    ? br.com.valueprojects.subscription.vo.Plan.PREMIUM 
                    : br.com.valueprojects.subscription.vo.Plan.BASIC;
            student.setPlan(plan);
        }
        if (studentDTO.getCompletedCourses() != null) {
            student.setCompletedCourses(studentDTO.getCompletedCourses());
        }
        if (studentDTO.getCredits() != null) {
            student.setCredits(studentDTO.getCredits());
        }
        if (studentDTO.getCoins() != null) {
            student.setCoins(studentDTO.getCoins());
        }

        student = studentRepository.save(student);
        return StudentDTO.fromEntity(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Estudante não encontrado com ID: " + id);
        }
        studentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Student findStudentEntityById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudante não encontrado com ID: " + id));
    }
}



