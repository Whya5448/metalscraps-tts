package org.metalscraps.discord.bot.tts.core

interface TTSProvider {
    fun synthesize(text: String): Response {
        return synthesize(getVoices().random().getId(), text)
    }

    fun synthesize(voice: String, text: String): Response

    fun getVoices(): List<Voice>

    fun getId(): String

    fun getFriendlyName(): String
}