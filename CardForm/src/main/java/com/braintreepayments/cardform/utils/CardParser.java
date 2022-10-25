package com.braintreepayments.cardform.utils;

import android.text.TextUtils;

import com.braintreepayments.api.CardType;
import com.braintreepayments.cardform.R;

import java.util.HashMap;
import java.util.Map;

public class CardParser {

    private Map<CardType, CardDescriptor> cardDescriptors;

    public CardParser() {
        cardDescriptors = new HashMap<>();

        cardDescriptors.put(CardType.VISA,
                new CardDescriptor(CardType.VISA, "^4\\d*", R.drawable.bt_ic_visa, 16, 16, 3, R.string.bt_cvv, null)
        );

        cardDescriptors.put(CardType.MASTERCARD,
                new CardDescriptor(CardType.MASTERCARD, "^(5[1-5]|222[1-9]|22[3-9]|2[3-6]|27[0-1]|2720)\\d*", R.drawable.bt_ic_mastercard, 16, 16, 3, R.string.bt_cvc, null)
        );

        cardDescriptors.put(CardType.DISCOVER,
                new CardDescriptor(CardType.DISCOVER, "^(6011|65|64[4-9]|622)\\d*", R.drawable.bt_ic_discover, 16, 19, 3, R.string.bt_cid, null)
        );

        cardDescriptors.put(CardType.AMEX,
                new CardDescriptor(CardType.AMEX, "^3[47]\\d*", R.drawable.bt_ic_amex, 15, 15, 4, R.string.bt_cid, null)
        );

        cardDescriptors.put(CardType.DINERS_CLUB,
                new CardDescriptor(CardType.DINERS_CLUB, "^(36|38|30[0-5])\\d*", R.drawable.bt_ic_diners_club, 14, 14, 3, R.string.bt_cvv, null)
        );

        cardDescriptors.put(CardType.JCB,
                new CardDescriptor(CardType.JCB, "^35\\d*", R.drawable.bt_ic_jcb, 16, 16, 3, R.string.bt_cvv, null)
        );

        cardDescriptors.put(CardType.MAESTRO,
                new CardDescriptor(CardType.MAESTRO, "^(5018|5020|5038|5043|5[6-9]|6020|6304|6703|6759|676[1-3])\\d*", R.drawable.bt_ic_maestro, 12, 19, 3, R.string.bt_cvc, "^6\\d*")
        );

        cardDescriptors.put(CardType.UNIONPAY,
                new CardDescriptor(CardType.UNIONPAY, "^62\\d*", R.drawable.bt_ic_unionpay, 16, 19, 3, R.string.bt_cvn, null)
        );

        cardDescriptors.put(CardType.HIPER,
                new CardDescriptor(CardType.HIPER, "^637(095|568|599|609|612)\\d*", R.drawable.bt_ic_hiper, 16, 16, 3, R.string.bt_cvc, null)
        );

        cardDescriptors.put(CardType.HIPERCARD,
                new CardDescriptor(CardType.HIPERCARD, "^606282\\d*", R.drawable.bt_ic_hipercard, 16, 16, 3, R.string.bt_cvc, null)
        );

        cardDescriptors.put(CardType.UNKNOWN,
                new CardDescriptor(CardType.UNKNOWN, "\\d+", R.drawable.bt_ic_unknown, 12, 19, 3, R.string.bt_cvv, null)
        );

        cardDescriptors.put(CardType.EMPTY,
                new CardDescriptor(CardType.EMPTY, "^$", R.drawable.bt_ic_unknown, 12, 19, 3, R.string.bt_cvv, null)
        );
    }

    /**
     * Returns the card type matching this account, or {@link com.braintreepayments.cardform.utils.CardType#UNKNOWN}
     * for no match.
     * <p/>
     * A partial account type may be given, with the caveat that it may not have enough digits to
     * match.
     */
    public CardType parseCard(String cardNumber) {
        CardType patternMatch = forCardNumberPattern(cardNumber);
        if (patternMatch != CardType.EMPTY && patternMatch != CardType.UNKNOWN) {
            return patternMatch;
        }

        CardType relaxedPrefixPatternMatch = forCardNumberRelaxedPrefixPattern(cardNumber);
        if (relaxedPrefixPatternMatch != CardType.EMPTY && relaxedPrefixPatternMatch != CardType.UNKNOWN) {
            return relaxedPrefixPatternMatch;
        }

        if (!cardNumber.isEmpty()) {
            return CardType.UNKNOWN;
        }

        return CardType.EMPTY;
    }

    private CardType forCardNumberPattern(String cardNumber) {
        for (CardDescriptor descriptor : cardDescriptors.values()) {
            if (descriptor.getPattern().matcher(cardNumber).matches()) {
                return descriptor.getType();
            }
        }

        return CardType.EMPTY;
    }

    private CardType forCardNumberRelaxedPrefixPattern(String cardNumber) {
        for (CardDescriptor descriptor : cardDescriptors.values()) {
            if (descriptor.getRelaxedPrefixPattern() != null) {
                if (descriptor.getRelaxedPrefixPattern().matcher(cardNumber).matches()) {
                    return descriptor.getType();
                }
            }
        }

        return CardType.EMPTY;
    }

    /**
     * Performs the Luhn check on the given card number.
     *
     * @param cardNumber a String consisting of numeric digits (only).
     * @return {@code true} if the sequence passes the checksum
     * @throws IllegalArgumentException if {@code cardNumber} contained a non-digit (where {@link
     *                                  Character#isDefined(char)} is {@code false}).
     * @see <a href="http://en.wikipedia.org/wiki/Luhn_algorithm">Luhn Algorithm (Wikipedia)</a>
     */
    public boolean isLuhnValid(String cardNumber) {
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
        } else if (!TextUtils.isDigitsOnly(cardNumber)) {
            return false;
        }

        CardType cardType = parseCard(cardNumber);
        CardDescriptor cardDescriptor = cardDescriptors.get(cardType);
        if (cardDescriptor == null) {
            return false;
        }

        final int numberLength = cardNumber.length();
        if (numberLength < cardDescriptor.getMinCardLength() || numberLength > cardDescriptor.getMaxCardLength()) {
            return false;
        } else if (!cardDescriptor.getPattern().matcher(cardNumber).matches() && cardDescriptor.getRelaxedPrefixPattern() != null && !cardDescriptor.getRelaxedPrefixPattern().matcher(cardNumber).matches()) {
            return false;
        }
        return isLuhnValid(cardNumber);
    }

    public CardDescriptor getDescriptor(CardType cardType) {
        return cardDescriptors.get(cardType);
    }
}
