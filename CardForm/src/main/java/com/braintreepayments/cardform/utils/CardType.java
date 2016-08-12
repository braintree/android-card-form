package com.braintreepayments.cardform.utils;

import android.text.TextUtils;

import com.braintreepayments.cardform.R;

import java.util.regex.Pattern;

/**
 * Card types and related formatting and validation rules.
 */
public enum CardType {

    VISA("^4\\d*",
            R.drawable.bt_ic_visa,
            16, 16,
            3, R.string.bt_cvv),
    MASTERCARD("^(5[1-5]|222[1-9]|22[3-9]|2[3-6]|27[0-1]|2720)\\d*",
            R.drawable.bt_ic_mastercard,
            16, 16,
            3, R.string.bt_cvc),
    DISCOVER("^(6011|65|64[4-9]|622)\\d*",
            R.drawable.bt_ic_discover,
            16, 16,
            3, R.string.bt_cid),
    AMEX("^3[47]\\d*",
            R.drawable.bt_ic_amex,
            15, 15,
            4, R.string.bt_cid),
    DINERS_CLUB("^(36|38|30[0-5])\\d*",
            R.drawable.bt_ic_diners_club,
            14, 14,
            3, R.string.bt_cvv),
    JCB("^35\\d*",
            R.drawable.bt_ic_jcb,
            16, 16,
            3, R.string.bt_cvv),
    MAESTRO("^(5018|5020|5038|6304|6703|6759|676[1-3])\\d*",
            R.drawable.bt_ic_maestro,
            12, 19,
            3, R.string.bt_cvc),
    UNIONPAY("^62\\d*",
            R.drawable.bt_ic_unionpay,
            16, 19,
            3, R.string.bt_cvn),
    UNKNOWN("\\d+",
            R.drawable.bt_ic_unknown,
            12, 19,
            3, R.string.bt_cvv),
    EMPTY("^$",
            R.drawable.bt_ic_unknown,
            12, 19,
            3, R.string.bt_cvv);

    private static final int[] AMEX_SPACE_INDICES = { 4, 10 };
    private static final int[] DEFAULT_SPACE_INDICES = { 4, 8, 12 };

    private final Pattern mPattern;
    private final int mFrontResource;
    private final int mMinCardLength;
    private final int mMaxCardLength;
    private final int mSecurityCodeLength;
    private final int mSecurityCodeName;

    CardType(String regex, int frontResource, int minCardLength, int maxCardLength, int securityCodeLength,
             int securityCodeName) {
        mPattern = Pattern.compile(regex);
        mFrontResource = frontResource;
        mMinCardLength = minCardLength;
        mMaxCardLength = maxCardLength;
        mSecurityCodeLength = securityCodeLength;
        mSecurityCodeName = securityCodeName;
    }

    /**
     * Returns the card type matching this account, or {@link com.braintreepayments.cardform.utils.CardType#UNKNOWN}
     * for no match.
     * <p/>
     * A partial account type may be given, with the caveat that it may not have enough digits to
     * match.
     */
    public static CardType forCardNumber(String cardNumber) {
        for (CardType cardType : values()) {
            if (cardType.getPattern().matcher(cardNumber).matches()) {
                return cardType;
            }
        }
        return EMPTY;
    }

    /**
     * @return The regex matching this card type.
     */
    public Pattern getPattern() {
        return mPattern;
    }

    /**
     * @return The android resource id for the front card image, highlighting card number format.
     */
    public int getFrontResource() {
        return mFrontResource;
    }

    /**
     * @return The android resource id for the security code name for this card type.
     */
    public int getSecurityCodeName() {
        return mSecurityCodeName;
    }

    /**
     * @return The length of the current card's security code.
     */
    public int getSecurityCodeLength() {
        return mSecurityCodeLength;
    }

    /**
     * @return minimum length of a card for this {@link com.braintreepayments.cardform.utils.CardType}
     */
    public int getMinCardLength() {
        return mMinCardLength;
    }

    /**
     * @return max length of a card for this {@link com.braintreepayments.cardform.utils.CardType}
     */
    public int getMaxCardLength() {
        return mMaxCardLength;
    }

    /**
     * @return the locations where spaces should be inserted when formatting the card in a user
     * friendly way. Only for display purposes.
     */
    public int[] getSpaceIndices() {
        return this == AMEX ? AMEX_SPACE_INDICES : DEFAULT_SPACE_INDICES;
    }

    /**
     * @param cardNumber the card number to check
     * @return {@code true} if cardNumber is a legal length, {@code false} if not
     */
    public boolean isLegalCardLength(String cardNumber) {
        final int len = cardNumber.length();
        return len >= mMinCardLength && len <= mMaxCardLength;
    }

    /**
     * Performs the Luhn check on the given card number.
     *
     * @param cardNumber a String consisting of numeric digits (only).
     * @return {@code true} if the sequence passes the checksum
     * @throws IllegalArgumentException if {@code cardNumber} contained a non-digit (where {@link
     * Character#isDefined(char)} is {@code false}).
     * @see <a href="http://en.wikipedia.org/wiki/Luhn_algorithm">Luhn Algorithm (Wikipedia)</a>
     */
    public static boolean isLuhnValid(String cardNumber) {
        final String reversed = new StringBuffer(cardNumber).reverse().toString();
        final int len = reversed.length();
        int oddSum = 0;
        int evenSum = 0;
        for (int i = 0; i < len; i++) {
            final char c = reversed.charAt(i);
            if (!Character.isDigit(c)) {
                throw new IllegalArgumentException(String.format("Not a digit: '%s'", c));
            }
            final int digit = Character.digit(c, 10);
            if (i % 2 == 0) {
                oddSum += digit;
            } else {
                evenSum += digit / 5 + (2 * digit) % 10;
            }
        }
        return (oddSum + evenSum) % 10 == 0;
    }

    /**
     * @param cardNumber The card number to validate.
     * @return {@code true} if this card number is locally valid.
     */
    public boolean validate(String cardNumber) {
        if (TextUtils.isEmpty(cardNumber)) {
            return false;
        }

        final int numberLength = cardNumber.length();
        if (numberLength < mMinCardLength || numberLength > mMaxCardLength) {
            return false;
        } else if (!mPattern.matcher(cardNumber).matches()) {
            return false;
        }
        return isLuhnValid(cardNumber);
    }
}