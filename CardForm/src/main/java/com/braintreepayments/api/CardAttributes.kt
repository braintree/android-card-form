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
        relaxedPrefixPattern: String?,
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

        private fun createCardAttributeMap(vararg items: CardAttributes): Map<CardType, CardAttributes> {
            val result = mutableMapOf<CardType, CardAttributes>()
            for (item in items) {
                result[item.cardType] = item
            }
            return result
        }

        private val cardAttributeMap = createCardAttributeMap(
            CardAttributes(
                CardType.EMPTY,
                "^$",
                R.drawable.bt_ic_unknown,
                12,
                19,
                3,
                R.string.bt_cvv,
                null
            ),
            CardAttributes(
                CardType.UNKNOWN,
                "\\d+",
                R.drawable.bt_ic_unknown,
                12,
                19,
                3,
                R.string.bt_cvv,
                null
            ),
            CardAttributes(
                CardType.HIPERCARD,
                "^606282\\d*",
                R.drawable.bt_ic_hipercard,
                16,
                16,
                3,
                R.string.bt_cvc,
                null
            ),
            CardAttributes(
                CardType.HIPER,
                "^637(095|568|599|609|612)\\d*",
                R.drawable.bt_ic_hiper,
                16,
                16,
                3,
                R.string.bt_cvc,
                null
            ),
            CardAttributes(
                CardType.UNIONPAY,
                "^62\\d*",
                R.drawable.bt_ic_unionpay,
                16,
                19,
                3,
                R.string.bt_cvn,
                null
            ),
            CardAttributes(
                CardType.VISA,
                "^4\\d*",
                R.drawable.bt_ic_visa,
                16,
                16,
                3,
                R.string.bt_cvv,
                null
            ),
            CardAttributes(
                CardType.MASTERCARD,
                "^(5[1-5]|222[1-9]|22[3-9]|2[3-6]|27[0-1]|2720)\\d*",
                R.drawable.bt_ic_mastercard,
                16,
                16,
                3,
                R.string.bt_cvc,
                null
            ),
            CardAttributes(
                CardType.DISCOVER,
                "^(6011|65|64[4-9]|622)\\d*",
                R.drawable.bt_ic_discover,
                16,
                19,
                3,
                R.string.bt_cid,
                null
            ),
            CardAttributes(
                CardType.AMEX,
                "^3[47]\\d*",
                R.drawable.bt_ic_amex,
                15,
                15,
                4,
                R.string.bt_cid,
                null
            ),
            CardAttributes(
                CardType.DINERS_CLUB,
                "^(36|38|30[0-5])\\d*",
                R.drawable.bt_ic_diners_club,
                14,
                14,
                3,
                R.string.bt_cvv,
                null
            ),
            CardAttributes(
                CardType.JCB,
                "^35\\d*",
                R.drawable.bt_ic_jcb,
                16,
                16,
                3,
                R.string.bt_cvv,
                null
            ),
            CardAttributes(
                CardType.MAESTRO,
                "^(5018|5020|5038|5043|5[6-9]|6020|6304|6703|6759|676[1-3])\\d*",
                R.drawable.bt_ic_maestro,
                12,
                19,
                3,
                R.string.bt_cvc,
                "^6\\d*"
            )
        )

        @JvmStatic
        fun forCardType(cardType: CardType): CardAttributes =
            cardAttributeMap[cardType] ?: cardAttributeMap[CardType.UNKNOWN]!!
    }
}