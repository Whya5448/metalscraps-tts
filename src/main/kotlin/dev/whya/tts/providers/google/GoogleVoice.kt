package dev.whya.tts.providers.google

import dev.whya.tts.core.Voice

enum class GoogleVoice(
    override val id: String,
    override val friendlyName: String
) : Voice {
    ko_KR_Standard_A("ko-KR-Standard-A", "구글녀1"),
    ko_KR_Standard_B("ko-KR-Standard-B", "구글녀2"),
    ko_KR_Standard_C("ko-KR-Standard-C", "구글남1"),
    ko_KR_Standard_D("ko-KR-Standard-D", "구글남2");

    override val provider: String = GoogleConst.ID
}