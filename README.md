# Card Form

[![Build Status](https://travis-ci.org/braintree/android-card-form.svg?branch=master)](https://travis-ci.org/braintree/android-card-form)

Card Form is a ready made card form layout that can be included in your app making it easy to
accept credit and debit cards.

## Adding It To Your Project

Add the dependency in your `build.gradle`:

```groovy
dependencies {
    compile 'com.braintreepayments:card-form:3.4.1'
}
```

To use the latest build from the `master` branch use:

 ```groovy
dependencies {
    compile 'com.braintreepayments:card-form:3.4.2-SNAPSHOT'
}
```

## Usage

Card Form is a LinearLayout that you can add to your layout:

```xml
<com.braintreepayments.cardform.view.CardForm
    android:id="@+id/card_form"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

To initialize the view and change which fields are required for the user to enter, use the required
field methods and `CardForm#setup(Activity activity)`.

```java
CardForm cardForm = (CardForm) findViewById(R.id.card_form);
cardForm.cardRequired(true)
        .expirationRequired(true)
        .cvvRequired(true)
        .postalCodeRequired(true)
        .mobileNumberRequired(true)
        .mobileNumberExplanation("SMS is required on this number")
        .actionLabel("Purchase")
        .setup(activity);
```

To access the values in the form, there are getters for each field:

```java
cardForm.getCardNumber();
cardForm.getExpirationMonth();
cardForm.getExpirationYear();
cardForm.getCvv();
cardForm.getPostalCode();
cardForm.getCountryCode();
cardForm.getMobileNumber();
```

To check if `CardForm` is valid call `CardForm#isValid()`. To validate each required field
and show the user which fields are incorrect, call `CardForm#validate()`.

To set custom error messages on a field call `CardForm#setCardNumberError(String)` on the given field.

Additionally `CardForm` has 4 available listeners:

* `CardForm#setOnCardFormValidListener` called when the form changes state from valid to invalid or invalid to valid.
* `CardForm#setOnCardFormSubmitListener` called when the form should be submitted.
* `CardForm#setOnFormFieldFocusedListener` called when a field in the form is focused.
* `CardForm#setOnCardTypeChangedListener` called when the `CardType` in the form changes.

## card.io

The card form is compatible with [card.io](https://github.com/card-io/card.io-Android-SDK).

To use card.io, add the dependency in your `build.gradle`:

```groovy
dependencies {
    compile 'io.card:android-sdk:[5.5.0,6.0.0)'
}
```

Check if card.io is available for use:

```java
cardForm.isCardScanningAvailable();
```

Scan a card:

```java
cardForm.scanCard(activity);
```

## Example

![](/Sample/screenshot.png)

## Styling

The card form uses the [Android Design Support Library](http://android-developers.blogspot.com/2015/05/android-design-support-library.html)
for styling and floating labels. All card form inputs use the `colorAccent` theme attribute, when present,
to set their focused color. For more information on the `colorAccent` attribute, see
[Using the Material Theme](https://developer.android.com/training/material/theme.html). Additional
styling, such as the error color (`textErrorColor`) can be set in your theme and will be picked up
by the card form.

The included [sample app](https://github.com/braintree/android-card-form/tree/master/Sample) has
examples with a light theme and dark theme.

**Note:** Any `Activity` using the card form must use a style that is a Theme.AppCompat theme or
descendant (defines `android.support.v7.appcompat.R.attr.colorPrimary`). This is a requirement of
the [Android Design Support Library](http://android-developers.blogspot.com/2015/05/android-design-support-library.html).
If this is a problem in your usage of the card form, please [file an issue](https://github.com/braintree/android-card-form/issues)
and we will look further into workarounds for this.

## [Releases](https://github.com/braintree/android-card-form/releases)

## License

Card Form is open source and available under the MIT license. See the [LICENSE](LICENSE) file for
more info.
