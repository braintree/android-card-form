package com.braintreepayments.api

import org.junit.Assert.assertTrue
import org.junit.Test

// TODO: Consider refactoring and converting to a parameterized test to get better coverage
// since each card brand has it's own cvv requirements, etc.
class CardAttributesUnitTest {

    companion object {
        private const val MIN_MIN_CARD_LENGTH = 12
        private const val MAX_MAX_CARD_LENGTH = 19

        private const val MIN_SECURITY_CODE_LENGTH = 3
        private const val MAX_SECURITY_CODE_LENGTH = 4
    }

    @Test
    fun allParametersSane() {
        for (cardType in CardType.values()) {
            val attributes = CardAttributes.forCardType(cardType)

            attributes.apply {
                assertTrue(
                    String.format("%s: Min card length %s too small", cardType, minCardLength),
                    minCardLength >= MIN_MIN_CARD_LENGTH
                )
                assertTrue(
                    String.format("%s: Max card length %s too large", cardType, maxCardLength),
                    maxCardLength <= MAX_MAX_CARD_LENGTH
                )
                assertTrue(
                    String.format(
                        "%s: Min card length %s greater than its max %s",
                        cardType,
                        minCardLength,
                        maxCardLength
                    ), minCardLength <= maxCardLength
                )
                assertTrue(
                    String.format(
                        "%s: Unusual security code length %s",
                        cardType,
                        securityCodeLength
                    ),
                    securityCodeLength in MIN_SECURITY_CODE_LENGTH..MAX_SECURITY_CODE_LENGTH
                )
                assertTrue(
                    String.format("%s: No front resource declared", cardType),
                    frontResource != 0
                )
                assertTrue(
                    String.format("%s: No Security code resource declared", cardType),
                    securityCodeName != 0
                )

                if (cardType != CardType.UNKNOWN && cardType != CardType.EMPTY) {
                    val regex = pattern.toString()
                    assertTrue(
                        String.format("%s: Pattern must start with ^", cardType),
                        regex.startsWith("^")
                    )
                    assertTrue(
                        String.format("%s: Pattern must end with \\d*", cardType),
                        regex.endsWith("\\d*")
                    )
                }
            }
        }
    }
}