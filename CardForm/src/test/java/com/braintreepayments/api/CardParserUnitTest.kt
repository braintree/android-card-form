package com.braintreepayments.api

import org.junit.Assert.*
import org.junit.Test

class CardParserUnitTest {

    private val sampleCards: Map<String, CardType> = mapOf(
        // Visa
        "4111111111111111" to CardType.VISA,
        "4005519200000004" to CardType.VISA,
        "4009348888881881" to CardType.VISA,
        "4012000033330026" to CardType.VISA,
        "4012000077777777" to CardType.VISA,
        "4012888888881881" to CardType.VISA,
        "4217651111111119" to CardType.VISA,
        "4500600000000061" to CardType.VISA,

        // Mastercard
        "5555555555554444" to CardType.MASTERCARD,
        "5105105105105100" to CardType.MASTERCARD,
        "2221000000000009" to CardType.MASTERCARD,
        "2223000048400011" to CardType.MASTERCARD,
        "2230000000000008" to CardType.MASTERCARD,
        "2300000000000003" to CardType.MASTERCARD,
        "2500000000000001" to CardType.MASTERCARD,
        "2600000000000000" to CardType.MASTERCARD,
        "2700000000000009" to CardType.MASTERCARD,
        "2720990000000007" to CardType.MASTERCARD,

        // Discover
        "6011111111111117" to CardType.DISCOVER,
        "6011000990139424" to CardType.DISCOVER,
        "6500000000000000003" to CardType.DISCOVER,

        // Amex
        "378282246310005" to CardType.AMEX,
        "371449635398431" to CardType.AMEX,

        // Diner's club
        "30000000000004" to CardType.DINERS_CLUB,

        // JCB
        "3530111333300000" to CardType.JCB,
        "3566002020360505" to CardType.JCB,

        // Maestro
        "5018000000000009" to CardType.MAESTRO,
        "5018000000000000122" to CardType.MAESTRO,
        "6703000000000007" to CardType.MAESTRO,
        "6020111111111116" to CardType.MAESTRO,
        "6764111111111116" to CardType.MAESTRO,
        "560000000003" to CardType.MAESTRO,
        "5600000000000000002" to CardType.MAESTRO,
        "570000000002" to CardType.MAESTRO,
        "5700000000000000018" to CardType.MAESTRO,
        "580000000001" to CardType.MAESTRO,
        "5800000000000000008" to CardType.MAESTRO,
        "590000000000" to CardType.MAESTRO,
        "5900000000000000006" to CardType.MAESTRO,
        "5043111111111111" to CardType.MAESTRO,

        // Union Pay
        "6240888888888885" to CardType.UNIONPAY,
        "6240888888888885127" to CardType.UNIONPAY,

        // Hiper
        "6370950000000005" to CardType.HIPER,
        "6375680000000003" to CardType.HIPER,
        "6375990000000006" to CardType.HIPER,
        "6376090000000004" to CardType.HIPER,
        "6376120000000009" to CardType.HIPER,

        // Hipercard
        "6062820524845321" to CardType.HIPERCARD,

        // Unknown
        "2721000000000004" to CardType.UNKNOWN,
        "1" to CardType.UNKNOWN,

        // Empty
        "" to CardType.EMPTY,
    )

    @Test
    fun sampleCardsAreLuhnValid() {
        val sut = CardParser()
        for ((cardNumber, cardType) in sampleCards) {
            val actualType: CardType = sut.parseCardNumber(cardNumber)
            assertEquals(
                String.format("CardType.forAccountNumber failed for %s", cardNumber),
                cardType,
                actualType
            )
            if (cardType != CardType.UNKNOWN && cardType != CardType.EMPTY) {
                assertTrue(
                    String.format("%s: Luhn check failed for [%s]", cardType, cardNumber),
                    sut.isLuhnValid(cardNumber)
                )
            }
        }
    }

    @Test
    fun validateSampleCards() {
        val sut = CardParser()
        for ((cardNumber, cardType) in sampleCards) {
            val actualType: CardType = sut.parseCardNumber(cardNumber)
            assertEquals(
                String.format("CardType.forAccountNumber failed for %s", cardNumber),
                cardType,
                actualType
            )
            if (cardType != CardType.UNKNOWN && cardType != CardType.EMPTY) {
                assertTrue(
                    String.format("%s: Validate check failed for [%s]", cardType, cardNumber),
                    sut.validate(cardNumber)
                )
            }
        }
    }

    @Test
    fun validate_whenGivenNonDigits_returnsFalse() {
        val sut = CardParser()
        assertFalse(sut.validate(""))
        assertFalse(sut.validate("Not-A-Number"))
        assertFalse(sut.validate("@#$%^&"))
    }

    @Test
    fun validate_whenPatternFailsAndNoRelaxedPatternExists_returnsFalse() {
        val sut = CardParser()
        assertFalse(sut.validate("9999999999999999"))
    }
}