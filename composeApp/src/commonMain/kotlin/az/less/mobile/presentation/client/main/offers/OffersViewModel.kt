package az.less.mobile.presentation.client.main.offers

import androidx.compose.runtime.key
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.analytics.AnalyticsWrapper
import az.less.mobile.data.remote.dto.CategoryDto
import az.less.mobile.data.remote.dto.MerchantDto
import az.less.mobile.data.remote.dto.OfferDto
import az.less.mobile.data.remote.dto.OfferSectionDto
import az.less.mobile.data.remote.dto.OffersScreenDto
import az.less.mobile.data.remote.dto.SegmentedCategoryDto
import az.less.mobile.data.remote.dto.SpecialCategoryDto
import az.less.mobile.presentation.client.main.offers.models.Category
import az.less.mobile.presentation.client.main.offers.models.OfferItem
import az.less.mobile.presentation.client.main.offers.models.OfferMerchant
import az.less.mobile.presentation.client.main.offers.models.OfferSection
import az.less.mobile.presentation.client.main.offers.models.SegmentedCategory
import az.less.mobile.presentation.client.main.offers.models.SegmentedCategoryType
import az.less.mobile.presentation.client.main.offers.models.SpecialDiscountItem
import az.less.mobile.presentation.client.main.offers.models.UserInfo
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_explore_24dp
import lessmobile.composeapp.generated.resources.ic_mark_16dp
import lessmobile.composeapp.generated.resources.ic_star_16dp
import lessmobile.composeapp.generated.resources.test_offer_category_burger
import lessmobile.composeapp.generated.resources.test_offer_category_pasta
import lessmobile.composeapp.generated.resources.test_offer_category_pizza
import lessmobile.composeapp.generated.resources.test_offer_category_sushi
import lessmobile.composeapp.generated.resources.test_offer_item_image
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for Offers Screen using Orbit MVI
 */
class OffersViewModel(private val analyticsWrapper: AnalyticsWrapper) : ViewModel(),
    ContainerHost<OffersState, OffersSideEffect> {

    override val container: Container<OffersState, OffersSideEffect> =
        viewModelScope.container(OffersState())

    init {
        loadUserInfo()
        loadScreenData()
    }

    /**
     * Handle user intents
     */
    fun onIntent(intent: OffersIntent) {
        when (intent) {
            is OffersIntent.OnSearchQueryChanged -> handleSearchQueryChanged(intent.query)
            is OffersIntent.OnSearchClicked -> handleSearchClicked()
            is OffersIntent.OnCategorySelected -> handleCategorySelected(intent.categoryId)
            is OffersIntent.OnSpecialCategoryClicked -> handleSpecialCategoryClicked(intent.specialCategoryId)
            is OffersIntent.OnSegmentSelected -> handleSegmentSelected(intent.segmentId)
            is OffersIntent.OnOfferItemClicked -> handleOfferItemClicked(intent.offerId)
            is OffersIntent.OnSeeAllClicked -> handleSeeAllClicked(intent.sectionId)
        }
    }

    // ==================== Data Loading ====================

    /**
     * Load user info (separate request)
     * GET /api/v1/user/me
     */
    private fun loadUserInfo() = intent {
        reduce { state.copy(isUserLoading = true) }
        // TODO: Replace with actual API call
        val userInfo = getMockUserInfo()
        reduce {
            state.copy(
                userInfo = userInfo,
                isUserLoading = false
            )
        }
    }

    /**
     * Load screen data (single request)
     * GET /api/v1/offers/home
     */
    private fun loadScreenData() = intent {
        reduce { state.copy(isLoading = true) }

        // TODO: Replace with actual API call
        val response = getOffers()

        // Map DTO to domain models
        reduce {
            state.copy(
                categories = response.categories.map { it.toDomain() },
                specialCategories = response.specialCategories.map { it.toDomain() },
                segmentedCategories = response.segmentedCategories.map { it.toDomain() },
                offerSections = response.offerSections.map { it.toDomain() },
                isLoading = false
            )
        }
    }

    // ==================== Intent Handlers ====================

    private fun handleSearchQueryChanged(query: String) = intent {
        reduce { state.copy(searchQuery = query) }
    }

    private fun handleSearchClicked() = intent {
        postSideEffect(OffersSideEffect.NavigateToSearch)
    }

    private fun handleCategorySelected(categoryId: String) = intent {
        val category = state.categories.find { it.id == categoryId }
        if (category != null) {
            postSideEffect(
                OffersSideEffect.NavigateToCategoryOffers(
                    categoryId = category.id,
                    categoryType = category.type,
                    categoryTitle = category.title
                )
            )
        }
    }

    private fun handleSpecialCategoryClicked(specialCategoryId: String) = intent {
        val specialCategory = state.specialCategories.find { it.id == specialCategoryId }
        if (specialCategory != null) {
            postSideEffect(
                OffersSideEffect.NavigateToCategoryOffers(
                    categoryId = specialCategory.id,
                    categoryType = specialCategory.type,
                    categoryTitle = specialCategory.title
                )
            )
        }
    }

    private fun handleSegmentSelected(segmentId: String) = intent {
        val segment = state.segmentedCategories.find { it.id == segmentId }
        if (segment != null) {
            postSideEffect(
                OffersSideEffect.NavigateToCategoryOffers(
                    categoryId = segment.id,
                    categoryType = segment.type.name,
                    categoryTitle = segment.title
                )
            )
        }
    }

    private fun handleSeeAllClicked(sectionId: String) = intent {
        val section = state.offerSections.find { it.id == sectionId }
        if (section != null) {
            postSideEffect(
                OffersSideEffect.NavigateToCategoryOffers(
                    categoryId = section.id,
                    categoryType = section.type,
                    categoryTitle = section.title
                )
            )
        }
    }

    private fun handleOfferItemClicked(offerId: String) = intent {
        postSideEffect(OffersSideEffect.NavigateToReserve)
    }

    // ==================== Mock Data ====================

    private fun getMockUserInfo(): UserInfo {
        return UserInfo(
            id = "user_123",
            name = "Katheryn",
            avatarUrl = null
        )
    }

    /**
     * Mock API request: GET /api/v1/offers/home
     * Returns all screen data in a single response
     */
    private fun getOffers(): OffersScreenDto {
        return OffersScreenDto(
            categories = listOf(
                CategoryDto(id = "cat_1", type = "FOOD_CATEGORY", title = "Burger", imageUrl = ""),
                CategoryDto(id = "cat_2", type = "FOOD_CATEGORY", title = "Pizza", imageUrl = ""),
                CategoryDto(id = "cat_3", type = "FOOD_CATEGORY", title = "Asian", imageUrl = ""),
                CategoryDto(id = "cat_4", type = "FOOD_CATEGORY", title = "Italian", imageUrl = ""),
                CategoryDto(id = "cat_5", type = "FOOD_CATEGORY", title = "Bakery", imageUrl = ""),
                CategoryDto(id = "cat_6", type = "FOOD_CATEGORY", title = "Desserts", imageUrl = "")
            ),
            specialCategories = listOf(
                SpecialCategoryDto(
                    id = "special_1",
                    type = "DISCOUNT",
                    title = "Special discount for Desserts",
                    description = "Hurry to pick up from 22:00",
                    imageUrl = ""
                ),
                SpecialCategoryDto(
                    id = "special_2",
                    type = "DISCOUNT",
                    title = "Special discount for Pizza",
                    description = "Hurry to pick up from 20:00",
                    imageUrl = ""
                ),
                SpecialCategoryDto(
                    id = "special_3",
                    type = "DISCOUNT",
                    title = "Special discount for Burgers",
                    description = "Hurry to pick up from 19:00",
                    imageUrl = ""
                ),
                SpecialCategoryDto(
                    id = "special_4",
                    type = "DISCOUNT",
                    title = "Special discount for Sushi",
                    description = "Hurry to pick up from 21:00",
                    imageUrl = ""
                ),
                SpecialCategoryDto(
                    id = "special_5",
                    type = "DISCOUNT",
                    title = "Special discount for Pasta",
                    description = "Hurry to pick up from 18:00",
                    imageUrl = ""
                )
            ),
            segmentedCategories = listOf(
                SegmentedCategoryDto(id = "nearest", type = "NEAREST", title = "Nearest"),
                SegmentedCategoryDto(id = "top_rated", type = "TOP_RATED", title = "Top rated"),
                SegmentedCategoryDto(id = "hot_deals", type = "HOT_DEALS", title = "Hot deals")
            ),
            offerSections = listOf(
                OfferSectionDto(
                    id = "section_top_rated",
                    type = "TOP_RATED",
                    title = "Top rated",
                    offers = listOf(
                        OfferDto(
                            id = "offer_1",
                            title = "Belgian Coffee",
                            description = "Snacks and Drinks",
                            imageUrl = null,
                            imageBgColor = "#fff2eb",
                            quantity = 12,
                            originalPrice = 22.99,
                            currentPrice = 12.99,
                            bagType = "Small Bag",
                            category = "Snacks and Drinks",
                            pickupTime = "17:00 - 23:00",
                            merchant = MerchantDto(
                                id = "merchant_1",
                                name = "Belgian Chocolate & Coffee",
                                logoUrl = null,
                                location = "1.2 km",
                                rating = 4.9
                            )
                        ),
                        OfferDto(
                            id = "offer_2",
                            title = "Belgian Chocolate",
                            description = "Desserts",
                            imageUrl = null,
                            imageBgColor = "#fff2eb",
                            quantity = 5,
                            originalPrice = 18.99,
                            currentPrice = 10.99,
                            bagType = "Medium Bag",
                            category = "Desserts",
                            pickupTime = "17:00 - 23:00",
                            merchant = MerchantDto(
                                id = "merchant_1",
                                name = "Belgian Chocolate & Coffee",
                                logoUrl = null,
                                location = "1.2 km",
                                rating = 4.9
                            )
                        ),
                        OfferDto(
                            id = "offer_3",
                            title = "Surprise Mix",
                            description = "Mixed",
                            imageUrl = null,
                            imageBgColor = "#fff2eb",
                            quantity = 3,
                            originalPrice = 25.99,
                            currentPrice = 15.99,
                            bagType = "Large Bag",
                            category = "Mixed",
                            pickupTime = "17:00 - 23:00",
                            merchant = MerchantDto(
                                id = "merchant_1",
                                name = "Belgian Chocolate & Coffee",
                                logoUrl = null,
                                location = "1.2 km",
                                rating = 4.9
                            )
                        )
                    )
                ),
                OfferSectionDto(
                    id = "section_late_dinner",
                    type = "RECOMMENDATION",
                    title = "Top picks for late dinner",
                    offers = listOf(
                        OfferDto(
                            id = "offer_4",
                            title = "Late Night Pizza",
                            description = "Italian",
                            imageUrl = null,
                            imageBgColor = "#fff2eb",
                            quantity = 8,
                            originalPrice = 19.99,
                            currentPrice = 14.99,
                            bagType = "Medium Bag",
                            category = "Pizza",
                            pickupTime = "21:00 - 23:30",
                            merchant = MerchantDto(
                                id = "merchant_2",
                                name = "Pizza Palace",
                                logoUrl = null,
                                location = "0.8 km",
                                rating = 4.7
                            )
                        ),
                        OfferDto(
                            id = "offer_5",
                            title = "Sushi Combo",
                            description = "Japanese",
                            imageUrl = null,
                            imageBgColor = "#fff2eb",
                            quantity = 4,
                            originalPrice = 32.99,
                            currentPrice = 22.99,
                            bagType = "Large Bag",
                            category = "Sushi",
                            pickupTime = "20:00 - 22:00",
                            merchant = MerchantDto(
                                id = "merchant_3",
                                name = "Sushi Master",
                                logoUrl = null,
                                location = "1.5 km",
                                rating = 4.8
                            )
                        )
                    )
                )
            )
        )
    }

    // ==================== DTO to Domain Mappers ====================

    private fun CategoryDto.toDomain(): Category {
        // Map imageUrl to testImage based on category title for mock
        val testImage = when (title.lowercase()) {
            "burger" -> Res.drawable.test_offer_category_burger
            "pizza" -> Res.drawable.test_offer_category_pizza
            "asian" -> Res.drawable.test_offer_category_sushi
            else -> Res.drawable.test_offer_category_pasta
        }
        return Category(
            id = id,
            type = type,
            title = title,
            imageUrl = imageUrl.ifEmpty { null },
            testImage = testImage
        )
    }

    private fun SpecialCategoryDto.toDomain(): SpecialDiscountItem {
        return SpecialDiscountItem(
            id = id,
            type = type,
            title = title,
            description = description,
            imageUrl = imageUrl.ifEmpty { null },
            testImage = Res.drawable.test_offer_item_image
        )
    }

    private fun SegmentedCategoryDto.toDomain(): SegmentedCategory {
        val segmentType = when (type) {
            "NEAREST" -> SegmentedCategoryType.NEAREST
            "TOP_RATED" -> SegmentedCategoryType.TOP_RATED
            "HOT_DEALS" -> SegmentedCategoryType.HOT_DEALS
            else -> SegmentedCategoryType.NEAREST
        }
        val (icon, iconTint) = when (segmentType) {
            SegmentedCategoryType.NEAREST -> Res.drawable.ic_explore_24dp to 0xFFFF8B38L
            SegmentedCategoryType.TOP_RATED -> Res.drawable.ic_star_16dp to 0xFF5AA9E7L
            SegmentedCategoryType.HOT_DEALS -> Res.drawable.ic_mark_16dp to 0xFFAD3CDAL
        }
        return SegmentedCategory(
            id = id,
            type = segmentType,
            title = title,
            icon = icon,
            iconTint = iconTint
        )
    }

    private fun OfferSectionDto.toDomain(): OfferSection {
        return OfferSection(
            id = id,
            type = type,
            title = title,
            offers = offers.map { it.toDomain() },
            showSeeAll = true
        )
    }

    private fun OfferDto.toDomain(): OfferItem {
        return OfferItem(
            id = id,
            title = title,
            description = description,
            imageUrl = imageUrl,
            imageBgColor = imageBgColor ?: "#fff2eb",
            quantity = quantity,
            originalPrice = originalPrice.toString(),
            currentPrice = currentPrice.toString(),
            bagType = bagType,
            category = category,
            pickupTime = pickupTime,
            merchant = merchant.toDomain()
        )
    }

    private fun MerchantDto.toDomain(): OfferMerchant {
        return OfferMerchant(
            id = id,
            name = name,
            logoUrl = logoUrl,
            location = location,
            rating = rating.toFloat()
        )
    }
}
