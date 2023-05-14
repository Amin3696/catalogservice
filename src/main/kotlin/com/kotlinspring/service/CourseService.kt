package com.kotlinspring.service

import com.kotlinspring.dto.CourseDto
import com.kotlinspring.entity.Course
import com.kotlinspring.exception.CourseNotFoundException
import com.kotlinspring.exception.InstructorNotValidException
import com.kotlinspring.repository.CourseRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class CourseService(val courseRepository: CourseRepository, val instructorService: InstructorService) {
    companion object : KLogging()

    fun addCourse(courseDto: CourseDto): CourseDto {
        val instructor = instructorService.findInstructorById(courseDto.instructorId!!)
        if (!instructor.isPresent) {
            throw InstructorNotValidException("Instructor id is not valid!")
        }
        val course = courseDto.let { Course(null, it.name, it.category, instructor.get()) }

        courseRepository.save(course)
        logger.info { "course is saved: $course" }

        return course.let { CourseDto(it.id, it.name, it.category, it.instructor?.id) }

    }

    fun getAllCoursed(): List<CourseDto> {
        return courseRepository.findAll()
            .map { CourseDto(it.id, it.name, it.category, it.instructor?.id) }
    }

    fun updateCourse(courseId: Int, courseDto: CourseDto): CourseDto {

        val existingCourse = courseRepository.findById(courseId)

        return if (existingCourse.isPresent) {
            existingCourse.get()
                .let {
                    it.name = courseDto.name
                    it.category = courseDto.category
                    courseRepository.save(it)
                    logger.info { "course is updated: $it" }
                    CourseDto(it.id, it.name, it.category, it.instructor?.id)
                }
        } else {
            throw CourseNotFoundException("no course found with given id: $courseId")
        }
    }

    fun deleteCourse(courseId: Int) {
        val courseToDelete = courseRepository.findById(courseId)

        if (courseToDelete.isPresent) {
            courseRepository.deleteById(courseId)
            logger.info { "course with courseId $courseId is delete!" }

        } else {
            throw CourseNotFoundException("no course found with given id: $courseId")
        }
    }
}