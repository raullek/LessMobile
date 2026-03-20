package az.less.mobile.domain.model.auth

enum class AppMode {
    CLIENT,
    MERCHANT;

    companion object {
        fun fromString(value: String?): AppMode =
            entries.find { it.name.equals(value, ignoreCase = true) } ?: CLIENT
    }
}
