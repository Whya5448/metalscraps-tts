package dev.whya.tts.providers.kakao

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

class KakaoTTSService(
    appKey: String? = getProperty(KakaoConst.KEY_PROPERTY_NAME)
) : Provider {
    override val id: String = KakaoConst.ID
    override val friendlyName: String = KakaoConst.FRIENDLY_NAME
    override val voices: List<Voice> = enumValues<KakaoVoice>().asList()

    private val client: KakaoClient
    private val appKey: String

    init {
        require(!appKey.isNullOrBlank())
        this.appKey = appKey

        val jaxbFactory = JAXBContextFactory.Builder()
            .withMarshallerJAXBEncoding("UTF-8")
            .withMarshallerSchemaLocation(Const.SSML_SCHEMA)
            .build()

        this.client = Feign.builder()
            .logger(Slf4jLogger())
            .logLevel(Const.LOG_LEVEL)
            .encoder(JAXBEncoder(jaxbFactory))
            .target(KakaoClient::class.java, KakaoConst.HOST)
    }

    override fun synthesize(voice: String, text: String): Response {
        val response = client.synthesize(appKey, SSMLRequest(voice, text))
        val status = response.status()

        if (status == 204) {
            return Response.data(AudioFormat.NONE, byteArrayOf())
        }

        if (status != 200) {
            return createResponseError(response)
        }

        val responseBody: ByteArray = try {
            response.body().asInputStream().readBytes()
        } catch (e: Exception) {
            return createResponseError(response, e.message.toString(), e.stackTraceToString())
        }

        return Response.data(AudioFormat.MP3, responseBody)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}