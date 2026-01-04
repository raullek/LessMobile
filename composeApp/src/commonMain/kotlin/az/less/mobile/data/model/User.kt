package az.less.mobile.data.model

/**
 * User data model
 */
data class User(
    val id: String,
    val name: String,
    val email: String,
    val avatarUrl: String? = null,
    val co2Saved: String = "0 kg",
    val moneySaved: String = "$0"
)
