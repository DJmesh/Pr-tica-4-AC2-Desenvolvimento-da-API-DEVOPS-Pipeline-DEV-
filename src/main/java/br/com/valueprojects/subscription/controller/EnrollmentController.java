package br.com.valueprojects.subscription.controller;

import br.com.valueprojects.subscription.dto.EnrollmentDTO;
import br.com.valueprojects.subscription.dto.EnrollmentResultDTO;
import br.com.valueprojects.subscription.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "*")
@Tag(name = "Enrollments", description = "API para gerenciamento de matrículas")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    @Operation(summary = "Realiza matrícula em um curso", 
               description = "Matricula um estudante em um curso. Pode usar voucher ou créditos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Matrícula processada (aceita ou rejeitada)"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Estudante não encontrado")
    })
    public ResponseEntity<EnrollmentResultDTO> enroll(@Valid @RequestBody EnrollmentDTO enrollmentDTO) {
        EnrollmentResultDTO result = enrollmentService.enroll(
                enrollmentDTO.getStudentId(),
                enrollmentDTO.getCourseCode(),
                enrollmentDTO.getUsingVoucher() != null ? enrollmentDTO.getUsingVoucher() : false
        );
        
        HttpStatus status = result.getAccepted() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(result);
    }
}



