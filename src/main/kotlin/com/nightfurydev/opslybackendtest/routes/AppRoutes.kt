package com.nightfurydev.opslybackendtest.routes

import com.nightfurydev.opslybackendtest.handler.Handler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class AppRoutes(val handler: Handler) {

    @Bean
    fun routes() = router {
        GET("/", handler::getAllMessages)
    }

}