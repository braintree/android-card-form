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
    val pattern: Pattern,
    val relaxedPrefixPattern: Pattern?,
    @DrawableRes val frontResource: Int,
    val minCardLength: Int,
    val maxCardLength: Int,
    val securityCodeLength: Int,
    @StringRes val securityCodeName: Int,
) {

    constructor(
        cardType: CardType,
        regex: String,
        @DrawableRes frontResource: Int,
        minCardLength: Int,
        maxCardLength: Int,
        securityCodeLength: Int,
        @StringRes securityCodeName: Int,
        relaxedPrefixPattern: String? = null,
    ) : this(
        cardType,
        Pattern.compile(regex),
        relaxedPrefixPattern?.let { Pattern.compile(it) },
        frontResource,
        minCardLength,
        maxCardLength,
        securityCodeLength,
        securityCodeName
    )

    val spaceIndices: IntArray =
        if (cardType == CardType.AMEX) AMEX_SPACE_INDICES else DEFAULT_SPACE_INDICES

    // TODO: investigate possible redundant regex matching now that parsing is de-coupled from the CardType
    fun matches(cardNumber: String) = matchesStrict(cardNumber) || matchesRelaxed(cardNumber)
    fun matchesStrict(cardNumber: String) = pattern.matcher(cardNumber).matches()
    fun matchesRelaxed(cardNumber: String) =
        relaxedPrefixPattern?.matcher(cardNumber)?.matches() ?: false

    companion object {

        private val AMEX_SPACE_INDICES = intArrayOf(4, 10)
        private val DEFAULT_SPACE_INDICES = intArrayOf(4, 8, 12)

        @JvmStatic
        fun forCardType(cardType: CardType): CardAttributes =
            cardAttributeMap[cardType] ?: cardAttributeMap[CardType.UNKNOWN]!!

        private val cardAttributeMap = createCardAttributeMap(
            CardAttributes(
                cardType = CardType.EMPTY,
                regex = "^$",
                frontResource = R.drawable.bt_ic_unknown,
                minCardLength = 12,
                maxCardLength = 19,
                securityCodeLength = 3,
                securityCodeName = R.string.bt_cvv,
                relaxedPrefixPattern = null
            ),
            CardAttributes(
                cardType = CardType.UNKNOWN,
                regex = "\\d+",
                frontResource = R.drawable.bt_ic_unknown,
                minCardLength = 12,
                maxCardLength = 19,
                securityCodeLength = 3,
                securityCodeName = R.string.bt_cvv,
                relaxedPrefixPattern = null
            ),
            CardAttributes(
                cardType = CardType.HIPERCARD,
                regex = "^606282\\d*",
                frontResource = R.drawable.bt_ic_hipercard,
                minCardLength = 16,
                maxCardLength = 16,
                securityCodeLength = 3,
                securityCodeName = R.string.bt_cvc,
                relaxedPrefixPattern = null
            ),
            CardAttributes(
                cardType = CardType.HIPER,
                regex = "^637(095|568|599|609|612)\\d*",
                frontResource = R.drawable.bt_ic_hiper,
                minCardLength = 16,
                maxCardLength = 16,
                securityCodeLength = 3,
                securityCodeName = R.string.bt_cvc,
                relaxedPrefixPattern = null
            ),
            CardAttributes(
                cardType = CardType.UNIONPAY,
                regex = "^62\\d*",
                frontResource = R.drawable.bt_ic_unionpay,
                minCardLength = 16,
                maxCardLength = 19,
                securityCodeLength = 3,
                securityCodeName = R.string.bt_cvn,
                relaxedPrefixPattern = null
            ),
            CardAttributes(
                cardType = CardType.VISA,
                regex = "^4\\d*",
                frontResource = R.drawable.bt_ic_visa,
                minCardLength = 16,
                maxCardLength = 16,
                securityCodeLength = 3,
                securityCodeName = R.string.bt_cvv,
                relaxedPrefixPattern = null
            ),
            CardAttributes(
                cardType = CardType.MASTERCARD,
                regex = "^(5[1-5]|222[1-9]|22[3-9]|2[3-6]|27[0-1]|2720)\\d*",
                frontResource = R.drawable.bt_ic_mastercard,
                minCardLength = 16,
                maxCardLength = 16,
                securityCodeLength = 3,
                securityCodeName = R.string.bt_cvc,
                relaxedPrefixPattern = null
            ),
            CardAttributes(
                cardType = CardType.DISCOVER,
                regex = "^(6011|65|64[4-9]|622)\\d*",
                frontResource = R.drawable.bt_ic_discover,
                minCardLength = 16,
                maxCardLength = 19,
                securityCodeLength = 3,
                securityCodeName = R.string.bt_cid,
                relaxedPrefixPattern = null
            ),
            CardAttributes(
                cardType = CardType.AMEX,
                regex = "^3[47]\\d*",
                frontResource = R.drawable.bt_ic_amex,
                minCardLength = 15,
                maxCardLength = 15,
                securityCodeLength = 4,
                securityCodeName = R.string.bt_cid,
                relaxedPrefixPattern = null
            ),
            CardAttributes(
                cardType = CardType.DINERS_CLUB,
                regex = "^(36|38|30[0-5])\\d*",
                frontResource = R.drawable.bt_ic_diners_club,
                minCardLength = 14,
                maxCardLength = 14,
                securityCodeLength = 3,
                securityCodeName = R.string.bt_cvv,
                relaxedPrefixPattern = null
            ),
            CardAttributes(
                cardType = CardType.JCB,
                regex = "^35\\d*",
                frontResource = R.drawable.bt_ic_jcb,
                minCardLength = 16,
                maxCardLength = 16,
                securityCodeLength = 3,
                securityCodeName = R.string.bt_cvv,
                relaxedPrefixPattern = null
            ),
            CardAttributes(
                cardType = CardType.MAESTRO,
                regex = "^(5018|5020|5038|5043|5[6-9]|6020|6304|6703|6759|676[1-3])\\d*",
                frontResource = R.drawable.bt_ic_maestro,
                minCardLength = 12,
                maxCardLength = 19,
                securityCodeLength = 3,
                securityCodeName = R.string.bt_cvc,
                relaxedPrefixPattern = "^6\\d*"
            )
        )

        private fun createCardAttributeMap(vararg items: CardAttributes): Map<CardType, CardAttributes> {
            val result = mutableMapOf<CardType, CardAttributes>()
            for (item in items) {
                result[item.cardType] = item
            }
            return result
        }
    }
}