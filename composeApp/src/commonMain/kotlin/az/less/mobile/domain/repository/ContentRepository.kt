package az.less.mobile.domain.repository

import az.less.mobile.domain.model.ContentData
import az.less.mobile.network.NetworkResult

interface ContentRepository {
    suspend fun getTerms(): NetworkResult<ContentData>
}
