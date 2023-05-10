package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDto
import com.kotlinspring.entity.Course
import com.kotlinspring.repository.CourseRepository
import com.kotlinspring.util.courseEntityList
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
internal class CourseControllerIntgTest {

    @Autowired(required = true)
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var courseRepository: CourseRepository

    @BeforeEach
    fun setUp() {
        courseRepository.deleteAll()
        val courses = courseEntityList()
        courseRepository.saveAll(courses)
    }

    @Test
    fun addCourse() {
        val courseDto = CourseDto(
            null, "Build Restfull Api using springboot and kotlin",
            "AminSh"
        )

        val savedCurseDto = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDto)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDto::class.java)
            .returnResult()
            .responseBody

        assertTrue {
            savedCurseDto!!.id != null
        }
    }

    @Test
    fun updateCourse() {
        //existing Course
        val course = Course(
            null, "Build Restfull Api using springboot and kotlin",
            "development"
        )
        courseRepository.save(course)

        //courseId
        //update to courseDto
        val updateCourseDto = CourseDto(
            null, "Build Restfull Api using springboot and kotlin-part 1",
            "development"
        )

        val updatedCurse = webTestClient
            .put()
            .uri("/v1/courses/{course_id}", course.id)
            .bodyValue(updateCourseDto)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDto::class.java)
            .returnResult()
            .responseBody

        assertEquals("Build Restfull Api using springboot and kotlin-part 1", updatedCurse!!.name)
    }

    @Test
    fun getAllCourses() {
        val courseDtos = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDto::class.java)
            .returnResult()
            .responseBody
        println("coureses: $courseDtos")
        assertEquals(3, courseDtos!!.size)
    }

    @Test
    fun deleteCourse() {
        //existing Course
        val course = Course(
            null, "Build Restfull Api using springboot and kotlin",
            "development"
        )
        courseRepository.save(course)

        val deletedCurse = webTestClient
            .delete()
            .uri("/v1/courses/{course_id}", course.id)
            .exchange()
            .expectStatus().isNoContent
    }

}
