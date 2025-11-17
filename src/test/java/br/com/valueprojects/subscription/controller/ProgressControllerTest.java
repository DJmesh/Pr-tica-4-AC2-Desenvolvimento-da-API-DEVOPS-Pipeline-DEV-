package br.com.valueprojects.subscription.controller;

import br.com.valueprojects.subscription.dto.ConvertCoinsDTO;
import br.com.valueprojects.subscription.dto.FinishCourseDTO;
import br.com.valueprojects.subscription.dto.StudentDTO;
import br.com.valueprojects.subscription.service.ProgressService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProgressController.class)
class ProgressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProgressService progressService;

    @Autowired
    private ObjectMapper objectMapper;

    private StudentDTO studentDTO;

    @BeforeEach
    void setUp() {
        studentDTO = StudentDTO.builder()
                .id(1L)
                .name("Test Student")
                .plan("BASIC")
                .credits(0)
                .coins(0)
                .completedCourses(0)
                .build();
    }

    @Test
    void shouldFinishCourse() throws Exception {
        FinishCourseDTO finishCourseDTO = new FinishCourseDTO();
        finishCourseDTO.setStudentId(1L);
        finishCourseDTO.setCount(1);
        finishCourseDTO.setAverage(8.5);

        StudentDTO updatedStudent = StudentDTO.builder()
                .id(1L)
                .name("Test Student")
                .completedCourses(1)
                .credits(5)
                .plan("BASIC")
                .build();

        when(progressService.finishCourse(anyLong(), anyInt(), anyDouble())).thenReturn(updatedStudent);

        mockMvc.perform(post("/api/progress/finish-course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(finishCourseDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.completedCourses").value(1))
                .andExpect(jsonPath("$.credits").value(5));
    }

    @Test
    void shouldConvertCoins() throws Exception {
        ConvertCoinsDTO convertCoinsDTO = new ConvertCoinsDTO();
        convertCoinsDTO.setStudentId(1L);
        convertCoinsDTO.setCoinsToConvert(4);

        StudentDTO updatedStudent = StudentDTO.builder()
                .id(1L)
                .name("Test Student")
                .coins(0)
                .credits(2)
                .build();

        when(progressService.convertCoins(anyLong(), anyInt())).thenReturn(updatedStudent);

        mockMvc.perform(post("/api/progress/convert-coins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(convertCoinsDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.coins").value(0))
                .andExpect(jsonPath("$.credits").value(2));
    }

    @Test
    void shouldReturnBadRequestWhenFinishCourseWithInvalidData() throws Exception {
        FinishCourseDTO finishCourseDTO = new FinishCourseDTO();
        finishCourseDTO.setStudentId(null);
        finishCourseDTO.setCount(-1);
        finishCourseDTO.setAverage(-1.0);

        mockMvc.perform(post("/api/progress/finish-course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(finishCourseDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenConvertCoinsWithInvalidData() throws Exception {
        ConvertCoinsDTO convertCoinsDTO = new ConvertCoinsDTO();
        convertCoinsDTO.setStudentId(null);
        convertCoinsDTO.setCoinsToConvert(-1);

        mockMvc.perform(post("/api/progress/convert-coins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(convertCoinsDTO)))
                .andExpect(status().isBadRequest());
    }
}

