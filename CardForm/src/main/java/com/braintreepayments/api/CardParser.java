package com.braintreepayments.api;

import android.text.TextUtils;

public class CardParser {

    /**
     * Returns the card type matching this account, or {@link CardType#UNKNOWN}
     * for no match.
     * <p/>
     * A partial account type may be given, with the caveat that it may not have enough digits to
     * match.
     */
    CardType parseCardNumber(String cardNumber) {
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

    private static CardType forCardNumberRelaxedPrefixPattern(String cardNumber) {
        for (CardType cardType : CardType.values()) {
            CardAttributes cardAttributes = CardAttributes.forCardType(cardType);
            if (cardAttributes.matchesRelaxed(cardNumber)) {
                return cardType;
            }
        }

        return CardType.EMPTY;
    }

    private static CardType forCardNumberPattern(String cardNumber) {
        for (CardType cardType : CardType.values()) {
            CardAttributes cardAttributes = CardAttributes.forCardType(cardType);
            if (cardAttributes.matchesStrict(cardNumber)) {
                return cardType;
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
     * Character#isDefined(char)} is {@code false}).
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

        final int numberLength = cardNumber.length();
        CardType cardType = parseCardNumber(cardNumber);
        CardAttributes cardAttributes = CardAttributes.forCardType(cardType);

        int minCardLength = cardAttributes.getMinCardLength();
        int maxCardLength = cardAttributes.getMaxCardLength();
        if (numberLength < minCardLength || numberLength > maxCardLength) {
            return false;
        } else if (!cardAttributes.matches(cardNumber)) {
            return false;
        }
        return isLuhnValid(cardNumber);
    }
}
