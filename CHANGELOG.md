# Android Card Form Release Notes

## 5.1.0
  * Bump `compileSdkVersion` and `targetSdkVersion` to API level 30
  * Add support for Maestro cards beginning in 5043
  * Update Visa card icons (fixes #103)
  
## 5.0.0

* Breaking Changes
  * Remove CardIO
  * Remove `ExpirationDateEditText::useDialogForExpirationDateEntry()`
  * Remove `ExpirationDateDialog` and default to numeric keyboard for expiration date field
  * Update CVV keyboard to be numeric only

## 4.3.0

* Fix autofill issue that causes multiple slashes to be displayed in the expiration date field (resolves #88)
* Update expiration date keyboard to match the light or dark theme, instead of be inverted
* Add setter for cardholder name icon (thanks @tperraut, resolves #78)
* Update payment option icons

## 4.2.0

* Add save card CheckBox

## 4.1.0

* Add support for Hiper and Hipercard card identification

## 4.0.0

* Convert to AndroidX

## 3.5.1

* Fix NullPointerException in CardType#validate
  - CardType#validate restricted to only accept strings with digits

## 3.5.0

* Bump to Android SDK 28
* Add Cardholder Name as an optional field to CardForm
* Fix NPE when returning from CardIO

## 3.4.1

* Fix Maestro card detection in CardType.validate

## 3.4.0

* Update translations
* Improve Maestro card detection

## 3.3.0

* Add optional input masking to CardEditText and CvvEditText.

## 3.2.0

* Update Android SDK and build tools

## 3.1.1

* Fix improperly formatted expiration date when scanning with card.io
* Suppress card number confirmation in card.io
* Update MasterCard logo
* Update Maestro logo

## 3.1.0

* Update compile and target SDK version to 26
* Upgrade design support library to 26.0.0

## 3.0.6

* Fix possible BadTokenException (#26)
* Remove design support library from consumer ProGuard file (#29)
* Change design support library dependency to oldest supported version instead of dependency range (#29)

## 3.0.5

* Prevent dependency resolution of alpha major versions of the design support library

## 3.0.4

* Fix error in visual layout editor (fixes #24)
* Increase minSdkVersion to 16
* Increase card.io minimum version to 5.5.0
* Update ExpirationDateDialog maximum expiration years (fixes #25)

## 3.0.3

* Allow overriding of form icons (Fixes #22)
* Fix a bug where ExpirationDateDialog was not shown in certain devices

## 3.0.2

* Update compile and target API versions to 25
* Update translations
  - android-card-form is now available in 23 languages: ar, da, de, en, es, fr-rCA, fr, in, it, iw, ja, ko, nb, nl, pl, pt, ru, sv, th, tr, zh-rCN, zh-rHK, zh-rTW.
* Improve layout for right to left languages
* Fix text input for right to left languages
* Improve layout on small screens (fixes #16)
* Add Maestro card bin 6020 for card type detection

## 3.0.1

* Fix crash on API 15 and 16 (Fixes #14)

## 3.0.0

* Target API 24
* Add SupportedCardTypesView.
* Highlight current card type in SupportedCardTypesView
* Add option to disable card type hint in CardEditText
* Switch to design support library for float labels instead of custom implementation (CardForm can only be used with an appcompat theme)
* Change CVV hint based on card type
* Update assets
* Add field icons
* Combine expiration and cvv on a single line when both present
* Refactor setting required fields into chainable methods
* Add country code field
* Add mobile number field
* Add CardType.EMPTY type
* Allow fields to be marked as optional to prevent validation and errors

## 2.3.2

* Add support for MasterCard 2 series bin range
* Include library proguard file to prevent ProGuard exceptions (fixes #11)
* Add additional bin ranges for Maestro cards

## 2.3.1

* Prevent CardForm#setRequiredFields from reseting ExpirationDateDialog use and invalidating the expiration field

## 2.3.0

* Add optional support for card.io (fixes #5)
* Fix Activity leak in ExpirationDateDialogTheme
* Fix inconsistent state when calling CardForm#setRequiredFields multiple times

## 2.2.0

* Add error animations
* Add vibration for errors if vibration permission is present
* Add CardForm#setOnCardTypeChangedListener
* Rename MonthYearEditText to ExpirationDateEditText
* Remove CardUtils class and move Luhn validation to CardType class
* Add custom dialog for entering expiration dates
  - Enabled by default, can be disabled with CardForm#useDialogForExpirationDateEntry(activity, false)
* Update gradle build tools to 2.1.0
* Compress assets to reduce the library size by 27kb
* Fixes
  - Prevent expiration field from auto advancing if invalid
  - Fix maximum card length for unknown card types
  - Allow multiple calls to CardForm#setRequiredFields

## 2.1.1

* Fixes
  - Fix 4 digit year validation bug. Fixes #3
  - Return invalid for years too far into the future. Fixes #3

## 2.1.0

* Update minSdkVersion to 15
* Update compileSdkVersion to 23
* Update targetSdkVersion to 23
* Upgrade gradle build tools to 1.3.1
* Upgrade buildToolsVersion to 23.0.1
* Upgrade sample app support library to 23.0.1

## 2.0.1

* Fixes
  * Use `colorAccent` instead of `colorPrimary` for the focused color of `EditText`s. Fixes [#1](https://github.com/braintree/android-card-form/issues/1).

## 2.0.0

* Material design updates
* Prevent screenshots of the card form using `FLAG_SECURE`
* Add sample app
* Update gradle build tools to 1.2.3
* Breaking Changes
  * `CardForm#setRequriedFields` now requires an `Activity` and is no longer optional. Failure to call `CardForm#setRequiredFields` will result in an invisible form.
  * Remove methods to save form instance state, instance state is automatically saved
* Fixes
  * Strip metadata from assets
  * Fix crash on API 10 caused by `View#focuseSearch`

## 1.4.0

* Fixes
  * Fix missing expiration date float label ([issue](https://github.com/braintree/braintree_android/issues/21))


## 1.3.0

* Initial release
