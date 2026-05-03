package az.less.mobile.utils

/**
 * Launch the device's preferred maps/navigation app pointing at the given
 * coordinates with [label] as the pin title, *in directions/navigation mode*.
 *
 * Android: fires `ACTION_VIEW` against `geo:` so the chooser surfaces every
 * installed maps app (Google Maps, Waze, OsmAnd, …) and forces the chooser
 * on each tap so the user can swap apps freely.
 *
 * iOS: prefers Google Maps → Waze → Apple Maps. The first installed match
 * wins; Apple Maps via universal HTTPS is the always-available fallback.
 */
expect fun openDirections(latitude: Double, longitude: Double, label: String)
