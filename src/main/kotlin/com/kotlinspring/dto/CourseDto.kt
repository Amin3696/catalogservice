package com.kotlinspring.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CourseDto(
    val id: Int?,
    @get:NotBlank(message = "courseDTO.name must not be blank")
    val name: String,
    @get:NotBlank(message = "courseDTO.category must not be blank")
    val category: String,
    @get:NotNull(message = "courseDTO.instructorId must not be blank")
    val instructorId: Int?
)
