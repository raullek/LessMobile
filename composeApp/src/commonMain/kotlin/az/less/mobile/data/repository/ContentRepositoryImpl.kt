package az.less.mobile.data.repository

import az.less.mobile.data.datasource.ContentDataSource
import az.less.mobile.data.remote.model.ContentDto
import az.less.mobile.domain.model.ContentData
import az.less.mobile.domain.repository.ContentRepository
import az.less.mobile.network.NetworkResult

class ContentRepositoryImpl(
    private val contentDataSource: ContentDataSource
) : ContentRepository {

    override suspend fun getTerms(): NetworkResult<ContentData> {
        return contentDataSource.getTerms().map { it.toDomain() }
    }
}

private fun ContentDto.toDomain() = ContentData(
    title = title,
    body = body,
    isHtml = format.equals("html", ignoreCase = true)
)
