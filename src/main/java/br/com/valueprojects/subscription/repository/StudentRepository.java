package br.com.valueprojects.subscription.repository;

import br.com.valueprojects.subscription.entity.Student;
import br.com.valueprojects.subscription.vo.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByName(String name);

    @Query("SELECT s FROM Student s WHERE s.plan.type = :planType")
    List<Student> findByPlan(@Param("planType") String planType);

    @Query("SELECT s FROM Student s WHERE s.credits >= :minCredits")
    List<Student> findByCreditsGreaterThanEqual(@Param("minCredits") Integer minCredits);

    @Query("SELECT s FROM Student s WHERE s.completedCourses > :minCourses")
    List<Student> findByCompletedCoursesGreaterThan(@Param("minCourses") Integer minCourses);
}


