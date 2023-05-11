package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDto
import com.kotlinspring.entity.Course
import com.kotlinspring.service.CourseService
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.reactive.server.WebTestClient
import util.courseDTO
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.expectBody


@WebMvcTest(CourseController::class)
@AutoConfigureWebTestClient
class CourseControllerUnitTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockBean
    lateinit var courseServiceMock: CourseService


    @Test
    fun addCourse() {
        //given
        val courseDTO = courseDTO()

        every { courseServiceMock.addCourse(courseDTO) } returns courseDTO(id = 1)

        //when
        val savedCourseDTO = webTestClient
            .post()
            .uri("/v1/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDto::class.java)
            .returnResult()
            .responseBody

        //then
        Assertions.assertTrue {
            savedCourseDTO!!.id != null
        }
    }

    @Test
    fun addCourse_validation() {
        //given
        val courseDTO = courseDTO(null, "", "")

        every { courseServiceMock.addCourse(any()) } returns courseDTO(id = 1)

        //when
        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals("courseDTO.category must not be blank, courseDTO.name must not be blank", response)
    }

    @Test
    fun addCourse_runtimeException() {
        //given
        val courseDto = CourseDto(
            null, "Build Restfull Api using springboot and kotlin",
            "dev"
        )
        val errorMessage = "Unexpected Error occurred"
        every { courseServiceMock.addCourse(any()) } throws RuntimeException()

        //when
        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(courseDto)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals("Unexpected Error occurred", response)
    }

    @Test
    fun getAllCourses() {
        every { courseServiceMock.getAllCoursed() }.returnsMany(
            listOf(courseDTO( 1,"testName","testCategory"), courseDTO( 2, "course2","testCategory"))
        )
        val courseDtos = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDto::class.java)
            .returnResult()
            .responseBody
        println("coureses: $courseDtos")
        assertEquals(2, courseDtos!!.size)
    }

    @Test
    fun updateCourse() {
        //given
        every { courseServiceMock.updateCourse(any(), any()) } returns courseDTO(
            100,
            "Build Restfull Api using springboot and kotlin"
        )

        val updateCourseDto = CourseDto(
            null, "Build Restfull Api using springboot and kotlin-part 1",
            "development"
        )
        //when
        val updatedCurse = webTestClient
            .put()
            .uri("/v1/courses/{course_id}", 100)
            .bodyValue(updateCourseDto)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDto::class.java)
            .returnResult()
            .responseBody

        assertEquals("Build Restfull Api using springboot and kotlin-part 1", updatedCurse!!.name)
    }
}