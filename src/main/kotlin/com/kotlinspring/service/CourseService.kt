package com.kotlinspring.service

import com.kotlinspring.dto.CourseDto
import com.kotlinspring.entity.Course
import com.kotlinspring.exception.CourseNotFoundException
import com.kotlinspring.repository.CourseRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class CourseService(val courseRepository: CourseRepository) {
    companion object : KLogging()

    fun addCourse(courseDto: CourseDto): CourseDto {
        val courseEntity = courseDto.let { Course(null, it.name, it.category) }

        courseRepository.save(courseEntity)
        logger.info { "course is saved: $courseEntity" }

        return courseEntity.let { CourseDto(it.id, it.name, it.category) }

    }

    fun getAllCoursed(): List<CourseDto> {
        return courseRepository.findAll()
            .map { CourseDto(it.id, it.name, it.category) }
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
                    CourseDto(it.id, it.name, it.category)
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