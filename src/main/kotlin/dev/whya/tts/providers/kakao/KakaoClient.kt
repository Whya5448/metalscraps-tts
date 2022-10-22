package dev.whya.tts.providers.kakao

import feign.Headers
import feign.Param
import feign.RequestLine
import feign.Response
import dev.whya.tts.providers.SSMLRequest


// https://developers.kakao.com/assets/guide/kakao_ssml_guide.pdf
internal interface KakaoClient {
    @RequestLine(KakaoConst.REQUEST_LINE)
    @Headers("Content-Type: application/xml", "Authorization: KakaoAK {appKey}")
    fun synthesize(
        @Param("appKey") appKey: String,
        ssmlRequest: SSMLRequest
    ): Response
}