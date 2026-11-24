# KIDVERSE release readiness

**Status: development MVP — not yet approved for commercial release.**

This checklist tracks product, security, content and distribution work. A successful CI build only proves that the source compiles and the configured checks pass; it does not prove that the app is safe, legally compliant, usable by children or ready to sell.

## Work already in the repository

- Native Android Java application; no WebView.
- Offline lessons, stories, drawing, maths/science activities, a keyword-based offline lesson helper and eight real-world skill paths.
- English and Hindi lesson/skill content.
- Learning progress, saved drawings and downloaded lesson packs are stored locally.
- No account system, advertising SDK, analytics SDK, public chat or location permission is currently included.
- The app's manual update check is available from the Parent Zone; it is not run automatically at launch.
- Downloaded lesson packs are size-limited and checked for basic schema and quiz-index errors before being saved.
- CI validates the lesson JSON before producing a test APK.

## Required before selling

### Product and content
- [ ] A qualified primary-school educator reviews every lesson, worked example, answer key, Hindi translation and activity for accuracy, age fit and cultural clarity.
- [ ] Review the full Class 1–5 scope with an educator. The current 18 built-in topics and 18 downloadable topics are a starter collection, not complete coverage of the Indian school curriculum and not an official NCERT/CBSE product.
- [ ] Test reading level, tap targets, contrast, font scaling, TalkBack labels, orientation, small screens and low-memory phones.
- [ ] Test the app with parent/guardian oversight on multiple real Android devices and Android versions, including offline use, download interruption, low storage, update interruption and uninstall/reinstall.
- [ ] Check that activities never ask children to handle dangerous objects, reveal personal information, meet online contacts, make purchases or use financial accounts without an adult.
- [ ] Verify ownership/licences for every story, illustration, font, icon, sound, sample and lesson. Keep written permissions and attribution records where needed.
- [ ] Add a version history, owner-controlled support contact, sales description, end-user licence terms, refund/cancellation process and a way for buyers to report defects.

### Child privacy and safety
- [ ] Have an India-qualified privacy/legal professional review the app and the actual sales/distribution process under the Digital Personal Data Protection Act, 2023, the DPDP Rules, 2025 and any other applicable laws. The Act and Rules have phased commencement dates; do not assume every provision is already in force or that a local-only design removes every legal duty.
- [ ] Publish a completed privacy notice at a stable public URL. Start from `PRIVACY_NOTICE_DRAFT.md`, replace every placeholder and make sure the published wording matches the final binary and all third-party services.
- [ ] Give parents clear pre-purchase information about the app, offline/online functions, update mechanism, age range and limits of the learning content.
- [ ] Do not add cloud AI, analytics, ads, social/chat, child profiles, voice recording or other data collection without a new privacy, safety and legal review.
- [ ] Replace or professionally review the simple arithmetic Parent Zone check before relying on it for sensitive controls. It is a usability barrier, not strong authentication.
- [ ] Test data reset and deletion on a real device. Current reset clears stars, activity rewards and skill-practice marks; it intentionally keeps saved drawings and downloaded lesson packs. Android app-data clearing/uninstall removes app-private files.

### Release engineering
- [ ] Create one permanent release keystore on an owner-controlled computer. Keep at least two secure backups. Never upload the keystore to this repository, an issue, a chat or a public drive.
- [ ] Add GitHub Actions secrets: `KIDVERSE_KEYSTORE_BASE64`, `KIDVERSE_KEYSTORE_PASSWORD`, `KIDVERSE_KEY_ALIAS`, and `KIDVERSE_KEY_PASSWORD`.
- [ ] Build a signed release APK from a version tag such as `v1.0.0`. Confirm the signature with Android's `apksigner` and install/upgrade it on a clean test device.
- [ ] Keep the same application ID (`com.kidverse.app`) and the same signing key for every future direct-APK update. Losing/changing the key prevents normal in-place updates.
- [ ] Keep debug APKs for testing only. Do not sell or distribute them as the production release.
- [ ] Publish checksums and release notes. Test the actual download and parent-initiated installation process on Android 8+ devices.
- [ ] Decide the supported distribution channel. The current manifest includes `REQUEST_INSTALL_PACKAGES` for the direct-APK self-update flow. Google Play policy prohibits using that permission for ordinary self-updates; a Play build must use Play's update mechanism and remove that permission/update path unless the app independently qualifies for an allowed use. See the official policy linked below.

### Business and launch
- [ ] Confirm the business/owner identity, applicable registrations, taxes, payment provider terms, invoice/receipt process, support response process and refund policy with a qualified adult/professional.
- [ ] Set a sustainable price and state exactly what is included. Do not promise cloud AI, a complete syllabus, teacher certification, unlimited content or guaranteed learning outcomes unless these are actually delivered and funded.
- [ ] Conduct a closed pilot with parent/guardian permission, collect feedback without collecting children's personal details, fix issues and repeat regression tests before a public launch.

## Permanent release signing setup

Generate the keystore locally, under adult/owner control, using a current JDK:

```bash
keytool -genkeypair -v -keystore kidverse-release.jks -alias kidverse -keyalg RSA -keysize 3072 -validity 10000
```

Convert the keystore to Base64 locally and put the Base64 text in the GitHub Actions secret named `KIDVERSE_KEYSTORE_BASE64`. Add the other three password/alias values as separate GitHub Actions secrets. Never commit the keystore or secret values. Keep the original keystore and backup offline and secure.

The tag-triggered release workflow validates the content, builds a release APK, verifies the APK signature and publishes the APK plus SHA-256 checksum to GitHub Releases. It will fail safely when signing secrets are missing.

## Useful references

- India DPDP Rules, 2025 — [Official Gazette notification](https://egazette.gov.in/WriteReadData/2025/267650.pdf?v=20261014102521)
- DPDP Act commencement notification — [India Code summary of the Gazette notification](https://indiacode.ecourtsindia.com/rules/enforcement-timeline-for-the-dpdp-act-e1be0965/)
- Google Play Families policy — [Official policy](https://support.google.com/googleplay/android-developer/answer/9893335)
- Google Play `REQUEST_INSTALL_PACKAGES` policy — [Official policy](https://support.google.com/googleplay/android-developer/answer/12085295)
- Android app signing — [Official Android documentation](https://developer.android.com/studio/publish/app-signing)

**Do not mark this document complete merely because the APK builds.**
