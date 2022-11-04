package com.splendo.kaluga.example

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform