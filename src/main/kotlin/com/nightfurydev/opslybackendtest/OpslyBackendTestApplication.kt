package com.nightfurydev.opslybackendtest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux

@EnableWebFlux
@EnableConfigurationProperties(SocialNetworkUrls::class)
@SpringBootApplication
class OpslyBackendTestApplication

fun main(args: Array<String>) {
	runApplication<OpslyBackendTestApplication>(*args)
}
