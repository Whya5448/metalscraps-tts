package dev.whya.tts.core

interface Provider {
    val id: String
    val friendlyName: String
    val voices: List<Voice>

    fun synthesize(voice: String, text: String): Response

    fun synthesize(text: String): Response {
        return synthesize(voices.random().id, text)
    }

}