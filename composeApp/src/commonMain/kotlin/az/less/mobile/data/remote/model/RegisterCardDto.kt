package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class RegisterCardDto(
    val redirectUrl: String? = null,
    val url: String? = null,
    val extra: JsonObject? = null
)
