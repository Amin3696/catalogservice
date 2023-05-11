package com.kotlinspring.service

import com.kotlinspring.dto.InstructorDto
import com.kotlinspring.entity.Instructor
import com.kotlinspring.repository.InstructorRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class InstructorService(val instructorRepository: InstructorRepository) {
    companion object : KLogging()

    fun addInstructor(instructorDto: InstructorDto): InstructorDto {
        val instructor = instructorDto.let { Instructor(null, it.name) }
        logger.info { "instructor is saved: $instructor" }

        instructorRepository.save(instructor)
        return instructor.let { InstructorDto(it.id, it.name) }
    }


}

