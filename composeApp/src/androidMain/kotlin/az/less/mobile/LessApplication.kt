package az.less.mobile

import android.app.Application
import az.less.mobile.preferences.appContext
import az.less.mobile.di.initKoin
import com.google.firebase.FirebaseApp

class LessApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        initKoin {}
        initializeFirebase()
    }

    private fun initializeFirebase(){
        FirebaseApp.initializeApp(this)
    }
}