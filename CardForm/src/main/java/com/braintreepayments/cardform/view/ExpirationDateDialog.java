package com.braintreepayments.cardform.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.utils.ExpirationDateDialogTheme;
import com.braintreepayments.cardform.utils.ExpirationDateItemAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class ExpirationDateDialog extends Dialog implements DialogInterface.OnShowListener {

    private static final String[] MONTHS = new String[] { "01", "02", "03", "04", "05", "06", "07",
            "08", "09", "10", "11", "12"};
    private final int CURRENT_MONTH = Calendar.getInstance().get(Calendar.MONTH) + 1; // months are 0 indexed
    private final int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);
    private final String[] mYears = new String[10];

    private int mAnimationDelay;
    private ExpirationDateEditText mEditText;
    private ExpirationDateDialogTheme mTheme;
    private boolean mHasSelectedMonth;
    private boolean mHasSelectedYear;
    private int mSelectedMonth = -1;
    private int mSelectedYear = -1;

    protected ExpirationDateDialog(Context context) {
        super(context);
    }

    protected ExpirationDateDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ExpirationDateDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static ExpirationDateDialog create(Activity activity, ExpirationDateEditText editText) {
        ExpirationDateDialog dialog = new ExpirationDateDialog(activity, R.style.bt_expiration_date_dialog_sheet);
        dialog.setOwnerActivity(activity);
        dialog.mTheme = ExpirationDateDialogTheme.detectTheme(activity);
        dialog.mEditText = editText;

        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAnimationDelay = getContext().getResources().getInteger(android.R.integer.config_shortAnimTime);

        if (mTheme == ExpirationDateDialogTheme.LIGHT) {
            setContentView(R.layout.bt_expiration_date_sheet_light);
        } else {
            setContentView(R.layout.bt_expiration_date_sheet_dark);
        }

        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setOnShowListener(this);

        for (int i = 0; i < mYears.length; i++) {
            mYears[i] = Integer.toString(CURRENT_YEAR + i);
        }

        final ExpirationDateItemAdapter monthAdapter = new ExpirationDateItemAdapter(getContext(), mTheme, MONTHS);
        final ExpirationDateItemAdapter yearAdapter = new ExpirationDateItemAdapter(getContext(), mTheme, mYears);

        GridView monthsGridView = (GridView) findViewById(R.id.bt_expiration_month_grid_view);
        monthsGridView.setAdapter(monthAdapter);
        monthAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mHasSelectedMonth = true;
                mSelectedMonth = position;
                setExpirationDate();

                if (Integer.parseInt(MONTHS[position]) < CURRENT_MONTH) {
                    yearAdapter.setDisabled(Collections.singletonList(0));
                } else {
                    yearAdapter.setDisabled(new ArrayList<Integer>());
                }
            }
        });

        GridView yearsGridView = (GridView) findViewById(R.id.bt_expiration_year_grid_view);
        yearsGridView.setAdapter(yearAdapter);
        yearAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mHasSelectedYear = true;
                mSelectedYear = position;
                setExpirationDate();

                if (Integer.parseInt(mYears[position]) == CURRENT_YEAR) {
                    List<Integer> disabledMonths = new ArrayList<>();
                    for (int i = 0; i < MONTHS.length; i++) {
                        if (Integer.parseInt(MONTHS[i]) < CURRENT_MONTH) {
                            disabledMonths.add(i);
                        }
                    }
                    monthAdapter.setDisabled(disabledMonths);
                } else {
                    monthAdapter.setDisabled(new ArrayList<Integer>());
                }
            }
        });

        String currentMonth = mEditText.getMonth();
        for (int i = 0; i < MONTHS.length; i++) {
            if (MONTHS[i].equals(currentMonth)) {
                mSelectedMonth = i;
                monthAdapter.setSelected(mSelectedMonth);
                break;
            }
        }

        String currentYear = mEditText.getYear();
        for (int i = 0; i < mYears.length; i++) {
            if (mYears[i].equals(currentYear)) {
                mSelectedYear = i;
                yearAdapter.setSelected(mSelectedYear);
                break;
            }
        }
    }

    @Override
    public void show() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ExpirationDateDialog.super.show();
            }
        }, mAnimationDelay);
    }

    @Override
    public void onShow(DialogInterface dialog) {
        mHasSelectedMonth = false;
        mHasSelectedYear = false;
    }

    private void setExpirationDate() {
        String expirationDate;
        if (mSelectedMonth == -1) {
            expirationDate = "  ";
        } else {
            expirationDate = MONTHS[mSelectedMonth];
        }

        if (mSelectedYear == -1) {
            expirationDate += "    ";
        } else {
            expirationDate += mYears[mSelectedYear];
        }

        mEditText.setText(expirationDate);

        if (mHasSelectedMonth && mHasSelectedYear) {
            final View focusedView = mEditText.focusNextView();
            if (focusedView != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                                .showSoftInput(focusedView, 0);
                    }
                }, mAnimationDelay);
            }
        }
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        boolean closeOnTouch = (event.getAction() == MotionEvent.ACTION_DOWN && isOutOfBounds(event)
                && getWindow().peekDecorView() != null);
        if (isShowing() && closeOnTouch) {
            View rootView = getOwnerActivity().getWindow().getDecorView().getRootView();
            final View selectedView;
            if (rootView instanceof ViewGroup) {
                selectedView = findViewAt((ViewGroup) rootView, (int) event.getRawX(), (int) event.getRawY());
            } else {
                selectedView = null;
            }

            if (selectedView != null && selectedView != mEditText) {
                dismiss();

                selectedView.requestFocus();
                if (selectedView instanceof EditText) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                                    .showSoftInput(selectedView, 0);
                        }
                    }, mAnimationDelay);
                }
            }

            return true;
        }

        return false;
    }

    private View findViewAt(ViewGroup viewGroup, int x, int y) {
        for(int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                View foundView = findViewAt((ViewGroup) child, x, y);
                if (foundView != null) {
                    return foundView;
                }
            } else {
                int[] location = new int[2];
                child.getLocationOnScreen(location);
                Rect rect = new Rect(location[0], location[1], location[0] + child.getWidth(), location[1] + child.getHeight());
                if (rect.contains(x, y)) {
                    return child;
                }
            }
        }

        return null;
    }

    /**
     * Based on Window#isOutOfBounds
     */
    private boolean isOutOfBounds(MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(getContext()).getScaledWindowTouchSlop();
        final View decorView = getWindow().getDecorView();
        return (x < -slop) || (y < -slop)
                || (x > (decorView.getWidth() + slop))
                || (y > (decorView.getHeight() + slop));
    }
}
