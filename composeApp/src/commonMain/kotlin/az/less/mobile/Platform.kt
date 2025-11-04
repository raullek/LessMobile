package az.less.mobile

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform