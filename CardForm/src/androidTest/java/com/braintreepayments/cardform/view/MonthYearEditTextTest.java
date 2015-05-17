package com.braintreepayments.cardform.view;

import android.test.UiThreadTest;
import android.text.Editable;
import android.text.Spanned;

import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.test.TestActivityTestCase;

public class MonthYearEditTextTest extends TestActivityTestCase {

    private MonthYearEditText mView;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        setupCardForm();
        mView = (MonthYearEditText) mActivity.findViewById(R.id.bt_card_form_expiration);
        assertNotNull(mView);
    }

    @UiThreadTest
    public void testTyping_2_through_9_addsPrefix_0() {
        for (int i = 2; i <= 9; i++) {
            setText(String.valueOf(i));
            assertTextIs("0" + i);
        }
    }

    @UiThreadTest
    public void testTyping_0_or_1_doesntAddPrefix_0() {
        setText("0");
        assertTextIs("0");

        setText("1");
        assertTextIs("1");
    }

    @UiThreadTest
    public void testCanOnlyTypeNumeric() {
        type('-');
        assertTextIs("");

        type('5', ':', '/', '-', 'h');
        assertTextIs("05");
    }

    @UiThreadTest
    public void testTypingNumbersWithoutSlashWorks() {
        type('1', '2', '1', '8');
        assertTextIs("1218");
    }

    @UiThreadTest
    public void testAddsSlashForYou() {
        setText("1218");

        Spanned spanned = mView.getText();

        SlashSpan[] appendSlashSpan = spanned.getSpans(0, mView.getText().toString().length(), SlashSpan.class);
        assertEquals(1, appendSlashSpan.length);
    }

    @UiThreadTest
    public void testMaxLengthIsSix() {
        type('1', '2', '2', '0', '0', '1', '5');
        assertTextIs("122001");
    }

    @UiThreadTest
    public void testGetMonth() {
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

    @UiThreadTest
    public void testGetYear() {
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

    private MonthYearEditTextTest type(char... chars) {
        Editable editable = mView.getText();
        for (char c : chars) {
            editable.append(c);
        }
        return this;
    }

    private MonthYearEditTextTest type(char c) {
        mView.getText().append(c);
        return this;
    }

    private void assertTextIs(String expected) {
        assertEquals(expected, getString());
    }

    private String getString() {
        return mView.getText().toString();
    }

    /** Clears the field and types the text as if it were new/untouched. */
    private void setText(CharSequence seq) {
        mView.setText("");
        mView.setText(seq);
    }
}
