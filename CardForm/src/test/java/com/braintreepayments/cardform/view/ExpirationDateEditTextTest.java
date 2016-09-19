package com.braintreepayments.cardform.view;

import android.text.Editable;
import android.text.Spanned;
import android.view.View;

import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.test.TestActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class ExpirationDateEditTextTest {

    private ExpirationDateEditText mView;

    @Before
    public void setup() {
        mView = (ExpirationDateEditText) Robolectric.setupActivity(TestActivity.class)
                .findViewById(R.id.bt_card_form_expiration);
    }

    @Test
    public void onDetachedFromWindow_handlesNullDialog() {
        mView.onDetachedFromWindow();
    }

    @Test
    public void onFocusChanged_handlesNullDialog() {
        mView.onFocusChanged(true, View.FOCUS_DOWN, null);
    }

    @Test
    public void typing_2_through_9_addsPrefix_0() {
        for (int i = 2; i <= 9; i++) {
            setText(String.valueOf(i));
            assertTextIs("0" + i);
        }
    }

    @Test
    public void typing_0_or_1_doesntAddPrefix_0() {
        setText("0");
        assertTextIs("0");

        setText("1");
        assertTextIs("1");
    }

    @Test
    public void canOnlyTypeNumeric() {
        type('-');
        assertTextIs("");

        type('5', ':', '/', '-', 'h');
        assertTextIs("05");
    }

    @Test
    public void typingNumbersWithoutSlashWorks() {
        type('1', '2', '1', '8');
        assertTextIs("1218");
    }

    @Test
    public void addsSlashForYou() {
        setText("1218");

        Spanned spanned = mView.getText();

        SlashSpan[] appendSlashSpan = spanned.getSpans(0, mView.getText().toString().length(), SlashSpan.class);
        assertEquals(1, appendSlashSpan.length);
    }

    @Test
    public void maxLengthIsSix() {
        type('1', '2', '2', '0', '0', '1', '5');
        assertTextIs("122001");
    }

    @Test
    public void getMonth() {
        assertEquals("getMonth() should be \"\" if text is empty", "", mView.getMonth());

        setText("1");
        assertEquals("", mView.getMonth());
        setText("01");
        assertEquals("01", mView.getMonth());
        setText("0218");
        assertEquals("02", mView.getMonth());
        setText("032018");
        assertEquals("03", mView.getMonth());
    }

    @Test
    public void getYear() {
        assertEquals("getYear() should be \"\" if text is empty", "", mView.getYear());

        setText("01");
        assertEquals("", mView.getYear());

        type('1');
        assertEquals("getYear() doesn't return unformatted years", "", mView.getYear());

        type('8');
        assertEquals("18", mView.getYear());

        setText("012018");
        assertEquals("getYear() will return 4-digit years", "2018", mView.getYear());
    }

    @Test
    public void isValid_returnsFalseForInvalidDate() {
        assertFalse(mView.isValid());
    }

    @Test
    public void isValid_returnsTrueForValidDate() {
        setText("1218");

        assertTrue(mView.isValid());
    }

    @Test
    public void isValid_returnsTrueForInvalidDateWhenOptional() {
        mView.setOptional(true);

        assertTrue(mView.isValid());
    }

    @Test
    public void getErrorMessage_returnsErrorMessageWhenEmpty() {
        assertEquals(RuntimeEnvironment.application.getString(R.string.bt_expiration_required), mView.getErrorMessage());
    }

    @Test
    public void getErrorMessage_returnsErrorMessageWhenNotEmpty() {
        type('4');

        assertEquals(RuntimeEnvironment.application.getString(R.string.bt_expiration_invalid), mView.getErrorMessage());
    }

    private ExpirationDateEditTextTest type(char... chars) {
        Editable editable = mView.getText();
        for (char c : chars) {
            editable.append(c);
        }
        return this;
    }

    private ExpirationDateEditTextTest type(char c) {
        mView.getText().append(c);
        return this;
    }

    private void assertTextIs(String expected) {
        assertEquals(expected, mView.getText().toString());
    }

    /** Clears the field and types the text as if it were new/untouched. */
    private void setText(CharSequence seq) {
        mView.setText("");
        mView.setText(seq);
    }
}
