package com.javatechie.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatechie.dto.*;
import com.javatechie.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.List;

@RestController
@RequestMapping("/courses")
//@Slf4j
public class CourseController {

    Logger logger= LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;


    @PostMapping
    public ServiceResponse<CourseResponseDTO> addCourse(@RequestBody @Valid CourseRequestDTO course) {
        logger.info("CourseController::addCourse request payload {} ",printRequestResponse(course));
        ServiceResponse<CourseResponseDTO> courseResponse = new ServiceResponse<>();
        CourseResponseDTO responseDTO = courseService.onboardNewCourse(course);
        courseResponse.setHttpStatus(HttpStatus.CREATED);
        courseResponse.setResponse(responseDTO);
        logger.info("CourseController::addCourse response  {} ",printRequestResponse(responseDTO));
        return courseResponse;
    }

    @GetMapping
    public ServiceResponse<List<CourseResponseDTO>> findAllCourse() {
        logger.info("CourseController::findAllCourse get all courses");
        ServiceResponse<List<CourseResponseDTO>> courseResponse = new ServiceResponse<>();
        List<CourseResponseDTO> courseResponseDTOS = courseService.viewAllCourses();
        courseResponse.setHttpStatus(HttpStatus.OK);
        courseResponse.setResponse(courseResponseDTOS);
        logger.info("CourseController::findAllCourse response {}",printRequestResponse(courseResponse));
        return courseResponse;
    }

    @GetMapping("/search/path/{courseId}")
    public ServiceResponse<CourseResponseDTO> findCourse(@PathVariable int courseId) {
        logger.info("CourseController::findCourse get course by id {}",courseId);
        ServiceResponse<CourseResponseDTO> courseResponse = new ServiceResponse<>();
        CourseResponseDTO course = courseService.findCourseById(courseId);
        courseResponse.setHttpStatus(HttpStatus.OK);
        courseResponse.setResponse(course);
        logger.info("CourseController::findCourse response {}",printRequestResponse(courseResponse));
        return courseResponse;
    }

    @GetMapping("/search/request")
    public ServiceResponse<CourseResponseDTO> findCourseWithRequestParm(@RequestParam(required = false) Integer courseId) {
        ServiceResponse<CourseResponseDTO> courseResponse = new ServiceResponse<>();
        logger.info("CourseController::findCourseWithRequestParm get course by id {}",courseId);
        CourseResponseDTO course = courseService.findCourseById(courseId);
        courseResponse.setHttpStatus(HttpStatus.OK);
        courseResponse.setResponse(course);
        logger.info("CourseController::findCourseWithRequestParm response {}",printRequestResponse(courseResponse));
        return courseResponse;
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable int courseId) {
        logger.info("CourseController::deleteCourse by id {}",courseId);
        courseService.deleteCourse(courseId);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{courseId}")
    public ServiceResponse<CourseResponseDTO> updateCourse(@PathVariable int courseId, @RequestBody CourseRequestDTO course) {
        ServiceResponse<CourseResponseDTO> courseResponse = new ServiceResponse<>();
        logger.info("CourseController::updateCourse request payload {} & {}",printRequestResponse(course),courseId);
        CourseResponseDTO courseResponseDTO = courseService.updateCourse(course, courseId);
        courseResponse.setHttpStatus(HttpStatus.OK);
        courseResponse.setResponse(courseResponseDTO);
        logger.info("CourseController::updateCourse response  {} ",printRequestResponse(courseResponse));
        return courseResponse;
    }

    @GetMapping("/log")
    public String logLevel(){
        logger.trace("A TRACE Message");
        logger.debug("A DEBUG Message");
        logger.info("An INFO Message");
        logger.warn("A WARN Message");
        logger.error("An ERROR Message");
        return "Different logging level -> check in console";
    }

    private String printRequestResponse(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
