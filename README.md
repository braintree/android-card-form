# Card Form

Card Form is a ready made card form layout that can be included in your app making it easy to
accept credit and debit cards.

[![Build Status](https://travis-ci.org/braintree/android-card-form.svg?branch=master)](https://travis-ci.org/braintree/android-card-form)

## Adding It To Your Project

In your `build.gradle`:

```groovy
dependencies {
    compile project('com.braintreepayments:card-form:2.0.0')
}
```

## Usage

Card Form is a LinearLayout that you can add to your layout:

```xml
<com.braintreepayments.cardform.view.CardForm
    android:id="@+id/bt_card_form"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

To initialize the view and change which fields are required for the user to enter, use
`CardForm#setRequiredFields(Activity activity, boolean cardNumberRequired, boolean expirationRequired, boolean cvvRequired, boolean postalCodeRequired, String imeActionLabel)`.

```java
CardForm cardForm = (CardForm) findViewById(R.id.bt_card_form);
cardForm.setRequiredFields(Activity.this, true, true, false, false, "Purchase");
```

To access the values in the form, there are getters for each field:

```java
cardForm.getCardNumber();
cardForm.getExpirationMonth();
cardForm.getExpirationYear();
cardForm.getCvv();
cardForm.getPostalCode();
```

To check if `CardForm` is valid call `CardForm#isValid()`. To validate each required field
and show the user which fields are incorrect, call `CardForm#validate()`.

Additionally `CardForm` has 3 available listeners:

* `CardForm#setOnCardFormValidListener` called when the form changes state from valid to invalid or invalid to valid.
* `CardForm#setOnCardFormSubmitListener` called when the form should be submitted.
* `CardForm#setOnFormFieldFocusedListener` called when a field in the form is focused.

## Styling

All card form inputs use the `colorPrimary` theme attribute, when present, to set their focused color.
For more information on the `colorPrimary` attribute, see [Using the Material Theme](https://developer.android.com/training/material/theme.html).

The included [sample app](https://github.com/braintree/android-card-form/tree/master/Sample) has examples of a Holo theme, Material light theme and Material dark theme.

## [Change Log](CHANGELOG.md)

## License

Card Form is open source and available under the MIT license. See the [LICENSE](LICENSE) file for more info.
