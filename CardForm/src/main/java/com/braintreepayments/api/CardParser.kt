package com.braintreepayments.api

import android.text.TextUtils

class CardParser {

    private val undetectableCardTypes = setOf(CardType.EMPTY, CardType.UNKNOWN)
    private val detectableCardAttributes =
        (CardType.values().toSet() - undetectableCardTypes).map { CardAttributes.forCardType(it) }

    /**
     * Returns the card type matching this account, or [CardType.UNKNOWN] for no match.
     *
     * A partial account type may be given, with the caveat that it may not have enough digits to match.
     */
    fun parseCardType(cardNumber: String): CardType {
        val cardAttributes =
            findStrictCardTypeMatch(cardNumber) ?: findRelaxedCardTypeMatch(cardNumber)

        return cardAttributes?.cardType
            ?: (if (cardNumber.isEmpty()) CardType.EMPTY else CardType.UNKNOWN)
    }

    private fun findStrictCardTypeMatch(cardNumber: String) =
        detectableCardAttributes.find { it.matchesStrict(cardNumber) }

    private fun findRelaxedCardTypeMatch(cardNumber: String) =
        detectableCardAttributes.find { it.matchesRelaxed(cardNumber) }

    /**
     * Performs the Luhn check on the given card number.
     *
     * @param cardNumber a String consisting of numeric digits (only).
     * @return `true` if the sequence passes the checksum
     * @throws IllegalArgumentException if `cardNumber` contained a non-digit (where [Character.isDigit] is `false`).
     * @see [Luhn Algorithm](http://en.wikipedia.org/wiki/Luhn_algorithm)
     */
    fun isLuhnValid(cardNumber: String?): Boolean {
        val reversed = (cardNumber ?: "").reversed()
        val len = reversed.length
        var oddSum = 0
        var evenSum = 0
        for (i in 0 until len) {
            val c = reversed[i]
            require(Character.isDigit(c)) { String.format("Not a digit: '%s'", c) }
            val digit = c.digitToIntOrNull() ?: -1
            if (i % 2 == 0) {
                oddSum += digit
            } else {
                evenSum += digit / 5 + 2 * digit % 10
            }
        }
        return (oddSum + evenSum) % 10 == 0
    }

    /**
     * @param cardNumber The card number to validate.
     * @return `true` if this card number is locally valid.
     */
    fun validate(cardNumber: String): Boolean {
        if (TextUtils.isEmpty(cardNumber) || !TextUtils.isDigitsOnly(cardNumber)) {
            return false
        }
        val cardType = parseCardType(cardNumber)
        val cardAttributes = CardAttributes.forCardType(cardType)

        val isValidLength = cardAttributes.run {
            (minCardLength..maxCardLength).contains(cardNumber.length)
        }
        return isValidLength && cardAttributes.matches(cardNumber) && isLuhnValid(cardNumber)
    }
}