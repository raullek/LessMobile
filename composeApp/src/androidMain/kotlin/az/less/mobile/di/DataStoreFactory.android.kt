package az.less.mobile.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import az.less.mobile.preferences.DATA_STORE_FILE_NAME
import okio.Path.Companion.toPath

lateinit var appContext: Context
    internal set

actual fun createPlatformDataStore(): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            appContext.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath.toPath()
        }
    )
}
