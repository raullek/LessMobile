package az.less.mobile

import android.app.Application
import az.less.mobile.preferences.appContext
import az.less.mobile.di.initKoin

class LessApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        initKoin {}
    }
}