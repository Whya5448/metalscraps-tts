package org.metalscraps.discord.tts.core

interface Voice {
    fun getId(): String

    fun getFriendlyName(): String

    fun getRandomVoice(): Voice

    fun getProvider(): String
}