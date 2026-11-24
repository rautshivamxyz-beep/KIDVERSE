# KIDVERSE privacy notice — draft for owner review

> **Do not publish this draft unchanged.** Replace all bracketed placeholders, obtain an appropriate legal review, publish it at a stable URL and verify every statement against the final APK and all distribution services.

**Effective date:** [DAY MONTH YEAR]  
**App owner/publisher:** [LEGAL NAME / BUSINESS NAME]  
**Privacy/support contact:** [WORKING EMAIL ADDRESS]  
**Public privacy-notice URL:** [URL]

## Who this notice is for

KIDVERSE is designed as an educational app for children approximately 6–10 years old and is intended to be used with parent or guardian awareness. This notice explains the current app build's local storage and optional network functions.

## Information KIDVERSE asks for

The current app has no child account, registration form, profile, public chat, advertising SDK or analytics SDK. Its normal learning activities do not ask a child to provide a name, home address, school, phone number, precise location or payment information.

The offline lesson helper processes the text typed into it on the device to search the installed lesson library. It is not a cloud AI service and does not send the question to an AI provider.

## Information stored on the device

KIDVERSE stores the following in app-private storage on the device:
- Learning stars, activity reward/completion counters and skill-practice completion marks.
- The selected language in the Real-World Skills screen.
- A drawing saved by the child in Creator Studio.
- A lesson pack after the user chooses to download it.

This information is not uploaded to a KIDVERSE account or server by the current build. The app has no account-sync or cloud-backup feature. Clearing KIDVERSE app data or uninstalling the app removes its app-private data. The Parent Zone's **Reset learning progress** control clears stars, activity rewards and completed skill-practice marks, but intentionally keeps saved drawings and downloaded lesson packs.

## Optional network connections

The Android app requests internet access for two user-started functions:
1. **Download pack:** downloads the KIDVERSE lesson JSON from the project's public GitHub repository so lessons can be used offline afterwards.
2. **Check for app updates:** a parent can start this check from the Parent Zone. The app checks GitHub Releases and may offer a parent-initiated APK download and Android installation prompt.

The current app does not perform the update check automatically at launch. When a device connects to GitHub, GitHub may receive ordinary connection data such as the device's IP address and may process server/security logs under GitHub's own terms and privacy notice. KIDVERSE does not control GitHub's independent processing. Review GitHub's current privacy documentation before publishing this notice.

## Permissions and third-party software

The current Android manifest requests internet access and `REQUEST_INSTALL_PACKAGES` for the direct-APK update flow. The app does not request location, microphone, camera, contacts, phone-state or advertising-ID permissions. The app uses Android's system download/installation surfaces for user-started updates. The final release must be checked again for any new SDK, permission or external service.

## Advertising, analytics and sale of data

The current project contains no advertising SDK or analytics SDK, and does not sell children's personal information. Do not add advertising, analytics, behavioural profiling, cloud AI, voice/video features or social features without updating this notice and completing a fresh privacy and child-safety review.

## Parents and guardians

Parents/guardians should supervise real-world activities where the lesson asks for adult help. Do not let children enter personal information into the offline helper. The Parent Zone currently uses a simple arithmetic question; it is not strong authentication and must not be relied on as a security boundary.

## Purchases and support

The current app does not contain an in-app payment processor. Any sale or payment is handled by the separate seller/distribution channel, whose privacy notice and terms must also be shown to the buyer. For app support, privacy questions or deletion requests, contact **[WORKING EMAIL ADDRESS]**. Replace this placeholder with a monitored address before sale.

## Changes to this notice

The publisher should update this notice whenever app features, permissions, SDKs, download/update hosts or data practices change. The effective date above must match the published version.
