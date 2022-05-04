package org.metalscraps.discord.bot.tts

interface Voice {
    fun getId(): String

    fun getFriendlyName(): String

    fun getRandomVoice(): Voice

    fun getProvider(): String
}