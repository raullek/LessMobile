# Role-Based Navigation & Access Control Spec

## Roles

Three possible roles: `client`, `merchant`, `partner`.
A user can have multiple roles simultaneously (e.g. `[client, merchant]`, `[client, partner]`, `[client, merchant, partner]`).

### Navigation Modes

The app has three navigation modes (roots):

| Mode | Navigation Graph | Root constant | Available to |
|------|------------------|----------------|---------------|
| **Client** | `ClientGraph` | `ROOT_CLIENT` | any authenticated user with `client` role |
| **Merchant** | `MerchantGraph` | `ROOT_MERCHANT` | users with `merchant` role (with or without `partner`) |
| **Partner** | `PartnerGraph` | `ROOT_PARTNER` | users with `partner` role **and no** `merchant` role |

### Role → Mode Mapping

`merchant` takes precedence over `partner` — a user who is both merchant and partner stays on `MerchantGraph` and gets the partner-only cells surfaced inside it.

```kotlin
enum class AppMode { CLIENT, MERCHANT, PARTNER }

fun User.availableModes(): Set<AppMode> {
    val modes = mutableSetOf<AppMode>()
    if (isClient) modes.add(AppMode.CLIENT)
    if (isMerchant) modes.add(AppMode.MERCHANT)
    else if (isPartner) modes.add(AppMode.PARTNER)
    return modes
}
fun User.canSwitchMode(): Boolean = isClient && (isMerchant || isPartner)
```

Resulting matrix:

| Roles                              | `availableModes()`       | Start mode (if no `lastUsedMode`) |
|------------------------------------|--------------------------|-----------------------------------|
| `[client]`                         | `{CLIENT}`               | `CLIENT`                          |
| `[client, merchant]`               | `{CLIENT, MERCHANT}`     | `CLIENT`                          |
| `[client, partner]`                | `{CLIENT, PARTNER}`      | `CLIENT`                          |
| `[client, merchant, partner]`      | `{CLIENT, MERCHANT}`     | `CLIENT` (MERCHANT holds partner extras) |

### Default Landing & Last-Used Mode

- `last_used_mode` is stored in DataStore (`CLIENT` | `MERCHANT` | `PARTNER`).
- On app launch (after login), `AppRootNavigation` reads the value:
  - If the stored mode is in `user.availableModes()` → navigate to the matching root.
  - Otherwise → stay on `ROOT_CLIENT`.
- `saveLastUsedMode(...)` is called when the user switches mode (from Client More) and when they switch back to Client (from Merchant/Partner More).

---

## More Screen Behavior

### Client More Screen (`MoreScreen`)

- Header: user personal info (name, email, avatar).
- "Switch to Merchant" cell is shown when `user.canSwitchMode()` is true. On click, the lambda injected from `ClientGraph` picks the target root (`ROOT_MERCHANT` if `MERCHANT ∈ availableModes`, otherwise `ROOT_PARTNER`) and persists the mode.

### Merchant More Screen (`MerchMoreScreen`)

- Header: venue info only — name, logo, rating, reviews (from `user.venue`).
- "Switch to Client" cell is shown when `user.canSwitchMode()` is true.
- Cell visibility:
  - `Places` cell is **visible** iff `user.isPartner` (so `[c,m,p]` users can manage venues from merchant flow).
  - All other cells are role-independent.

### Partner More Screen (`PartnerMoreScreen`)

- Header: venue info (same fields as merchant's header).
- "Switch to Client" cell is shown when `user.canSwitchMode()` is true.
- **No `Places` cell** — Places is a dedicated bottom-navigation tab in `PartnerGraph`, so surfacing it again in More would be redundant.

---

## Role-Based Feature Access

### Merchant Mode (`MerchantGraph`)

| Feature                              | `merchant` only | `merchant + partner` |
|--------------------------------------|:---------------:|:--------------------:|
| Orders (bottom tab)                  | Yes             | Yes                  |
| Add Lot (bottom tab)                 | Yes             | Yes                  |
| History (bottom tab)                 | Yes             | Yes                  |
| More (bottom tab)                    | Yes             | Yes                  |
| Places (from More → sub-screen)      | **Hidden**      | Visible              |
| Add/Edit venue, branch users         | —               | Yes (via Places)     |

### Partner Mode (`PartnerGraph`)

Bottom-navigation tabs: **Places / History / More** (no Orders, no Add Lot).

| Feature                              | `partner` (no merchant) |
|--------------------------------------|:-----------------------:|
| Places (bottom tab — venue list)     | Yes                     |
| History (bottom tab)                 | Yes                     |
| More (bottom tab)                    | Yes                     |
| Add/Edit venue, branch users         | Yes (via Places)        |
| Orders / Add Lot                     | — (not in this mode)    |

> `PartnerGraph` shares the same sub-screens as Merchant's Places flow (EditProfile, BranchVerification, BranchUsers, AddBranchUser, SelectBranchLocation, InputAddress). They are currently duplicated in `presentation/partner/` for easier iteration; `data` and `domain` layers remain shared.

---

## Future Considerations

- **Partner entity on API**: when the API returns a dedicated `partner` object (similar to `venue` for merchant), extend `VerifyOtpUser` / `User` / `UserEntity` with partner-specific fields.
- **Multi-venue partner**: `User.venue` is currently a single venue. When a partner needs to select between multiple venues in merchant-like flows, introduce a venue picker.
- **Shared presentation for Places sub-screens**: the copies under `presentation/partner/places/edit/*` can be consolidated back into a neutral package (e.g. `presentation/places/`) once the partner-specific behavior is stabilized.
- **Role-specific analytics**: track mode entry/exit and per-mode feature usage via the recently added analytics tooling.

---

## Implementation Checklist

### 1. Data Layer
- [x] `AppMode` enum (`CLIENT`, `MERCHANT`, `PARTNER`)
- [x] `last_used_mode` preference key in `SessionLocalRepositoryImpl`
- [x] `saveLastUsedMode(mode: AppMode)` and `lastUsedMode: Flow<AppMode>` on `SessionLocalRepository`

### 2. Domain Layer
- [x] `User.availableModes()` returns `{CLIENT, MERCHANT}` when merchant is present, `{CLIENT, PARTNER}` otherwise
- [x] `User.canSwitchMode()` — `isClient && (isMerchant || isPartner)`
- [x] `isPartner` / `isMerchant` / `isClient` computed properties on `User`

### 3. Navigation
- [x] `AppRootNavigation` dispatches to the root matching `lastUsedMode` (when it is still in `availableModes`)
- [x] `NavigationGraph.kt` hosts three roots: `ROOT_CLIENT`, `ROOT_MERCHANT`, `ROOT_PARTNER`
- [x] `PartnerGraph` with `PartnerRoute` sealed interface (Places/History/More + Places sub-screens)
- [x] `AppPartnerRootScreen` + `AppPartnerBottomNavigation` (3 tabs, Places is start destination)
- [x] Client More "Switch to Merchant" picks the target root from `availableModes`
- [x] Login flows (`LoginCode`, `LoginPassword`) post `NavigateToPartner` side-effect when the target is partner

### 4. Client More Screen
- [x] "Switch" cell visible when `user.canSwitchMode()`
- [x] Handler routes to `ROOT_MERCHANT` or `ROOT_PARTNER`

### 5. Merchant More Screen
- [x] Header shows venue info
- [x] `Places` cell shown iff `user.isPartner`
- [x] `Switch to Client` visible iff `canSwitchMode`

### 6. Partner More Screen
- [x] Header shows venue info
- [x] No `Places` cell (Places lives in the bottom navigation instead)
- [x] `Switch to Client` visible iff `canSwitchMode`
