package io.satori.edu.faculty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/faculties")  // Updated to follow REST naming conventions
@CrossOrigin(origins = "*")  // Add CORS support if needed
public class FacultyController {

    private final FacultyService facultyService;

    @Autowired
    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping
    public ResponseEntity<List<Faculty>> getAllFaculties() {
        List<Faculty> faculties = facultyService.getFaculty();
        return ResponseEntity.ok(faculties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFacultyById(@PathVariable("id") Integer id) {
        Faculty faculty = facultyService.getOneFaculty(id);
        return ResponseEntity.ok(faculty);
    }

    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@Valid @RequestBody Faculty faculty) {
        facultyService.addFaculty(faculty);
        return new ResponseEntity<>(faculty, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Faculty> updateFaculty(
            @PathVariable("id") Integer id,
            @Valid @RequestBody Faculty faculty) {
        faculty.setId(id); // Ensure the ID matches the path variable
        facultyService.updateFaculty(faculty);
        return ResponseEntity.ok(faculty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable("id") Integer id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.noContent().build();
    }
}