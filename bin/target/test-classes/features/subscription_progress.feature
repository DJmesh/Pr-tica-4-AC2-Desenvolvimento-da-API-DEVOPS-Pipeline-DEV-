Feature: Progressão de assinatura por desempenho e contribuição

  Scenario: Ganhar 5 créditos com média >= 9.0
    Given um aluno BASIC chamado "Tales" com 0 cursos concluídos e 0 créditos
    When o aluno conclui 1 curso(s) com média 9.1
    Then o aluno deve possuir 1 curso concluído
    And o aluno deve possuir 5 créditos
    And o plano deve ser BASIC

  Scenario: Ganhar 3 créditos com 7.0 <= média < 9.0
    Given um aluno BASIC chamado "Tales" com 0 cursos concluídos e 0 créditos
    When o aluno conclui 1 curso(s) com média 7.3
    Then o aluno deve possuir 3 créditos

  Scenario: Converter moedas em créditos (2:1)
    Given um aluno BASIC chamado "Weber" com 0 cursos concluídos, 0 créditos e 4 moedas
    When o aluno converte 4 moedas em créditos
    Then o aluno deve possuir 2 créditos
    And o aluno deve possuir 0 moedas

  Scenario: Promoção para PREMIUM só quando > 12 cursos
    Given um aluno BASIC chamado "Lucas" com 12 cursos concluídos e 0 créditos
    When o aluno conclui 1 curso(s) com média 8.0
    Then o plano deve ser PREMIUM
