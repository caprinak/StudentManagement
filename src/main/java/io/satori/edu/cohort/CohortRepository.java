package io.satori.edu.cohort;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;




@Repository
public interface CohortRepository extends JpaRepository<Cohort,Integer> { //Spring Data JPA

    @Query("SELECT c FROM Cohort c WHERE c.name = ?1")
    Optional<Cohort> findCohortByName(String name);

}
