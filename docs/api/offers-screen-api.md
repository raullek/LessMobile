# Offers Screen API Documentation

## Overview

The Offers Screen requires two separate API calls:
1. **User Info** - Fetches current user information for the toolbar
2. **Offers Screen Data** - Fetches all screen content in a single request

---

## 1. User Info API

### Endpoint
```
GET /api/v1/user/me
```

### Response
```json
{
  "id": "user_123",
  "name": "Katheryn",
  "avatarUrl": "https://cdn.example.com/avatars/user_123.jpg"
}
```

### Response Fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `id` | string | Yes | Unique user identifier |
| `name` | string | Yes | User's display name |
| `avatarUrl` | string | No | URL to user's avatar image |

---

## 2. Offers Screen Data API

### Endpoint
```
GET /api/v1/offers/home
```

### Response
```json
{
  "categories": [
    {
      "id": "cat_1",
      "type": "FOOD_CATEGORY",
      "title": "Burger",
      "imageUrl": "https://cdn.example.com/categories/burger.png"
    },
    {
      "id": "cat_2",
      "type": "FOOD_CATEGORY",
      "title": "Pizza",
      "imageUrl": "https://cdn.example.com/categories/pizza.png"
    }
  ],
  "specialCategories": [
    {
      "id": "special_1",
      "type": "DISCOUNT",
      "title": "Special discount for Desserts",
      "description": "Hurry to pick up from 22:00",
      "imageUrl": "https://cdn.example.com/specials/desserts.jpg"
    }
  ],
  "segmentedCategories": [
    {
      "id": "nearest",
      "type": "NEAREST",
      "title": "Nearest"
    },
    {
      "id": "top_rated",
      "type": "TOP_RATED",
      "title": "Top rated"
    },
    {
      "id": "hot_deals",
      "type": "HOT_DEALS",
      "title": "Hot deals"
    }
  ],
  "offerSections": [
    {
      "id": "section_top_rated",
      "type": "TOP_RATED",
      "title": "Top rated",
      "offers": [
        {
          "id": "offer_1",
          "title": "Belgian Coffee",
          "description": "Snacks and Drinks",
          "imageUrl": "https://cdn.example.com/offers/belgian-coffee.jpg",
          "imageBgColor": "#fff2eb",
          "quantity": 12,
          "originalPrice": 22.99,
          "currentPrice": 12.99,
          "bagType": "Small Bag",
          "category": "Snacks and Drinks",
          "pickupTime": "17:00 - 23:00",
          "merchant": {
            "id": "merchant_1",
            "name": "Belgian Chocolate & Coffee",
            "logoUrl": "https://cdn.example.com/merchants/belgian.png",
            "location": "1.2 km",
            "rating": 4.9
          }
        }
      ]
    },
    {
      "id": "section_late_dinner",
      "type": "RECOMMENDATION",
      "title": "Top picks for late dinner",
      "offers": []
    }
  ]
}
```

### Response Fields

#### Root Object

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `categories` | array | Yes | Food category items (horizontal scroll) |
| `specialCategories` | array | Yes | Special discount banners (pager) |
| `segmentedCategories` | array | Yes | Filter segments (Nearest, Top rated, Hot deals) |
| `offerSections` | array | Yes | Offer sections with their items |

#### Category Object

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `id` | string | Yes | Unique category identifier |
| `type` | string | Yes | Category type (e.g., "FOOD_CATEGORY") |
| `title` | string | Yes | Display title |
| `imageUrl` | string | Yes | URL to category image |

#### Special Category Object

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `id` | string | Yes | Unique identifier |
| `type` | string | Yes | Type (e.g., "DISCOUNT", "PROMOTION") |
| `title` | string | Yes | Main title text |
| `description` | string | Yes | Subtitle/description text |
| `imageUrl` | string | Yes | URL to banner image |

#### Segmented Category Object

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `id` | string | Yes | Unique identifier |
| `type` | string | Yes | Segment type: "NEAREST", "TOP_RATED", "HOT_DEALS" |
| `title` | string | Yes | Display title |

#### Offer Section Object

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `id` | string | Yes | Unique section identifier |
| `type` | string | Yes | Section type (e.g., "TOP_RATED", "RECOMMENDATION") |
| `title` | string | Yes | Section title |
| `offers` | array | Yes | List of offers in this section |

#### Offer Object

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `id` | string | Yes | Unique offer identifier |
| `title` | string | Yes | Offer title |
| `description` | string | No | Offer description |
| `imageUrl` | string | No | URL to offer image |
| `imageBgColor` | string | No | Background color for image area (hex) |
| `quantity` | integer | Yes | Items left count |
| `originalPrice` | number | Yes | Original price |
| `currentPrice` | number | Yes | Discounted price |
| `bagType` | string | Yes | Bag type (e.g., "Small Bag", "Medium Bag") |
| `category` | string | Yes | Offer category |
| `pickupTime` | string | Yes | Pickup time range |
| `merchant` | object | Yes | Merchant information |

#### Merchant Object (nested in Offer)

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `id` | string | Yes | Unique merchant identifier |
| `name` | string | Yes | Merchant name |
| `logoUrl` | string | No | URL to merchant logo |
| `location` | string | Yes | Distance from user |
| `rating` | number | Yes | Merchant rating (0.0 - 5.0) |

---

## Type Enums

### Segmented Category Types
- `NEAREST` - Sort by distance
- `TOP_RATED` - Sort by rating
- `HOT_DEALS` - Sort by discount percentage

### Offer Section Types
- `TOP_RATED` - Top rated offers
- `RECOMMENDATION` - Personalized recommendations
- `NEARBY` - Nearby offers
- `NEW` - New offers

---

## Error Responses

### 401 Unauthorized
```json
{
  "error": "UNAUTHORIZED",
  "message": "Authentication required"
}
```

### 500 Internal Server Error
```json
{
  "error": "INTERNAL_ERROR",
  "message": "An unexpected error occurred"
}
```
