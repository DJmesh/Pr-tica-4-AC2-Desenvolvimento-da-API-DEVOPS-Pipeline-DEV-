package br.com.valueprojects.subscription.entity;

import br.com.valueprojects.subscription.vo.Plan;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    @Builder.Default
    private Plan plan = Plan.BASIC;

    @Column(nullable = false)
    @Builder.Default
    private Integer completedCourses = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer credits = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer coins = 0;

    // Business methods
    public void addCredits(int delta) {
        this.credits += delta;
    }

    public void consumeCredit() {
        if (this.credits > 0) {
            this.credits -= 1;
        }
    }

    public void addCompletedCourses(int count) {
        this.completedCourses += count;
    }

    // Constructor for backward compatibility
    public Student(String name) {
        this.name = name;
        this.plan = Plan.BASIC;
        this.completedCourses = 0;
        this.credits = 0;
        this.coins = 0;
    }
}



