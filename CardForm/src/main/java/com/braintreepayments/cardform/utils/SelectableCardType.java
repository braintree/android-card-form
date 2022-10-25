package com.braintreepayments.cardform.utils;

import com.braintreepayments.api.CardType;

public class SelectableCardType {

    CardType cardType;
    boolean disabled = false;

    public SelectableCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public CardType getCardType() {
        return cardType;
    }
}
