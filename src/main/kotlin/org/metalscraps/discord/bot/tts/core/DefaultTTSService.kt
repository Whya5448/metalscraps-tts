package org.metalscraps.discord.bot.tts.core

class DefaultTTSService {
    private val providers: MutableMap<String, TTSProvider> = mutableMapOf()

    fun synthesize(text: String): Response {
        if (providers.isEmpty()) return Response.error("No provider supplies.")

        val (id, provider) = providers.entries.random()
        return synthesize(id, provider.getVoices().random().getId(), text);
    }

    fun synthesize(provider: String, voice: String, text: String): Response {
        var provider = provider

        if (providers.isEmpty()) return Response.error("No provider supplies.")
        if (text.isBlank()) return Response.error("Text must not be null or blank")
        if (provider.isBlank()) provider = providers.values.random().getId()

        val target = providers[provider] ?: return Response.error("No such provider: $provider")
        return target.synthesize(voice, text)
    }

    fun addProvider(api: TTSProvider) {
        addProvider(api.getId(), api)
    }

    fun addProvider(id: String, api: TTSProvider) {
        providers[id] = api
    }

    fun setProvider(providers: Map<String, TTSProvider>) {
        this.providers.clear()
        this.providers.putAll(providers)
    }

    fun getProviders(): Map<String, TTSProvider> {
        return providers.toMap()
    }
}