# Role-Based Navigation & Access Control Spec

## Roles

Three possible roles: `client`, `merchant`, `partner`.
A user can have multiple roles simultaneously (e.g. `[client, merchant]`, `[client, merchant, partner]`).

### Navigation Modes

The app has two navigation modes (roots):

| Mode | Navigation Graph | Available to roles |
|------|------------------|---------------------|
| **Client** | `ClientGraph` | `client` (all authenticated users) |
| **Merchant** | `MerchantGraph` | `merchant`, `partner` |

> Partner uses the same `MerchantGraph` as merchant — no separate navigation root.

### Mode Switching

- **Switch button** in More screen is visible when the user has access to **more than one navigation mode**.
- Determined by checking: user has `client` role AND at least one of `[merchant, partner]`.
- Implementation: define a capability mapping (`role → set of available modes`) rather than checking `roles.size`. This is extensible for future roles.

```kotlin
enum class AppMode { CLIENT, MERCHANT }

fun User.availableModes(): Set<AppMode> {
    val modes = mutableSetOf<AppMode>()
    if (roles.contains("client")) modes.add(AppMode.CLIENT)
    if (roles.contains("merchant") || roles.contains("partner")) modes.add(AppMode.MERCHANT)
    return modes
}
fun User.canSwitchMode(): Boolean = availableModes().size > 1
```

### Default Landing & Last-Used Mode

- Store `last_used_mode` (`"client"` | `"merchant"`) in DataStore preferences.
- On app launch (after login), read `last_used_mode`:
  - If value exists and user has access to that mode → navigate to it.
  - Otherwise → default to `CLIENT`.
- When user switches mode via More screen → persist the new mode to `last_used_mode`.

---

## More Screen Behavior

### Client More Screen (`MoreScreen`)

- Header: user personal info (name, email, avatar) — **unchanged**.
- Switch button: visible if `user.canSwitchMode()` is true (label: "Switch to Merchant").
- Menu items: unchanged from current implementation.

### Merchant More Screen (`MerchMoreScreen`)

- Header: **venue info only** — venue name, businessLogo, rating, totalReviews. No personal user info.
  - Data source: `User.venue` from cached session.
- Switch button: visible if `user.canSwitchMode()` is true (label: "Switch to Client").
- Menu items: role-dependent (see below).

---

## Role-Based Feature Access (Merchant Mode)

| Feature | `merchant` | `partner` |
|---------|:----------:|:---------:|
| View/manage own lots | Yes | Yes (if single venue) |
| Add lots to assigned venue | Yes | Yes (if single venue) |
| Places (view/add/edit venues) | **No** (hidden) | Yes |
| Create merchants & assign to venues | **No** (hidden) | Yes |
| Orders | Yes | Yes |
| History | Yes | Yes |

### Partner as Merchant (Single Venue)

- If partner has only one venue (determined by `User.venue != null`), they can also manage lots for that venue — same as a regular merchant.
- Partner retains all partner privileges (Places, Create Merchants) regardless.
- No venue selection screen exists currently; may be added in future.

### Menu Visibility in Merchant More Screen

```
Merchant role:
  - Notification
  - Dark Mode
  - Switch to Client (if canSwitchMode)

Partner role:
  - Places          ← partner only
  - Notification
  - Dark Mode
  - Switch to Client (if canSwitchMode)
```

> "Places" cell is **hidden** for merchant role, **visible** for partner role.

---

## Future Considerations

- **Partner entity**: API will eventually return a `partner` object in the user response (similar to `venue` for merchant). When available, update `VerifyOtpUser`, `User`, `UserEntity` with partner-specific fields.
- **Venue selection**: Partner may need to select between multiple venues. Not implemented now.
- **Partner-specific navigation**: If partner scope grows significantly, a third `AppMode.PARTNER` may be introduced. The `availableModes()` pattern supports this without refactoring.
- **Role-specific sections**: More menu sections can be extended per-role by passing the role set to section builders.

---

## Implementation Checklist

### 1. Data Layer
- [x] Add `AppMode` enum (`CLIENT`, `MERCHANT`)
- [x] Add `last_used_mode` preference key to `SessionLocalRepositoryImpl`
- [x] Add `saveLastUsedMode(mode: AppMode)` and `lastUsedMode: Flow<AppMode>` to `SessionLocalRepository`

### 2. Domain Layer
- [x] Add `availableModes()` and `canSwitchMode()` extensions on `User`
- [x] Add `isPartner: Boolean` computed property on `User` (`roles.contains("partner")`)
- [x] Add `isMerchant: Boolean` computed property on `User` (`roles.contains("merchant")`)

### 3. Navigation
- [x] Update `AppRootNavigation()` to read `last_used_mode` instead of hardcoded merchant-role check
- [x] Persist mode on switch in both More screens

### 4. Client More Screen
- [x] Replace `hasMerchantRole` with `canSwitchMode` (covers merchant + partner)
- [x] Update `MoreState`, `MoreViewModel` accordingly

### 5. Merchant More Screen
- [x] Update `MerchMoreHeader` to display venue info (name, logo, rating, reviews) instead of user info
- [x] Update `MerchMoreViewModel` to load venue data from cached user
- [x] Conditionally show/hide "Places" cell based on `user.isPartner`
- [x] Replace hardcoded section building with role-aware logic

### 6. Merchant More ViewModel
- [x] Pass user roles to section builder
- [x] Filter cells based on role capabilities
