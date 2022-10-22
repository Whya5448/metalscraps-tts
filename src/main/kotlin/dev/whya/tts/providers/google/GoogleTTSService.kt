package dev.whya.tts.providers.google

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import feign.Feign
import feign.jackson.JacksonDecoder
import feign.jackson.JacksonEncoder
import feign.slf4j.Slf4jLogger
import dev.whya.tts.core.AudioFormat
import dev.whya.tts.core.Response
import dev.whya.tts.core.Provider
import dev.whya.tts.core.Voice
import dev.whya.tts.providers.Const
import dev.whya.tts.providers.createResponseError
import dev.whya.tts.providers.getProperty
import org.slf4j.LoggerFactory
import java.util.*

class GoogleTTSService(
    apiKey: String? = getProperty(GoogleConst.KEY_PROPERTY_NAME)
) : Provider {

    override val id: String = GoogleConst.ID
    override val friendlyName: String = GoogleConst.FRIENDLY_NAME
    override val voices: List<Voice> = enumValues<GoogleVoice>().asList()

    private val client: GoogleClient
    private val apiKey: String

    init {
        require(!apiKey.isNullOrBlank())
        this.apiKey = apiKey

        this.client = Feign.builder()
            .logger(Slf4jLogger())
            .logLevel(Const.LOG_LEVEL)
            .encoder(JacksonEncoder())
            .decoder(JacksonDecoder())
            .target(GoogleClient::class.java, GoogleConst.HOST)
    }

    override fun synthesize(voice: String, text: String): Response {
        val response = client.synthesize(GoogleApiKey(apiKey), GoogleRequest(voice, text))
        val status = response.status()

        if (status != 200) {
            return createResponseError(response)
        }

        val responseJson = response.body().asInputStream().readAllBytes().decodeToString()

        val googleResponse = jacksonObjectMapper().readValue(responseJson, GoogleResponse::class.java)

        if (googleResponse.audioContent.isNullOrBlank()) {
            return createResponseError(response, "$googleResponse")
        }

        return googleResponse.audioContent.run {
            Response.data(
                AudioFormat.OGG,
                Base64.getDecoder().decode(this)
            )
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}