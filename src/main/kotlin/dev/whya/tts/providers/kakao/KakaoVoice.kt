package dev.whya.tts.providers.kakao

import dev.whya.tts.core.Voice

enum class KakaoVoice(
    override val id: String,
    override val friendlyName: String
) : Voice {
    WOMAN_READ_CALM("WOMAN_READ_CALM", "카카오녀차분"),
    MAN_READ_CALM("MAN_READ_CALM", "카카오남차분"),
    WOMAN_DIALOG_BRIGHT("WOMAN_DIALOG_BRIGHT", "카카오녀활발"),
    MAN_DIALOG_BRIGHT("MAN_DIALOG_BRIGHT", "카카오남활발");

    override val provider: String = KakaoConst.ID

    enum class Prosody {
        FAST, SLOW
    }
}