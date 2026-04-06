package az.less.mobile.presentation.client.account.account

enum class Gender(val displayName: String, val apiValue: String) {
    Male("Male", "male"),
    Female("Female", "female"),
    Other("Don't want to specify", "other");

    companion object {
        fun fromApiValue(value: String?): Gender? {
            return entries.find { it.apiValue == value }
        }
    }
}

