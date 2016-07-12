package com.braintreepayments.cardform.view;

import android.text.Editable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.test.TestActivity;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by dhaval on 12/07/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
public class CardNameEditTextTest {

    private CardNameEditText mView;

    @Before
    public void setup(){
        mView = (CardNameEditText) Robolectric.setupActivity(TestActivity.class)
                .findViewById(R.id.bt_card_form_card_name);
    }

    @Test
    public void validateContent(){
        assertNotNull("Name editbox is not null", mView);
        type("Jim");
        assertTrue("Name editbox contains name", "Jim".equals(mView.getText().toString()));
    }

    @Test
    public void defaultMinIs3(){
        type("Jim").assertTextIs("Jim");
        type("G").assertTextIs("JimG");
    }

    /* helpers */
    private CardNameEditTextTest type(String text) {
        Editable editable = mView.getText();
        for (char c : text.toCharArray()) {
            editable.append(c);
        }
        return this;
    }

    private void assertTextIs(String expected) {
        assertEquals(expected, mView.getText().toString());
    }
}
