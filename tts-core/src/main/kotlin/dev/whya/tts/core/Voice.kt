package dev.whya.tts.core

interface Voice {
    val id: String

    val friendlyName: String

    val provider: String
}