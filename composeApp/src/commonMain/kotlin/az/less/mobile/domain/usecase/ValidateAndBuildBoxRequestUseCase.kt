package az.less.mobile.domain.usecase

import az.less.mobile.data.remote.model.CreateBoxRequest

class ValidateAndBuildBoxRequestUseCase {

    fun execute(params: Params): Result {
        val errors = mutableSetOf<String>()

        if (params.boxType == null) errors.add(SECTION_BAG_TYPE)
        if (params.categoryId == null) errors.add(SECTION_CATEGORIES)
        if (params.pickupRange == null) errors.add(SECTION_PICKUP_TIME)

        val originalPrice = params.originalPriceText.toDoubleOrNull()
        val discountedPrice = params.discountedPriceText.toDoubleOrNull()

        if (originalPrice == null) errors.add(FIELD_PRICE_BEFORE)
        if (discountedPrice == null) errors.add(FIELD_PRICE_AFTER)

        if (originalPrice != null && discountedPrice != null && discountedPrice > originalPrice) {
            errors.add(FIELD_PRICE_AFTER)
        }

        if (errors.isNotEmpty()) return Result.ValidationErrors(errors)

        return Result.Success(
            CreateBoxRequest(
                description = params.description,
                boxType = params.boxType ?: return Result.ValidationErrors(errors),
                categoryId = params.categoryId ?: return Result.ValidationErrors(errors),
                tagIds = params.tagIds,
                pickupRange = params.pickupRange ?: return Result.ValidationErrors(errors),
                originalPrice = originalPrice ?: return Result.ValidationErrors(errors),
                discountedPrice = discountedPrice ?: return Result.ValidationErrors(errors),
                quantity = params.quantity
            )
        )
    }

    data class Params(
        val boxType: String?,
        val categoryId: String?,
        val tagIds: List<String>,
        val pickupRange: String?,
        val originalPriceText: String,
        val discountedPriceText: String,
        val description: String,
        val quantity: Int
    )

    sealed interface Result {
        data class Success(val request: CreateBoxRequest) : Result
        data class ValidationErrors(val errors: Set<String>) : Result
    }

    companion object {
        const val SECTION_BAG_TYPE = "bag_type"
        const val SECTION_CATEGORIES = "categories"
        const val SECTION_PICKUP_TIME = "pickup_time"
        const val FIELD_PRICE_BEFORE = "price_before"
        const val FIELD_PRICE_AFTER = "price_after"
    }
}
