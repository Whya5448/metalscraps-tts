package dev.whya.tts.core

data class Response private constructor(
    val error: Boolean = false,
    val errorMessage: String = "",
    val format: AudioFormat = AudioFormat.NONE,
    val bytes: ByteArray = byteArrayOf()
) {
    companion object {
        fun error(message: String): Response {
            return Response(error = true, errorMessage = message)
        }

        fun data(format: AudioFormat, bytes: ByteArray): Response {
            return Response(format = format, bytes = bytes)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Response

        if (error != other.error) return false
        if (errorMessage != other.errorMessage) return false
        if (format != other.format) return false
        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = error.hashCode()
        result = 31 * result + errorMessage.hashCode()
        result = 31 * result + format.hashCode()
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}