package com.braintreepayments.api;

import android.text.TextUtils;

import com.braintreepayments.api.cardform.R;

import java.util.regex.Pattern;

/**
 * Card types and related formatting and validation rules.
 */
public enum CardType {

    VISA,
    MASTERCARD,
    DISCOVER,
    AMEX,
    DINERS_CLUB,
    JCB,
    MAESTRO,
    UNIONPAY,
    HIPER,
    HIPERCARD,
    UNKNOWN,
    EMPTY;
}
