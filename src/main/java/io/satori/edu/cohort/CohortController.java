package io.satori.edu.cohort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;




    @RestController
    @RequestMapping(path = "/api/v1/cohorts")
    public class CohortController {

        private final CohortService cohortService;

        @Autowired
        public CohortController(CohortService cohortService) {
            this.cohortService = cohortService;
        }

        @GetMapping
        public ResponseEntity<List<Cohort>> getAllCohorts()
        {
            return ResponseEntity.ok(cohortService.getAllCohort());
        }

        @GetMapping(path = "/{id}")
        public ResponseEntity<Cohort> getCohortById(@PathVariable("id") Integer id)
        {
            return ResponseEntity.ok(cohortService.getOneCohort(id));
        }

        @PostMapping
    public ResponseEntity<Void> createCohort(
        @Valid @RequestBody Cohort cohort, 
        @RequestParam Integer facultyId)
    {
        cohortService.addCohort(cohort, facultyId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteCohort(@PathVariable("id") Integer id)
    {
        cohortService.deleteCohort(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Void> updateCohort(
        @PathVariable("id") Integer id,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) Integer facultyId)
    {
        cohortService.updateCohort(id, name, facultyId);
        return ResponseEntity.ok().build();
    }

}
