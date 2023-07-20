package vaoolo.savi.olorom.white.data.models

sealed class Message {
    abstract val id: String
}

data class UserMessage(
    override val id: String,
    val text: String
) : Message()

data class BotMessage(
    override val id: String,
    val text: String
) : Message()