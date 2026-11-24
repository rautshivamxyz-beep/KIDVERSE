# KIDVERSE

**Learn · Play · Create · Discover**

KIDVERSE is an offline-first Android learning playground designed for children around ages 6–10. This repository currently contains an early MVP, not a finished commercial release.

## What's in this build

The current content update expands the learning material and adds a repeatable science quiz. Content is designed as short, readable activities for children around ages 6–10; an educator/parent review is still needed before commercial release.

- A redesigned home dashboard with learning-world cards and a local star counter
- Math Kingdom: ten progressive arithmetic and fractions challenges with worked hints and one-time rewards
- Science Galaxy: five topic lessons plus a five-question quiz covering plants, Earth, magnets, water and mammals
- Story Forest: five original short stories with reading rewards
- Robot City: a simple sequencing activity
- Explorer Earth: short nature and environment lessons
- Creator Studio: finger drawing, colour selection and save-to-device
- Build-a-World: star milestones and unlock progress
- Parent dashboard: local progress view and reset control

## Privacy and storage

The app has no internet permission, account system, advertising SDK, analytics SDK, location collection or public chat. Stars, activity completion and saved drawings remain in the app's private local storage on the device. Uninstalling the app or clearing its data removes that local progress.

The Parent Zone uses a basic arithmetic gate. It is a usability barrier for this prototype, **not strong authentication or a security boundary**. An adult must review and improve this before use with children or commercial distribution.

## Build a test APK

1. Open **Actions → Build KIDVERSE APK**.
2. Choose **Run workflow** or open the latest push-triggered run.
3. Wait for the build and APK verification steps to finish.
4. Download the `KIDVERSE-debug-apk` artifact from that run.

The debug APK is intended for installation testing. It is not a signed production release.

## Before selling or distributing

Do not sell this MVP as a finished, tested children's product. Before a public or paid release:
- Have the app tested on multiple Android versions and screen sizes.
- Commission an age-appropriate educational and accessibility review.
- Replace the arithmetic Parent Zone gate with a properly designed adult-controlled area.
- Have a qualified adult review child privacy, consent, security, consumer protection, tax and applicable Indian legal requirements.
- Prepare a secure release keystore, sign the release build, protect the keystore and verify update/install behaviour.
- Add a support contact, clear licence/terms and a truthful privacy notice.

No production signing key or passwords belong in this public repository.
