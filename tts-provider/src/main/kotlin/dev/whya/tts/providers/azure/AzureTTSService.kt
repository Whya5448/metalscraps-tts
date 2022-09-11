package dev.whya.tts.providers.azure

import feign.Feign
import feign.jaxb.JAXBContextFactory
import feign.jaxb.JAXBEncoder
import feign.slf4j.Slf4jLogger
import dev.whya.tts.core.AudioFormat
import dev.whya.tts.core.Response
import dev.whya.tts.core.Provider
import dev.whya.tts.core.Voice
import dev.whya.tts.providers.Const
import dev.whya.tts.providers.SSMLRequest
import dev.whya.tts.providers.createResponseError
import dev.whya.tts.providers.getProperty
import org.slf4j.LoggerFactory

class AzureTTSService(
    subscriptionKey: String? = getProperty(AzureConst.KEY_PROPERTY_NAME),
) : Provider {
    override val id: String = AzureConst.ID
    override val friendlyName: String = AzureConst.FRENDLY_NAME
    override val voices: List<Voice> = enumValues<AzureVoice>().asList()

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    private val client: AzureClient
    private val subscriptionKey: String

    init {
        require(!subscriptionKey.isNullOrBlank())
        this.subscriptionKey = subscriptionKey

        val jaxbFactory = JAXBContextFactory.Builder()
            .withMarshallerJAXBEncoding("UTF-8")
            .withMarshallerSchemaLocation(Const.SSML_SCHEMA)
            .build()

        this.client = Feign.builder()
            .logger(Slf4jLogger())
            .logLevel(Const.LOG_LEVEL)
            .encoder(JAXBEncoder(jaxbFactory))
            .target(AzureClient::class.java, AzureConst.HOST)
    }

    override fun synthesize(voice: String, text: String): Response {
        val response = client.synthesize(subscriptionKey, SSMLRequest(voice, text))
        val status = response.status()

        if (status != 200) {
            return createResponseError(response)
        }

        val responseBody: ByteArray = try {
            response.body().asInputStream().readBytes()
        } catch (e: Exception) {
            return createResponseError(response, e.message.toString(), e.stackTraceToString())
        }

        return Response.data(AudioFormat.OGG, responseBody)
    }
}