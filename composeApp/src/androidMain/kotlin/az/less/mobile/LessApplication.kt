package az.less.mobile

import android.app.Application
import az.less.mobile.di.initKoin

class LessApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {}
    }
}