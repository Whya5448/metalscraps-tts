package dev.whya.tts.providers.aws

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

class AwsTTSService(
    accessKeyId: String? = getProperty(AwsConst.KEY_PROPERTY_NAME),
    secretAccessKey: String? = getProperty(AwsConst.SECRET_PROPERTY_NAME)
) : Provider {
    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    override val id: String = AwsConst.ID
    override val friendlyName: String = AwsConst.FRIENDLY_NAME
    override val voices: List<Voice> = enumValues<AwsVoice>().asList()

    private val client: AwsClient
    private val accessKeyId: String
    private val secretAccessKey: String

    init {
        require(!accessKeyId.isNullOrBlank())
        require(!secretAccessKey.isNullOrBlank())
        this.accessKeyId = accessKeyId
        this.secretAccessKey = secretAccessKey

        this.client = Feign.builder()
            .logger(Slf4jLogger())
            .logLevel(Const.LOG_LEVEL)
            .requestInterceptor(AwsRequestSigner(accessKeyId = accessKeyId, secretAccessKey = secretAccessKey))
            .encoder(JacksonEncoder())
            .decoder(JacksonDecoder())
            .target(AwsClient::class.java, AwsConst.HOST)
    }

    override fun synthesize(voice: String, text: String): Response {
        val response = client.synthesize(AwsRequest(voice, text))
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