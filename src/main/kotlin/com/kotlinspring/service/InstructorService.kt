package com.kotlinspring.service

import com.kotlinspring.dto.InstructorDto
import com.kotlinspring.entity.Instructor
import com.kotlinspring.repository.InstructorRepository
import mu.KLogging
import org.springframework.stereotype.Service
import java.util.*

@Service
class InstructorService(val instructorRepository: InstructorRepository) {
    companion object : KLogging()

    fun addInstructor(instructorDto: InstructorDto): InstructorDto {
        val instructor = instructorDto.let { Instructor(it.id, it.name) }

        instructorRepository.save(instructor)

        logger.info { "instructor is saved: $instructor" }
        return instructor.let { InstructorDto(it.id, it.name) }
    }

    fun findInstructorById(instructorId: Int): Optional<Instructor> {
        return instructorRepository.findById(instructorId)
    }


}

