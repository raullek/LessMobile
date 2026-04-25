package az.less.mobile.presentation.partner.places.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

/**
 * Model representing a merchant branch/place
 */
@Serializable
data class BranchItem(
    val id: String,
    val name: String,
    val address: String,
    val phone: String,
    val imageUrl: String? = null,
    val logoUrl: String? = null,
    val lotImageUrl: String? = null,
    val itemsOnSale: Int = 0,
    val hasActiveDiscount: Boolean = false,
    val rating: Float = 0f,
    val distance: String = "",
    val status: String? = null,
    val businessName: String? = null,
    val businessDescription: String? = null,
    val defaultBoxDescription: String? = null,
    val email: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
) {
    fun encode(): String = Json.encodeToString(this)

    companion object {
        fun decode(encoded: String): BranchItem? {
            return try {
                Json.decodeFromString<BranchItem>(encoded)
            } catch (e: Exception) {
                null
            }
        }
    }
}
