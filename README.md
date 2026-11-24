# KIDVERSE

**Learn · Play · Create · Discover**

KIDVERSE is an offline-first Android learning playground designed for children around ages 6–10. This repository currently contains an early MVP, not a finished commercial release.

## What's in this build

The current content update expands the learning material and adds a repeatable science quiz. Content is designed as short, readable activities for children around ages 6–10; an educator/parent review is still needed before commercial release.

- A redesigned home dashboard with learning-world cards and a local star counter
- Math Kingdom: ten progressive arithmetic and fractions challenges with worked hints and one-time rewards
- Science Galaxy: five topic lessons plus a five-question quiz covering plants, Earth, magnets, water and mammals
- Knowledge Library: 12 built-in bilingual chapters with three teaching sections each, worked examples, key vocabulary, hands-on activities and three explained check questions per lesson
- Knowledge Library reader: searchable full lesson text, estimated reading time, English/Hindi switch, subject labels and a scrollable chapter view
- Downloadable content: expandable offline lesson packs; refreshing a pack replaces older copies instead of leaving stale content
- Story Forest: five original short stories with reading rewards
- Robot City: a simple sequencing activity
- Explorer Earth: short nature and environment lessons
- Creator Studio: finger drawing, colour selection and save-to-device
- Build-a-World: star milestones and unlock progress
- Parent dashboard: local progress view and reset control

## Privacy and storage

The app uses internet only for the optional knowledge-pack download and GitHub release checks. It has no account system, advertising SDK, analytics SDK, location collection or public chat. Stars, activity completion, drawings and downloaded learning packs remain in app-private local storage on the device. Uninstalling the app or clearing its data removes that local progress.

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


## App updates

- The app checks the public GitHub Releases endpoint on startup and offers an update when a newer compatible release is available.
- A parent can also use Parent Zone → Check for app updates / Install downloaded update.
- Android always displays its own installation confirmation. KIDVERSE does not silently install APKs.
- Update APKs must be signed with the same permanent release key as the installed app. Never lose this key.
- To publish a signed update, create GitHub Actions repository secrets named `KIDVERSE_KEYSTORE_BASE64`, `KIDVERSE_KEYSTORE_PASSWORD`, `KIDVERSE_KEY_ALIAS` and `KIDVERSE_KEY_PASSWORD`, then push a version tag such as `v0.2.1`. Keep the keystore and passwords private and under adult/owner control.
- Debug APKs are for testing and are not interchangeable with the stable signed release channel. A production installation should start from the first signed release APK.


## Indian school learning order

The Knowledge Library is organised by Classes 1–5, with class filters and subject/chapter ordering. The current built-in starter curriculum contains 18 lessons: Class 1 (3), Class 2 (3), Class 3 (2), Class 4 (4), and Class 5 (6). Downloadable pack topics are also tagged by class and subject. This is an age-appropriate NCERT/CBSE-aligned learning progression, not an official reproduction of textbook chapter numbering or a board endorsement. Content coverage is still expanding; the lesson count is not a claim that every subject or chapter in Classes 1–5 is complete.

## Offline Smart Tutor (no API)

The tutor runs locally and retrieves explanations, examples and vocabulary from the installed lesson library. It does not call an AI API, require an account, or need internet access for answers. It is a lightweight keyword/retrieval-based study helper, not a generative large-language model; it may not understand every question and should not be presented as a general-purpose AI chatbot. Downloading new lesson packs still requires internet access.

## Real-World Skills

The app includes eight offline practice paths for children around ages 6–10:
- Daily independence and organisation
- Communication, listening and kindness
- Step-by-step problem-solving
- Money basics: needs, wants and simple saving choices
- Digital safety and privacy
- Nature, waste awareness and citizenship
- Creative making, testing and improving
- Naming feelings and asking for support

Each path includes a short decision scenario, a practical mission, small practice steps, a reflection prompt and safety guidance. Children mark steps after trying them; this is a self-reported practice record, not an assessment or proof of mastery. Skill completion is stored locally on the device and shown as a simple count in the Parent dashboard. The activities are supplementary and do not replace school teaching, adult guidance or professional support. Some activities explicitly require an adult.

The skills area supports English and Hindi and works offline. It does not collect a child's name, location, chat messages or other personal information.
