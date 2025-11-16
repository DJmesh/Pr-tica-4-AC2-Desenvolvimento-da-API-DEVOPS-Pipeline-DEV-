package br.com.valueprojects.subscription.dto;

import br.com.valueprojects.subscription.entity.Student;
import br.com.valueprojects.subscription.vo.Plan;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentDTOTest {

    /**
     * Subclasse para exercitar o ramo !other.canEqual(this) no equals.
     */
    static class StudentDTOFake extends StudentDTO {
        @Override
        protected boolean canEqual(Object other) {
            return false;
        }
    }

    // ---------- fromEntity ----------

    @Test
    void fromEntityDeveRetornarNullQuandoStudentForNull() {
        StudentDTO dto = StudentDTO.fromEntity(null);
        assertNull(dto);
    }

    @Test
    void fromEntityDeveMapearTodosOsCamposQuandoPlanNaoEhNull() {
        Student student = Student.builder()
                .id(1L)
                .name("João Silva")
                .plan(Plan.PREMIUM)
                .completedCourses(10)
                .credits(5)
                .coins(20)
                .build();

        StudentDTO dto = StudentDTO.fromEntity(student);

        assertAll(
                () -> assertEquals(1L, dto.getId()),
                () -> assertEquals("João Silva", dto.getName()),
                () -> assertEquals("PREMIUM", dto.getPlan()),
                () -> assertEquals(10, dto.getCompletedCourses()),
                () -> assertEquals(5, dto.getCredits()),
                () -> assertEquals(20, dto.getCoins())
        );
    }

    @Test
    void fromEntityDeveRetornarPlanNullQuandoStudentPlanForNull() {
        Student student = Student.builder()
                .id(2L)
                .name("Maria")
                .plan(null) // cobre o ramo do ternário
                .completedCourses(3)
                .credits(2)
                .coins(1)
                .build();

        StudentDTO dto = StudentDTO.fromEntity(student);

        assertAll(
                () -> assertEquals(2L, dto.getId()),
                () -> assertEquals("Maria", dto.getName()),
                () -> assertNull(dto.getPlan()),
                () -> assertEquals(3, dto.getCompletedCourses()),
                () -> assertEquals(2, dto.getCredits()),
                () -> assertEquals(1, dto.getCoins())
        );
    }

    // ---------- toEntity ----------

    @Test
    void toEntityDeveUsarBasicQuandoPlanForNullEValoresNullDevemVirarZero() {
        StudentDTO dto = StudentDTO.builder()
                .id(3L)
                .name("Jose")
                .plan(null)
                .completedCourses(null)
                .credits(null)
                .coins(null)
                .build();

        Student entity = dto.toEntity();

        assertAll(
                () -> assertEquals(3L, entity.getId()),
                () -> assertEquals("Jose", entity.getName()),
                () -> assertEquals(Plan.BASIC, entity.getPlan()),
                () -> assertEquals(0, entity.getCompletedCourses()),
                () -> assertEquals(0, entity.getCredits()),
                () -> assertEquals(0, entity.getCoins())
        );
    }

    @Test
    void toEntityDeveMapearPlanoPremiumEValoresPreenchidos() {
        StudentDTO dto = StudentDTO.builder()
                .id(4L)
                .name("Carlos")
                .plan("PREMIUM")
                .completedCourses(7)
                .credits(3)
                .coins(15)
                .build();

        Student entity = dto.toEntity();

        assertAll(
                () -> assertEquals(4L, entity.getId()),
                () -> assertEquals("Carlos", entity.getName()),
                () -> assertEquals(Plan.PREMIUM, entity.getPlan()),
                () -> assertEquals(7, entity.getCompletedCourses()),
                () -> assertEquals(3, entity.getCredits()),
                () -> assertEquals(15, entity.getCoins())
        );
    }

    // ---------- construtor all-args + getters/setters ----------

    @Test
    void construtorAllArgsEGettersDevemFuncionar() {
        StudentDTO dto = new StudentDTO(
                5L,
                "Ana",
                "BASIC",
                2,
                1,
                9
        );

        assertAll(
                () -> assertEquals(5L, dto.getId()),
                () -> assertEquals("Ana", dto.getName()),
                () -> assertEquals("BASIC", dto.getPlan()),
                () -> assertEquals(2, dto.getCompletedCourses()),
                () -> assertEquals(1, dto.getCredits()),
                () -> assertEquals(9, dto.getCoins())
        );

        // exercita setter explicitamente
        dto.setName("Ana Maria");
        assertEquals("Ana Maria", dto.getName());
    }

    // ---------- equals / hashCode / toString / canEqual ----------

    @Test
    void equalsHashCodeEToStringDevemCobrirTodosOsCaminhosPrincipais() {
        StudentDTO base = StudentDTO.builder()
                .id(10L)
                .name("João")
                .plan("BASIC")
                .completedCourses(5)
                .credits(2)
                .coins(10)
                .build();

        StudentDTO igual = StudentDTO.builder()
                .id(10L)
                .name("João")
                .plan("BASIC")
                .completedCourses(5)
                .credits(2)
                .coins(10)
                .build();

        StudentDTO diferente = StudentDTO.builder()
                .id(11L)
                .name("Maria")
                .plan("PREMIUM")
                .completedCourses(1)
                .credits(1)
                .coins(1)
                .build();

        StudentDTO comNomeNull = StudentDTO.builder()
                .id(10L)
                .name(null)
                .plan("BASIC")
                .completedCourses(5)
                .credits(2)
                .coins(10)
                .build();

        // self equals (ramo o == this)
        assertEquals(base, base);

        // equals true + hashCode igual
        assertEquals(base, igual);
        assertEquals(base.hashCode(), igual.hashCode());

        // equals false com objeto diferente
        assertNotEquals(base, diferente);

        // equals false com null
        assertNotEquals(base, null);

        // equals false com tipo diferente
        assertNotEquals(base, "string qualquer");

        // equals false quando um campo é null e o outro não (nome)
        assertNotEquals(base, comNomeNull);
        assertNotEquals(comNomeNull, base);

        // objetos com todos os campos null (cobre null-null em todos os campos)
        StudentDTO nulo1 = new StudentDTO();
        StudentDTO nulo2 = new StudentDTO();
        assertEquals(nulo1, nulo2);
        nulo1.hashCode(); // hashCode com campos null

        // diffs por campo, respeitando ordem dos campos em equals
        StudentDTO diffId1 = StudentDTO.builder().id(1L).name("X").build();
        StudentDTO diffId2 = StudentDTO.builder().id(2L).name("X").build();
        assertNotEquals(diffId1, diffId2);

        StudentDTO diffName1 = StudentDTO.builder().id(1L).name("A").build();
        StudentDTO diffName2 = StudentDTO.builder().id(1L).name("B").build();
        assertNotEquals(diffName1, diffName2);

        StudentDTO diffPlan1 = StudentDTO.builder()
                .id(1L).name("A").plan("BASIC").build();
        StudentDTO diffPlan2 = StudentDTO.builder()
                .id(1L).name("A").plan("PREMIUM").build();
        assertNotEquals(diffPlan1, diffPlan2);

        StudentDTO diffCompleted1 = StudentDTO.builder()
                .id(1L).name("A").plan("BASIC").completedCourses(1).build();
        StudentDTO diffCompleted2 = StudentDTO.builder()
                .id(1L).name("A").plan("BASIC").completedCourses(2).build();
        assertNotEquals(diffCompleted1, diffCompleted2);

        StudentDTO diffCredits1 = StudentDTO.builder()
                .id(1L).name("A").plan("BASIC").completedCourses(1).credits(1).build();
        StudentDTO diffCredits2 = StudentDTO.builder()
                .id(1L).name("A").plan("BASIC").completedCourses(1).credits(2).build();
        assertNotEquals(diffCredits1, diffCredits2);

        StudentDTO diffCoins1 = StudentDTO.builder()
                .id(1L).name("A").plan("BASIC")
                .completedCourses(1).credits(1).coins(1).build();
        StudentDTO diffCoins2 = StudentDTO.builder()
                .id(1L).name("A").plan("BASIC")
                .completedCourses(1).credits(1).coins(2).build();
        assertNotEquals(diffCoins1, diffCoins2);

        // canEqual retornando false via subclasse
        StudentDTOFake fake = new StudentDTOFake();
        fake.setId(base.getId());
        fake.setName(base.getName());
        fake.setPlan(base.getPlan());
        fake.setCompletedCourses(base.getCompletedCourses());
        fake.setCredits(base.getCredits());
        fake.setCoins(base.getCoins());
        assertFalse(base.equals(fake));

        // toString deve conter informações relevantes
        String texto = base.toString();
        assertNotNull(texto);
        assertTrue(texto.contains("João"));
        assertTrue(texto.contains("BASIC"));
        assertTrue(texto.contains("id=10"));
    }

    /**
     * Exercita explicitamente todos os casos "this.campo == null && other.campo != null"
     * para cada um dos campos, que são os ramos que costumam faltar no Jacoco.
     */
    @Test
    void equalsDeveCobrirNullVsNaoNullEmTodosOsCampos() {
        // id: this.id null, other.id != null
        StudentDTO idNull = new StudentDTO(); // tudo null
        StudentDTO idValor = StudentDTO.builder()
                .id(99L)
                .build();
        assertNotEquals(idNull, idValor);

        // plan: this.plan null, other.plan != null
        StudentDTO planNull = StudentDTO.builder()
                .id(1L)
                .name("A")
                .plan(null)
                .build();
        StudentDTO planValor = StudentDTO.builder()
                .id(1L)
                .name("A")
                .plan("BASIC")
                .build();
        assertNotEquals(planNull, planValor);

        // completedCourses: this null, other != null
        StudentDTO completedNull = StudentDTO.builder()
                .id(1L)
                .name("A")
                .plan("BASIC")
                .completedCourses(null)
                .build();
        StudentDTO completedValor = StudentDTO.builder()
                .id(1L)
                .name("A")
                .plan("BASIC")
                .completedCourses(1)
                .build();
        assertNotEquals(completedNull, completedValor);

        // credits: this null, other != null
        StudentDTO creditsNull = StudentDTO.builder()
                .id(1L)
                .name("A")
                .plan("BASIC")
                .completedCourses(1)
                .credits(null)
                .build();
        StudentDTO creditsValor = StudentDTO.builder()
                .id(1L)
                .name("A")
                .plan("BASIC")
                .completedCourses(1)
                .credits(1)
                .build();
        assertNotEquals(creditsNull, creditsValor);

        // coins: this null, other != null
        StudentDTO coinsNull = StudentDTO.builder()
                .id(1L)
                .name("A")
                .plan("BASIC")
                .completedCourses(1)
                .credits(1)
                .coins(null)
                .build();
        StudentDTO coinsValor = StudentDTO.builder()
                .id(1L)
                .name("A")
                .plan("BASIC")
                .completedCourses(1)
                .credits(1)
                .coins(1)
                .build();
        assertNotEquals(coinsNull, coinsValor);
    }

    // ---------- Builder interno (StudentDTO.StudentDTOBuilder) ----------

    @Test
    void builderToStringNaoDeveSerNull() {
        StudentDTO.StudentDTOBuilder builder = StudentDTO.builder()
                .id(1L)
                .name("Teste")
                .plan("BASIC")
                .completedCourses(1)
                .credits(1)
                .coins(1);

        String textoBuilder = builder.toString();
        assertNotNull(textoBuilder);

        StudentDTO dto = builder.build();
        assertEquals(1L, dto.getId());
        assertEquals("Teste", dto.getName());
    }
}
