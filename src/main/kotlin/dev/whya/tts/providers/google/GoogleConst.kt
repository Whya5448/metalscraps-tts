package dev.whya.tts.providers.google

internal object GoogleConst {
    const val ID = "google"
    const val FRIENDLY_NAME = "구글"

    const val REQUEST_LINE = "POST /v1/text:synthesize"
    const val HOST = "https://texttospeech.googleapis.com"

    const val KEY_PROPERTY_NAME = "GOOGLE_TTS_KEY"
}