package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ContentDto(
    val key: String,
    val title: String,
    val body: String,
    val format: String,
    val version: Int,
    val updatedAt: String
)
