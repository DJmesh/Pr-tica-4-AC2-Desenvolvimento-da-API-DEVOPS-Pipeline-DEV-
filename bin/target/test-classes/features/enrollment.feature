Feature: Matrícula em curso usando créditos ou voucher

  Scenario: Matricular consumindo 1 crédito
    Given um aluno BASIC chamado "Ana" com 0 cursos concluídos e 2 créditos
    When o aluno se matricula no curso "ML-101" sem voucher
    Then a matrícula deve ser aceita com o código "ML-101"
    And o aluno deve possuir 1 crédito

  Scenario: Impedir matrícula sem créditos nem voucher
    Given um aluno BASIC chamado "Bruno" com 0 cursos concluídos e 0 créditos
    When o aluno tenta se matricular no curso "DS-201" sem voucher
    Then a matrícula deve ser rejeitada com o motivo "INSUFFICIENT_CREDIT_OR_VOUCHER"

  Scenario: Voucher não consome crédito
    Given um aluno BASIC chamado "Aluno" com 0 cursos concluídos e 2 créditos
    When o aluno se matricula no curso "ML-101" usando voucher
    Then a matrícula deve ser aceita com o código "ML-101"
    And o aluno deve possuir 2 créditos
