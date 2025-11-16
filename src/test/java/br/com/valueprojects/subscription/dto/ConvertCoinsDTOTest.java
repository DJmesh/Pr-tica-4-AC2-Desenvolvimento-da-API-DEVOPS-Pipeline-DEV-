package br.com.valueprojects.subscription.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConvertCoinsDTOTest {

    static class ConvertCoinsDTOFake extends ConvertCoinsDTO {
        @Override
        protected boolean canEqual(Object other) {
            return false;
        }
    }

    @Test
    void builderDeveMontarObjetoCorretamente() {
        ConvertCoinsDTO dto = ConvertCoinsDTO.builder()
                .studentId(1L)
                .coinsToConvert(100)
                .build();

        assertAll(
                () -> assertEquals(1L, dto.getStudentId()),
                () -> assertEquals(100, dto.getCoinsToConvert())
        );
    }

    @Test
    void construtorAllArgsEGettersSettersDevemFuncionar() {
        ConvertCoinsDTO dto = new ConvertCoinsDTO(2L, 50);

        assertAll(
                () -> assertEquals(2L, dto.getStudentId()),
                () -> assertEquals(50, dto.getCoinsToConvert())
        );

        dto.setCoinsToConvert(60);
        assertEquals(60, dto.getCoinsToConvert());
    }

    @Test
    void equalsHashCodeToStringECanEqualDevemCobrirRamos() {
        ConvertCoinsDTO base = ConvertCoinsDTO.builder()
                .studentId(10L)
                .coinsToConvert(20)
                .build();

        ConvertCoinsDTO igual = ConvertCoinsDTO.builder()
                .studentId(10L)
                .coinsToConvert(20)
                .build();

        ConvertCoinsDTO diferente = ConvertCoinsDTO.builder()
                .studentId(11L)
                .coinsToConvert(30)
                .build();

        // self
        assertEquals(base, base);

        // igual
        assertEquals(base, igual);
        assertEquals(base.hashCode(), igual.hashCode());

        // diferente
        assertNotEquals(base, diferente);

        // null e tipo diferente
        assertNotEquals(base, null);
        assertNotEquals(base, "x");

        // todos null
        ConvertCoinsDTO n1 = new ConvertCoinsDTO();
        ConvertCoinsDTO n2 = new ConvertCoinsDTO();
        assertEquals(n1, n2);
        n1.hashCode();

        // diferenças por campo: studentId, coinsToConvert
        ConvertCoinsDTO diffStudent1 = ConvertCoinsDTO.builder()
                .studentId(1L).coinsToConvert(10).build();
        ConvertCoinsDTO diffStudent2 = ConvertCoinsDTO.builder()
                .studentId(2L).coinsToConvert(10).build();
        assertNotEquals(diffStudent1, diffStudent2);

        ConvertCoinsDTO diffCoins1 = ConvertCoinsDTO.builder()
                .studentId(1L).coinsToConvert(10).build();
        ConvertCoinsDTO diffCoins2 = ConvertCoinsDTO.builder()
                .studentId(1L).coinsToConvert(20).build();
        assertNotEquals(diffCoins1, diffCoins2);

        // null vs não-null
        ConvertCoinsDTO thisCoinsNull = ConvertCoinsDTO.builder()
                .studentId(1L).coinsToConvert(null).build();
        ConvertCoinsDTO otherCoinsNaoNull = ConvertCoinsDTO.builder()
                .studentId(1L).coinsToConvert(10).build();
        assertNotEquals(thisCoinsNull, otherCoinsNaoNull);
        assertNotEquals(otherCoinsNaoNull, thisCoinsNull);

        // canEqual false
        ConvertCoinsDTOFake fake = new ConvertCoinsDTOFake();
        fake.setStudentId(base.getStudentId());
        fake.setCoinsToConvert(base.getCoinsToConvert());
        assertFalse(base.equals(fake));

        // toString
        String texto = base.toString();
        assertNotNull(texto);
        assertTrue(texto.contains("studentId=10"));
    }

    @Test
    void builderToStringNaoDeveSerNull() {
        ConvertCoinsDTO.ConvertCoinsDTOBuilder builder = ConvertCoinsDTO.builder()
                .studentId(1L)
                .coinsToConvert(40);

        String s = builder.toString();
        assertNotNull(s);

        ConvertCoinsDTO dto = builder.build();
        assertEquals(40, dto.getCoinsToConvert());
    }
}
