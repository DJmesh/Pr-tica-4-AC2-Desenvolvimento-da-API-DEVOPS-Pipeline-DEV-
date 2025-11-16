package br.com.valueprojects.subscription.controller;

import br.com.valueprojects.subscription.dto.EnrollmentDTO;
import br.com.valueprojects.subscription.dto.EnrollmentResultDTO;
import br.com.valueprojects.subscription.service.EnrollmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnrollmentController.class)
class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnrollmentService enrollmentService;

    @Autowired
    private ObjectMapper objectMapper;

    private EnrollmentDTO enrollmentDTO;

    @BeforeEach
    void setUp() {
        enrollmentDTO = EnrollmentDTO.builder()
                .studentId(1L)
                .courseCode("ML-101")
                .usingVoucher(false)
                .build();
    }

    @Test
    void testEnrollSuccess() throws Exception {
        EnrollmentResultDTO result = EnrollmentResultDTO.accepted("ML-101");
        when(enrollmentService.enroll(anyLong(), anyString(), anyBoolean())).thenReturn(result);

        mockMvc.perform(post("/api/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enrollmentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accepted").value(true))
                .andExpect(jsonPath("$.code").value("ML-101"));

        verify(enrollmentService, times(1)).enroll(anyLong(), anyString(), anyBoolean());
    }

    @Test
    void testEnrollRejected() throws Exception {
        EnrollmentResultDTO result = EnrollmentResultDTO.rejected("INSUFFICIENT_CREDIT_OR_VOUCHER");
        when(enrollmentService.enroll(anyLong(), anyString(), anyBoolean())).thenReturn(result);

        mockMvc.perform(post("/api/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enrollmentDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.accepted").value(false))
                .andExpect(jsonPath("$.reason").value("INSUFFICIENT_CREDIT_OR_VOUCHER"));
    }

    @Test
    void testEnrollWithVoucher() throws Exception {
        enrollmentDTO.setUsingVoucher(true);
        EnrollmentResultDTO result = EnrollmentResultDTO.accepted("ML-101");
        when(enrollmentService.enroll(anyLong(), anyString(), eq(true))).thenReturn(result);

        mockMvc.perform(post("/api/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enrollmentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accepted").value(true));
    }
}



