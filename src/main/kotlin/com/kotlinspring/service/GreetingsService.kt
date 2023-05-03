package com.kotlinspring.service

import org.springframework.stereotype.Service

@Service
class GreetingsService {

    fun retriveGreetings(name: String) = "Hello ${name}"

}