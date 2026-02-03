package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.interfaces.helper.ImplicitMediaData
import eu.vendeli.tgbot.types.component.ParseMode
import eu.vendeli.tgbot.types.component.ImplicitFile
import eu.vendeli.tgbot.types.msg.MessageEntity
import eu.vendeli.tgbot.utils.serde.DurationSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.time.Duration

/**
 * Custom serializer for InputMedia that handles the "type" discriminator properly.
 * This avoids the conflict between the "type" property and JSON class discriminator.
 */
@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
object InputMediaSerializer : KSerializer<InputMedia> {
    override val descriptor: SerialDescriptor = buildSerialDescriptor(
        "eu.vendeli.tgbot.types.media.InputMedia",
        PolymorphicKind.SEALED
    )

    override fun serialize(encoder: Encoder, value: InputMedia) {
        when (value) {
            is InputMedia.Audio -> encoder.encodeSerializableValue(InputMedia.Audio.serializer(), value)
            is InputMedia.Document -> encoder.encodeSerializableValue(InputMedia.Document.serializer(), value)
            is InputMedia.Photo -> encoder.encodeSerializableValue(InputMedia.Photo.serializer(), value)
            is InputMedia.Video -> encoder.encodeSerializableValue(InputMedia.Video.serializer(), value)
            is InputMedia.Animation -> encoder.encodeSerializableValue(InputMedia.Animation.serializer(), value)
        }
    }

    override fun deserialize(decoder: Decoder): InputMedia {
        throw UnsupportedOperationException("InputMedia deserialization is not supported")
    }
}

/**
 * This object represents the content of a media message to be sent. It should be one of
 * - InputMediaAnimation
 * - InputMediaDocument
 * - InputMediaAudio
 * - InputMediaPhoto
 * - InputMediaVideo
 *
 * [Api reference](https://core.telegram.org/bots/api#inputmedia)
 *
 */
@Serializable(with = InputMediaSerializer::class)
@Suppress("OVERRIDE_DEPRECATION")
sealed class InputMedia : ImplicitMediaData {
    abstract val type: String

    @Serializable
    data class Audio(
        override var media: ImplicitFile,
        override var thumbnail: ImplicitFile? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val duration: Int? = null,
        val performer: String? = null,
        val title: String? = null,
    ) : InputMedia() {
        override val type: String = "audio"
    }

    @Serializable
    data class Document(
        override var media: ImplicitFile,
        override var thumbnail: ImplicitFile? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val disableContentTypeDetection: Boolean? = null,
    ) : InputMedia() {
        override val type: String = "document"
    }

    @Serializable
    data class Photo(
        override var media: ImplicitFile,
        override var thumbnail: ImplicitFile? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val hasSpoiler: Boolean? = null,
        val showCaptionAboveMedia: Boolean? = null,
    ) : InputMedia() {
        override val type: String = "photo"
    }

    @Serializable
    data class Video(
        override var media: ImplicitFile,
        override var thumbnail: ImplicitFile? = null,
        val cover: ImplicitFile? = null,
        @Serializable(DurationSerializer::class)
        val startTimestamp: Duration? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val width: Int? = null,
        val height: Int? = null,
        val duration: Int? = null,
        val supportsStreaming: Boolean? = null,
        val hasSpoiler: Boolean? = null,
        val showCaptionAboveMedia: Boolean? = null,
    ) : InputMedia() {
        override val type: String = "video"
    }

    @Serializable
    data class Animation(
        override var media: ImplicitFile,
        override var thumbnail: ImplicitFile? = null,
        val caption: String? = null,
        val parseMode: ParseMode? = null,
        val captionEntities: List<MessageEntity>? = null,
        val width: Int? = null,
        val height: Int? = null,
        val duration: Int? = null,
        val hasSpoiler: Boolean? = null,
        val showCaptionAboveMedia: Boolean? = null,
    ) : InputMedia() {
        override val type: String = "animation"
    }
}
