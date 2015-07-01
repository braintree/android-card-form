# Android Card Form Release Notes

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
