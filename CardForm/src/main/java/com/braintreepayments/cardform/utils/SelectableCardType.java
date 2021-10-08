package com.braintreepayments.cardform.utils;

public class SelectableCardType {

    CardType cardType;
    boolean selected = false;

    public SelectableCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public CardType getCardType() {
        return cardType;
    }
}
