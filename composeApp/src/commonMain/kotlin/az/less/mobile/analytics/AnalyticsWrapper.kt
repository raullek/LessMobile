package az.less.mobile.analytics

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.FirebaseAnalytics
import dev.gitlive.firebase.analytics.analytics

interface AnalyticsWrapper {
    fun sendEvent(key: String, data: Map<String, Any>?= null)
}

class AnalyticsWrapperImpl() :
    AnalyticsWrapper {
    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    override fun sendEvent(key: String, data: Map<String, Any>?) {
        firebaseAnalytics.logEvent(key,data)
    }


}