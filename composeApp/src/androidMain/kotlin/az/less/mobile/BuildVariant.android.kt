package az.less.mobile

actual fun currentBuildVariant(): BuildVariant =
    if (BuildConfig.DEBUG) BuildVariant.DEVELOPMENT else BuildVariant.PRODUCTION
