package az.less.mobile.domain.model.auth

data class User(
    val id: String,
    val name: String,
    val email: String,
    val roles: List<String> = emptyList(),
    val status: String = ""
)