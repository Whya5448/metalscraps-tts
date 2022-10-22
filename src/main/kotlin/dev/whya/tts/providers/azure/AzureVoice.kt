package dev.whya.tts.providers.azure

import dev.whya.tts.core.Voice

enum class AzureVoice(
    override val id: String,
    override val friendlyName: String
) : Voice {
    Sun_Hi("ko-KR-SunHiNeural", "선희"),
    InJoon("ko-KR-InJoonNeural", "인준");

    override val provider: String = AzureConst.ID
}