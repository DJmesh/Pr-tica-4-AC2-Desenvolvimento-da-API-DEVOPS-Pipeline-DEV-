package br.com.valueprojects.subscription.service;

import br.com.valueprojects.subscription.dto.StudentDTO;
import br.com.valueprojects.subscription.repository.StudentRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StudentServiceExtraTest {

    @Test
    void updateStudent_whenStudentNotFound_shouldThrow() {
        StudentRepository repo = mock(StudentRepository.class);
        when(repo.findById(999L)).thenReturn(Optional.empty());

        StudentService service = new StudentService(repo);

        StudentDTO dto = new StudentDTO();
        dto.setName("Nobody");

        assertThrows(RuntimeException.class, () -> service.updateStudent(999L, dto));
    }
}
