package dev.whya.tts.providers.aws

import dev.whya.tts.core.Voice

enum class AwsVoice(
    override val id: String,
    override val friendlyName: String
) : Voice {
    Seoyeon("Seoyeon", "서연");

    override val provider: String = AwsConst.ID
}