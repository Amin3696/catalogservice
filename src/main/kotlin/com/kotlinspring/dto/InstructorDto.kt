package com.kotlinspring.dto

import jakarta.validation.constraints.NotBlank

data class InstructorDto(
    val id: Int?,
    @get:NotBlank(message="InstructorDTO.name must not be blank")
    val name: String,
)
