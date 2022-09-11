package dev.whya.tts.core

interface Voice {
    val provider: String
    val id: String
    val friendlyName: String
}