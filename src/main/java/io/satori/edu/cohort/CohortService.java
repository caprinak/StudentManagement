package io.satori.edu.cohort;

import io.satori.edu.exception.APIEntityNotFoundException;
import io.satori.edu.exception.BadRequestException;
import io.satori.edu.faculty.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CohortService {

    private final CohortRepository cohortRepository;
    private final FacultyRepository facultyRepository;

    @Autowired
    public CohortService(CohortRepository cohortRepository, FacultyRepository facultyRepository) {
        this.cohortRepository = cohortRepository;
        this.facultyRepository = facultyRepository;
    }

    public List<Cohort> getAllCohort()
    {
        return cohortRepository.findAll();
    }

    public Cohort getOneCohort(Integer CohortId)
    {
        return cohortRepository.findById(CohortId).orElseThrow(()-> new APIEntityNotFoundException("Cohort with id "+CohortId+ " was not found"));
    }

    public void addCohort(Cohort Cohort,Integer facultyId)
    {
        Optional<Cohort> CohortByName = cohortRepository.findCohortByName(Cohort.getName());
        if(CohortByName.isPresent()) //if name of Cohort is present in database
        {
            throw new BadRequestException("Name already exist in database");
        }
        boolean exists = facultyRepository.existsById(facultyId);
        if(!exists)
        {
            throw new APIEntityNotFoundException("Faculty with id "+facultyId+ " was not found");
        }
        Cohort.setFaculty(facultyRepository.getById(facultyId));
        cohortRepository.save(Cohort);
        System.out.println(Cohort);
    }

    public void deleteCohort(Integer cohortId)
    {
        boolean exists = cohortRepository.existsById(cohortId);
        if(!exists)
        {
            throw new BadRequestException("Cohort with id = "+cohortId+ " does not exist in database");
        }
        System.out.println("Cohort id = "+cohortId+" has been delete");
        cohortRepository.deleteById(cohortId);
    }

    public void updateCohort(Integer CohortId, String name, Integer facultyId)
    {
        //Check studentId in database
        Cohort Cohort = cohortRepository.findById(CohortId).orElseThrow(() -> new APIEntityNotFoundException("Cohort by id "+CohortId+" was not found"));
        if(name != null && name.length() > 0 && !Objects.equals(Cohort.getName(),name)) //if the new name has been provided is not the same name in database
        {
            Optional<Cohort> CohortByName = cohortRepository.findCohortByName(name);
            if(CohortByName.isPresent()) //if new name of Cohort is present in database
            {
                throw new BadRequestException("Cohort "+name+" already exist in database");
            }
            Cohort.setName(name);
        }
        if(facultyId != null && !Objects.equals(Cohort.getFaculty().getId(), facultyId)) //if the new name has been provided is not the same name in database
        {
            boolean exists = facultyRepository.existsById(facultyId);
            if(!exists)
            {
                throw new BadRequestException("Faculty "+facultyId+" doesn't exist in database");
            }
            Cohort.setFaculty(facultyRepository.getById(facultyId));
        }
        cohortRepository.save(Cohort);
    }


}
