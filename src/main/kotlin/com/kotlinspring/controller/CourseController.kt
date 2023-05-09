package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDto
import com.kotlinspring.service.CourseService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/courses")
class CourseController(val courseService: CourseService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addCourse(@RequestBody courseDto: CourseDto): CourseDto {
        return courseService.addCourse(courseDto)
    }

    @GetMapping
    fun getAllCourses(): List<CourseDto> = courseService.getAllCoursed()


    @PutMapping("/{course_id}")
    fun updateCourse(@RequestBody courseDto: CourseDto,
                     @PathVariable("course_id") courseId: Int) = courseService.updateCourse(courseId,courseDto)
}