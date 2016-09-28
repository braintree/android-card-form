package com.braintreepayments.cardform.view;

import android.text.Editable;

import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.test.TestActivity;
import com.braintreepayments.cardform.utils.CardType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.braintreepayments.cardform.test.Assertions.assertIconHintIs;
import static com.braintreepayments.cardform.test.Assertions.assertNoHintIcon;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class CardEditTextTest {

    private CardEditText mView;

    @Before
    public void setup() {
        mView = (CardEditText) Robolectric.setupActivity(TestActivity.class)
                .findViewById(R.id.bt_card_form_card_number);
    }

    @Test
    public void testVisa() {
        helper("4", "111 1111 1111 1111", R.drawable.bt_ic_visa, 4, 8, 12);
    }

    @Test
    public void testMasterCard() {
        helper("55", "55 5555 5555 4444", R.drawable.bt_ic_mastercard, 4, 8, 12);
    }

    @Test
    public void testDiscover() {
        helper("6011", "1111 1111 1117", R.drawable.bt_ic_discover, 4, 8, 12);
    }

    @Test
    public void testAmex() {
        helper("37", "82 822463 10005", R.drawable.bt_ic_amex, 4, 10);
    }

    @Test
    public void testJcb() {
        helper("35", "30 1113 3330 0000", R.drawable.bt_ic_jcb, 4, 8, 12);
    }

    @Test
    public void testDiners() {
        helper("3000", "0000 0000 04", R.drawable.bt_ic_diners_club, 4, 8, 12);
    }

    @Test
    public void testMaestro() {
        helper("5018", "0000 0000 0000122", R.drawable.bt_ic_maestro, 4, 8, 12);
    }

    @Test
    public void testUnionPay() {
        helper("62", "40 8888 8888 8885127", R.drawable.bt_ic_unionpay, 4, 8, 12);
    }

    @Test
    public void testUnknown() {
        helper("1", "111 1111 1111 1111111", R.drawable.bt_ic_unknown, 4, 8, 12);
    }

    @Test
    public void doesNotShowsCardTypeIconsWhenEmpty() {
        assertNoHintIcon(mView);
    }

    @Test
    public void doesNotShowCardTypeIconsWhenDisabled() {
        mView.displayCardTypeIcon(false);

        assertNoHintIcon(mView);

        type("4");

        assertNoHintIcon(mView);
    }

    @Test
    public void doesNothingWhenCardTypeChangesAndNoCardTypeListenerSet() {
        type("4");
    }

    @Test
    public void callsCardTypeChangedListenerWhenSetAndCardTypeChanges() {
        CardEditText.OnCardTypeChangedListener listener = mock(CardEditText.OnCardTypeChangedListener.class);
        mView.setOnCardTypeChangedListener(listener);

        type("4");

        verify(listener).onCardTypeChanged(CardType.VISA);
    }

    @Test
    public void doesNotCallCardTypeChangedListenerWhenSetAndCardTypeDoesNotChange() {
        CardEditText.OnCardTypeChangedListener listener = mock(CardEditText.OnCardTypeChangedListener.class);
        mView.setOnCardTypeChangedListener(listener);

        type("4");
        type("111");

        verify(listener).onCardTypeChanged(CardType.VISA);
    }

    @Test
    public void doesNothingWhenEditTextChangesFromEmptyToUnknownAndNoCardTypeListenerSet() {
        type("1");
    }

    @Test
    public void callsCardTypeChangedListenerWhenEditTextChangesFromEmptyToUnknown() {
        CardEditText.OnCardTypeChangedListener listener = mock(CardEditText.OnCardTypeChangedListener.class);
        mView.setOnCardTypeChangedListener(listener);

        type("1");

        verify(listener).onCardTypeChanged(CardType.UNKNOWN);
    }

    @Test
    public void doesNothingWhenEditTextChangesFromUnknownToEmptyAndNoCardTypeListenerSet() {
        type("1");
        mView.getText().clear();
    }

    @Test
    public void callsCardTypeChangeListenerWhenEditTextChangesFromUnknownToEmpty() {
        CardEditText.OnCardTypeChangedListener listener = mock(CardEditText.OnCardTypeChangedListener.class);
        mView.setOnCardTypeChangedListener(listener);

        type("1");
        mView.getText().clear();

        verify(listener).onCardTypeChanged(CardType.UNKNOWN);
        verify(listener).onCardTypeChanged(CardType.EMPTY);
    }

    @Test
    public void isValid_returnsFalseForInvalidCardNumber() {
        assertFalse(mView.isValid());
    }

    @Test
    public void isValid_returnsTrueForValidCardNumber() {
        type("4111111111111111");

        assertTrue(mView.isValid());
    }

    @Test
    public void isValid_returnsTrueForInvalidCardNumberIfOptional() {
        mView.setOptional(true);

        assertTrue(mView.isValid());
    }

    @Test
    public void getErrorMessage_returnsErrorMessageWhenEmpty() {
        assertEquals(RuntimeEnvironment.application.getString(R.string.bt_card_number_required), mView.getErrorMessage());
    }

    @Test
    public void getErrorMessage_returnsErrorMessageWhenNotEmpty() {
        type("4");

        assertEquals(RuntimeEnvironment.application.getString(R.string.bt_card_number_invalid), mView.getErrorMessage());
    }

    private void helper(String start, String end, int drawable, int... spans) {
        assertNoHintIcon(mView);
        type(start);
        assertIconHintIs(mView, drawable);
        type(end).assertSpansAt(spans);
        assertCharacterMaxLengthHit();
    }

    private void assertCharacterMaxLengthHit() {
        Editable text = mView.getText();
        int maxLength = text.length();
        type("1111");
        assertEquals("Able to write more characters than max length", maxLength, text.length());
    }

    private void assertSpansAt(int... indices) {
        Editable text = mView.getText();
        List<SpaceSpan> allSpans = Arrays.asList(text.getSpans(0, text.length(),
                SpaceSpan.class));
        List<SpaceSpan> foundSpans = new ArrayList<>();
        for (int i : indices) {
            SpaceSpan[] span = text.getSpans(i - 1, i, SpaceSpan.class);
            assertEquals(1, span.length);
            foundSpans.add(span[0]);
        }
        assertEquals(allSpans, foundSpans);
    }

    private CardEditTextTest type(String text) {
        Editable editable = mView.getText();
        for (char c : text.toCharArray()) {
            if (c != ' ') {
                editable.append(c);
            }
        }
        return this;
    }
}
