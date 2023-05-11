package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDto
import com.kotlinspring.entity.Course
import com.kotlinspring.service.CourseService
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.reactive.server.WebTestClient
import util.courseDTO
import org.springframework.http.MediaType


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
    fun getAllCourses() {
        every { courseServiceMock.getAllCoursed() }.returnsMany(
            listOf(courseDTO(id = 1), courseDTO(id = 2, name = "course2"))
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
        Assertions.assertEquals(2, courseDtos!!.size)
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

        Assertions.assertEquals("Build Restfull Api using springboot and kotlin-part 1", updatedCurse!!.name)
    }
}