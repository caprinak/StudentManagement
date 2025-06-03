package io.satori.edu.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses()
    {
        return ResponseEntity.ok(courseService.getAllCourse());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable("id") Integer id)
    {
        return ResponseEntity.ok(courseService.getOneCourse(id));
    }

    @PostMapping
    public ResponseEntity<Void> createCourse(
        @Valid @RequestBody Course course,
        @RequestParam Integer facultyId)
    {
        courseService.addCourse(course, facultyId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable("id") Integer id)
    {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Void> updateCourse(
        @PathVariable("id") Integer id,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) Integer facultyId)
    {
        courseService.updateCourse(id, name, facultyId);
        return ResponseEntity.ok().build();
    }

}
