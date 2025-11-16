Feature: Bônus do grupo - BDD por integrante

  Scenario: Voucher não consome crédito (Eduardo Prestes)
    Given um aluno BASIC chamado "Eduardo Prestes" com 0 cursos concluídos e 2 créditos
    When o aluno se matricula no curso "ML-101" usando voucher
    Then a matrícula deve ser aceita com o código "ML-101"
    And o aluno deve possuir 2 créditos

  Scenario: Converter 4 moedas em 2 créditos (Weber)
    Given um aluno BASIC chamado "Weber" com 0 cursos concluídos, 0 créditos e 4 moedas
    When o aluno converte 4 moedas em créditos
    Then o aluno deve possuir 2 créditos
    And o aluno deve possuir 0 moedas

  Scenario: Promoção para PREMIUM quando ultrapassa 12 cursos (Lucas)
    Given um aluno BASIC chamado "Lucas" com 12 cursos concluídos e 0 créditos
    When o aluno conclui 1 curso(s) com média 8.0
    Then o plano deve ser PREMIUM

  Scenario: Ganhar 5 créditos com média >= 9.0 (Tales)
    Given um aluno BASIC chamado "Tales" com 0 cursos concluídos e 0 créditos
    When o aluno conclui 1 curso(s) com média 9.1
    Then o aluno deve possuir 1 curso concluído
    And o aluno deve possuir 5 créditos
    And o plano deve ser BASIC
