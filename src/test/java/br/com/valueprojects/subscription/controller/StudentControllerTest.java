package br.com.valueprojects.subscription.controller;

import br.com.valueprojects.subscription.dto.StudentDTO;
import br.com.valueprojects.subscription.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private StudentDTO studentDTO;

    @BeforeEach
    void setUp() {
        studentDTO = StudentDTO.builder()
                .id(1L)
                .name("Test Student")
                .plan("BASIC")
                .completedCourses(5)
                .credits(10)
                .coins(20)
                .build();
    }

    @Test
    void testGetAllStudents() throws Exception {
        List<StudentDTO> students = Arrays.asList(studentDTO);
        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Student"));

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    void testGetStudentById() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(studentDTO);

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Student"));

        verify(studentService, times(1)).getStudentById(1L);
    }

    @Test
    void testCreateStudent() throws Exception {
        StudentDTO newDTO = StudentDTO.builder()
                .name("New Student")
                .plan("BASIC")
                .completedCourses(0)
                .credits(0)
                .coins(0)
                .build();

        StudentDTO createdDTO = StudentDTO.builder()
                .id(2L)
                .name("New Student")
                .plan("BASIC")
                .completedCourses(0)
                .credits(0)
                .coins(0)
                .build();

        when(studentService.createStudent(any(StudentDTO.class))).thenReturn(createdDTO);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Student"));

        verify(studentService, times(1)).createStudent(any(StudentDTO.class));
    }

    @Test
    void testUpdateStudent() throws Exception {
        StudentDTO updateDTO = StudentDTO.builder()
                .name("Updated Student")
                .plan("PREMIUM")
                .completedCourses(10)
                .credits(20)
                .coins(30)
                .build();

        when(studentService.updateStudent(eq(1L), any(StudentDTO.class))).thenReturn(updateDTO);

        mockMvc.perform(put("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Student"));

        verify(studentService, times(1)).updateStudent(eq(1L), any(StudentDTO.class));
    }

    @Test
    void testDeleteStudent() throws Exception {
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isNoContent());

        verify(studentService, times(1)).deleteStudent(1L);
    }
}



