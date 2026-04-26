package az.less.mobile

enum class BuildVariant { DEVELOPMENT, PRODUCTION }

expect fun currentBuildVariant(): BuildVariant
