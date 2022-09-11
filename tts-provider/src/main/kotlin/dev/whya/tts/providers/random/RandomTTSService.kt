package dev.whya.tts.providers.random

import dev.whya.tts.core.Response
import dev.whya.tts.core.Provider
import dev.whya.tts.core.Voice

class RandomTTSService(private val proxy: Collection<Provider>) : Provider {
    override val id: String = "random"
    override val friendlyName: String = "랜덤"

    object RandomVoice : Voice {
        override val provider: String = "random"
        override val id: String = "random"
        override val friendlyName: String = "랜덤"
    }

    override val voices: List<Voice> = listOf(RandomVoice)

    override fun synthesize(voice: String, text: String): Response {
        return proxy.random().let {
            it.synthesize(it.voices.random().id, text)
        }
    }

}