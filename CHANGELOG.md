CN 相对官方的改动见 [docs/CN_OPTIMIZATIONS.md](docs/CN_OPTIMIZATIONS.md)。下载与每次发版说明见 [GitHub Releases](https://github.com/serein-morii/DiPlay-CN/releases)。

# DiPlay CN 0.2.8.5 — 2026-10-02

- Keep 0.2.8.4 overlay placement and geometric icons; snap overlay offsets onto the 5 % grid so settings and tests stay in range.

# DiPlay CN 0.2.8.4 — 2026-10-02

- Manual turn-card overlay placement: left/right and up/down in 5 % steps across the whole cluster, not just the centre navi window.
- Redraw the overlay as a dark glass card with geometric turn / U-turn / roundabout icons.
- Version numbers after the official 0.2.8 baseline now increment as 0.2.8.1, 0.2.8.2, …

# DiPlay CN 0.2.8 — 2026-10-02

- Rebase onto upstream DiPlay v0.2.8.
- Restore media and navigation audio-stream selection to channels 0–20. Upstream 0.2.8 limited the picker and persistence to 0–10, which dropped vendor stream IDs such as BYD 14/15. Channel 0 remains automatic usage routing; 1–20 are legacy stream types. Existing 0.2.7 navigation stream settings are inherited when the new key is absent.
- Keep the CN package `com.shihab.diplay.cn`, launcher name, Simplified Chinese fallback for unsupported car languages, and the wireless handoff watchdog skip after AirPlay is already active.
- Package the public APK as a release variant, matching the official build type.
- In Dashboard shows → Map with turn card, add Left / Centre / Right and Small / Medium / Large for a DiPlay-drawn instruction card. The iPhone still cannot place its own card independently of the car marker. Overlay size and placement follow the measured centre navi window so Small/Right stays visible and Large does not cover half the cluster.

# DiPlay 0.2.8 — 2026-09-30

- Keep iPhone location reporting active across the wireless Bluetooth-to-Wi-Fi CarPlay handoff; limit location updates to one per second on wireless and USB.
- Add optional ADB wheel-speed and gear reporting for iPhone dead reckoning when GPS is unavailable. Tunnel use has not yet been verified.
- Add optional iOS 27 video playback on the car screen while parked, with iPhone, touchscreen and steering-wheel controls; close playback when leaving P.
- Explain unsupported DRM-protected video such as Apple TV+, which requires a licensed FairPlay receiver.
- Improve playback error reporting and preserve CarPlay when the head unit cannot play a video.

# DiPlay 0.2.7 — 2026-09-29

- App interface in English, Simplified Chinese, Arabic, Russian and Spanish; synchronized Android app-language settings.
- Steering-wheel media controls and long-press Siri on supported BYD firmware while CarPlay is on screen.
- Dashboard display choices: map, turn card, or both; corrected dashboard keyframe recovery.
- Optional ADB feature on supported DiLink 5.0: pause the dashboard map stream when its display mode hides the map.
- Optional ADB battery reporting for Apple Maps, with warning threshold, charging-connector selection and a checked reconnect action.
- Audio playback reliability fixes and clearer dashboard settings.
- Clarify the BYD-only support scope on the README and all five website editions.

# 0.2.0 — BYD navigation and connection improvements

- Standalone windshield HUD arrows, distance and street names on the verified DiLink5.1 firmware; no ADB, root or computer helper.
- Retain contributor cluster/SOME-IP navigation, route parsing, BYD CarPlay icon and display-size presets.
- Fix Car hotspot startup by using scoped IPv6 when available and binding discovery/probing to the AP interface. Physically confirmed on the development car.
- Drain asynchronously decoded audio during packet gaps and rebuild the music buffer after starvation. Wi-Fi Direct is much better in the user retest; occasional audio cutouts remain for a later version.
- Preserve bounded music-buffer choices, USB read improvements and decoder recovery; fix USB request/close races and keep vendor output outside phone callbacks.
- Save audio/video/receive timing and discovery diagnostics without road names or protocol payloads.
- HUD cleanup on normal end/disconnect/off/stale input; interrupted sessions recover on the next app launch. Force-stop may leave guidance visible until reopening.
- Thanks to @romanchukg-cloud and @georgiyrr for PR #3 and vehicle testing.

# 0.1.0 release restored — 2026-09-25

- Rebuilt and signed the APK locally with explicitly supplied runtime authentication assets.
- Restored release downloads; no app behavior or version-code change from 0.1.0.
- Accessory identity remains in the APK only. No credential files enter Git or the source archive.
- Retained generated test identities and public-source credential checks.
- Source/CI builds omit runtime identity assets by default; local packaging requires an explicit external directory.

# Source reset — 2026-09-25

- Withdrew the 0.1.0 APK and removed its release tag.
- Reset the public branch after preserving restricted local incident records.
- Removed static synthetic test private keys; generate test identities at runtime.
- Removed automatic private-asset packaging and disabled the old release build script.
- Added a build guard rejecting credential asset files.
- Replaced the download site with a five-language suspension notice.

The APK was subsequently rebuilt and restored as described above. Existing copies cannot be recalled by a Git history reset.
