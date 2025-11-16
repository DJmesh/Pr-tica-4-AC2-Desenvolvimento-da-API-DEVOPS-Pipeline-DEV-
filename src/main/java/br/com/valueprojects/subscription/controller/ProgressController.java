package br.com.valueprojects.subscription.controller;

import br.com.valueprojects.subscription.dto.ConvertCoinsDTO;
import br.com.valueprojects.subscription.dto.FinishCourseDTO;
import br.com.valueprojects.subscription.dto.StudentDTO;
import br.com.valueprojects.subscription.service.ProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/progress")
@CrossOrigin(origins = "*")
@Tag(name = "Progress", description = "API para gerenciamento de progresso do estudante")
public class ProgressController {

    private final ProgressService progressService;

    @Autowired
    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @PostMapping("/finish-course")
    @Operation(summary = "Finaliza curso(s)", 
               description = "Registra a conclusão de curso(s) e atualiza créditos e plano conforme a média.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Progresso atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Estudante não encontrado")
    })
    public ResponseEntity<StudentDTO> finishCourse(@Valid @RequestBody FinishCourseDTO finishCourseDTO) {
        StudentDTO updated = progressService.finishCourse(
                finishCourseDTO.getStudentId(),
                finishCourseDTO.getCount(),
                finishCourseDTO.getAverage()
        );
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/convert-coins")
    @Operation(summary = "Converte moedas em créditos", 
               description = "Converte moedas em créditos na proporção 2:1 (2 moedas = 1 crédito)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Moedas convertidas com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Estudante não encontrado")
    })
    public ResponseEntity<StudentDTO> convertCoins(@Valid @RequestBody ConvertCoinsDTO convertCoinsDTO) {
        StudentDTO updated = progressService.convertCoins(
                convertCoinsDTO.getStudentId(),
                convertCoinsDTO.getCoinsToConvert()
        );
        return ResponseEntity.ok(updated);
    }
}



