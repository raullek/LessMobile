package az.less.mobile.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

internal const val DATA_STORE_FILE_NAME = "less.preferences_pb"

expect fun createPlatformDataStore(): DataStore<Preferences>
