package com.braintreepayments.api

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.braintreepayments.api.cardform.R
import java.util.regex.Pattern

/**
 * @property pattern The regex matching this card type.
 * @property relaxedPrefixPattern The relaxed prefix regex matching this card type. To be used in determining card type if no pattern matches.
 * @property frontResource The android resource id for the front card image, highlighting card number format.
 * @property securityCodeName The android resource id for the security code name for this card type.
 * @property securityCodeLength The length of the current card's security code.
 * @property minCardLength minimum length of a card for this {@link CardType}
 * @property maxCardLength max length of a card for this {@link CardType}
 * @property spaceIndices the locations where spaces should be inserted when formatting the card in a user friendly way. Only for display purposes.
 */
data class CardAttributes constructor(
    val cardType: CardType,
    @DrawableRes val frontResource: Int,
    val maxCardLength: Int,
    val minCardLength: Int,
    val pattern: Pattern,
    val relaxedPrefixPattern: Pattern? = null,
    val securityCodeLength: Int,
    @StringRes val securityCodeName: Int,
) {

    val spaceIndices: IntArray =
        if (cardType == CardType.AMEX) AMEX_SPACE_INDICES else DEFAULT_SPACE_INDICES

    fun matches(cardNumber: String) = matchesStrict(cardNumber) || matchesRelaxed(cardNumber)
    fun matchesStrict(cardNumber: String) = pattern.matcher(cardNumber).matches()
    fun matchesRelaxed(cardNumber: String) =
        relaxedPrefixPattern?.matcher(cardNumber)?.matches() ?: false

    companion object {

        private val AMEX_SPACE_INDICES = intArrayOf(4, 10)
        private val DEFAULT_SPACE_INDICES = intArrayOf(4, 8, 12)

        @JvmField
        val EMPTY = CardAttributes(
            cardType = CardType.EMPTY,
            pattern = Pattern.compile("^$"),
            frontResource = R.drawable.bt_ic_unknown,
            minCardLength = 12,
            maxCardLength = 19,
            securityCodeLength = 3,
            securityCodeName = R.string.bt_cvv,
        )

        val UNKNOWN = CardAttributes(
            cardType = CardType.UNKNOWN,
            pattern = Pattern.compile("\\d+"),
            frontResource = R.drawable.bt_ic_unknown,
            minCardLength = 12,
            maxCardLength = 19,
            securityCodeLength = 3,
            securityCodeName = R.string.bt_cvv,
        )

        private val knownCardBrandAttributes = createCardAttributeMap(
            CardAttributes(
                cardType = CardType.HIPERCARD,
                pattern = Pattern.compile("^606282\\d*"),
                frontResource = R.drawable.bt_ic_hipercard,
                minCardLength = 16,
                maxCardLength = 16,
                securityCodeLength = 3,
                securityCodeName = R.string.bt_cvc,
            ),
            CardAttributes(
                cardType = CardType.HIPER,
                pattern = Pattern.compile("^637(095|568|599|609|612)\\d*"),
                frontResource = R.drawable.bt_ic_hiper,
                minCardLength = 16,
                maxCardLength = 16,
                securityCodeLength = 3,
                securityCodeName = R.string.bt_cvc,
            ),
            CardAttributes(
                cardType = CardType.UNIONPAY,
                pattern = Pattern.compile("^62\\d*"),
                frontResource = R.drawable.bt_ic_unionpay,
                minCardLength = 16,
                maxCardLength = 19,
                securityCodeLength = 3,
                securityCodeName = R.string.bt_cvn,
            ),
            CardAttributes(
                cardType = CardType.VISA,
                pattern = Pattern.compile("^4\\d*"),
                frontResource = R.drawable.bt_ic_visa,
                minCardLength = 16,
                maxCardLength = 16,
                securityCodeLength = 3,
                securityCodeName = R.string.bt_cvv,
            ),
            CardAttributes(
                cardType = CardType.MASTERCARD,
                pattern = Pattern.compile("^(5[1-5]|222[1-9]|22[3-9]|2[3-6]|27[0-1]|2720)\\d*"),
                frontResource = R.drawable.bt_ic_mastercard,
                minCardLength = 16,
                maxCardLength = 16,
                securityCodeLength = 3,
                securityCodeName = R.string.bt_cvc,
            ),
            CardAttributes(
                cardType = CardType.DISCOVER,
                pattern = Pattern.compile("^(6011|65|64[4-9]|622)\\d*"),
                frontResource = R.drawable.bt_ic_discover,
                minCardLength = 16,
                maxCardLength = 19,
                securityCodeLength = 3,
                securityCodeName = R.string.bt_cid,
            ),
            CardAttributes(
                cardType = CardType.AMEX,
                pattern = Pattern.compile("^3[47]\\d*"),
                frontResource = R.drawable.bt_ic_amex,
                minCardLength = 15,
                maxCardLength = 15,
                securityCodeLength = 4,
                securityCodeName = R.string.bt_cid,
            ),
            CardAttributes(
                cardType = CardType.DINERS_CLUB,
                pattern = Pattern.compile("^(36|38|30[0-5])\\d*"),
                frontResource = R.drawable.bt_ic_diners_club,
                minCardLength = 14,
                maxCardLength = 14,
                securityCodeLength = 3,
                securityCodeName = R.string.bt_cvv,
            ),
            CardAttributes(
                cardType = CardType.JCB,
                pattern = Pattern.compile("^35\\d*"),
                frontResource = R.drawable.bt_ic_jcb,
                minCardLength = 16,
                maxCardLength = 16,
                securityCodeLength = 3,
                securityCodeName = R.string.bt_cvv,
            ),
            CardAttributes(
                cardType = CardType.MAESTRO,
                pattern = Pattern.compile("^(5018|5020|5038|5043|5[6-9]|6020|6304|6703|6759|676[1-3])\\d*"),
                frontResource = R.drawable.bt_ic_maestro,
                minCardLength = 12,
                maxCardLength = 19,
                securityCodeLength = 3,
                securityCodeName = R.string.bt_cvc,
                relaxedPrefixPattern = Pattern.compile("^6\\d*")
            )
        )

        val allKnownCardBrands = knownCardBrandAttributes.values

        private fun createCardAttributeMap(vararg items: CardAttributes): Map<CardType, CardAttributes> {
            val result = mutableMapOf<CardType, CardAttributes>()
            for (item in items) {
                result[item.cardType] = item
            }
            return result
        }

        @JvmStatic
        fun forCardType(cardType: CardType): CardAttributes =
            knownCardBrandAttributes[cardType] ?: UNKNOWN
    }
}