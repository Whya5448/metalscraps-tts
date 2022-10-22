package dev.whya.tts.core

class DefaultTTSService {
    private val providers: MutableMap<String, Provider> = mutableMapOf()

    fun synthesize(provider: String, voice: String, text: String): Response {
        var provider = provider

        if (providers.isEmpty()) return Response.error("No provider supplies.")
        if (text.isBlank()) return Response.error("Text must not be null or blank")
        if (provider.isBlank()) provider = providers.values.random().id

        val target = providers[provider] ?: return Response.error("No such provider: $provider")
        return target.synthesize(voice, text)
    }

    fun addProvider(api: Provider) {
        addProvider(api.id, api)
    }

    fun addProvider(id: String, api: Provider) {
        providers[id] = api
    }

    fun setProvider(providers: Map<String, Provider>) {
        this.providers.clear()
        this.providers.putAll(providers)
    }

    fun getProviders(): Map<String, Provider> {
        return providers.toMap()
    }
}
