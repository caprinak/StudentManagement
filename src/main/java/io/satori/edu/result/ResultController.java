package io.satori.edu.result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/results")
public class ResultController {

    private final ResultService resultService;

    @Autowired
    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping
    public List<Result> getAllResult()
    {
        return resultService.getAllResult();
    }

    @GetMapping(path = "/{resultId}")
    public Result getOneResult(@PathVariable("resultId") Integer resultId)
    {
        return resultService.getOneResult(resultId);
    }

    @GetMapping(path = "/grade/{grade}")
    public List<Result> getResultByGradeGreaterThanEqual(@PathVariable("grade") Integer grade)
    {
        return resultService.getResultByGradeGreaterThanEqual(grade);
    }

    @PostMapping
    public void addResult(
        @RequestBody Result result,
        @RequestParam Integer studentId,
        @RequestParam Integer courseId)
    {
        resultService.addResult(result, studentId, courseId);
    }
    @DeleteMapping(path = "/student/{studentId}/course/{courseId}")
    public void deleteResult(@PathVariable("studentId") Integer studentId,
                                @PathVariable("courseId") Integer courseId)
    {
        resultService.deleteResult(studentId,courseId);
    }

    @PutMapping(path = "/student/{studentId}/course/{courseId}") //Update result
    public void updateResult(@PathVariable("studentId") Integer studentId,
                                @PathVariable("courseId") Integer courseId,
                                @RequestParam(required = false) Integer grade)
    {
        resultService.updateResult(studentId,courseId,grade);
    }
}
