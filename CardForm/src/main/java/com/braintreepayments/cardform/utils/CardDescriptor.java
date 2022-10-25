package com.braintreepayments.cardform.utils;

import com.braintreepayments.api.CardType;

import java.util.regex.Pattern;

public class CardDescriptor {

    private static final int[] AMEX_SPACE_INDICES = {4, 10};
    private static final int[] DEFAULT_SPACE_INDICES = {4, 8, 12};

    private final CardType mType;

    private final Pattern mPattern;
    private final Pattern mRelaxedPrefixPattern;
    private final int mFrontResource;
    private final int mMinCardLength;
    private final int mMaxCardLength;
    private final int mSecurityCodeLength;
    private final int mSecurityCodeName;

    CardDescriptor(
            CardType type,
            String regex,
            int frontResource,
            int minCardLength,
            int maxCardLength,
            int securityCodeLength,
            int securityCodeName,
            String relaxedPrefixPattern
    ) {
        mType = type;
        mPattern = Pattern.compile(regex);
        mRelaxedPrefixPattern = relaxedPrefixPattern == null ? null : Pattern.compile(relaxedPrefixPattern);
        mFrontResource = frontResource;
        mMinCardLength = minCardLength;
        mMaxCardLength = maxCardLength;
        mSecurityCodeLength = securityCodeLength;
        mSecurityCodeName = securityCodeName;
    }

    /**
     * @return The regex matching this card type.
     */
    public Pattern getPattern() {
        return mPattern;
    }

    /**
     * @return The relaxed prefix regex matching this card type. To be used in determining card type if no pattern matches.
     */
    public Pattern getRelaxedPrefixPattern() {
        return mRelaxedPrefixPattern;
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
        return mType == CardType.AMEX ? AMEX_SPACE_INDICES : DEFAULT_SPACE_INDICES;
    }

    public CardType getType() {
        return mType;
    }
}
