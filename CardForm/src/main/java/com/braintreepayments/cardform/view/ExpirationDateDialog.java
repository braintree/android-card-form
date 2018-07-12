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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.utils.DateValidator;
import com.braintreepayments.cardform.utils.ExpirationDateDialogTheme;
import com.braintreepayments.cardform.utils.ExpirationDateItemAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class ExpirationDateDialog extends Dialog implements DialogInterface.OnShowListener {

    private static final List<String> MONTHS = Arrays.asList("01", "02", "03", "04", "05", "06",
            "07", "08", "09", "10", "11", "12");
    private final int CURRENT_MONTH = Calendar.getInstance().get(Calendar.MONTH) + 1; // months are 0 indexed
    private final int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);
    private final List<String> mYears = new ArrayList<>();

    private int mAnimationDelay;
    private ExpirationDateEditText mEditText;
    private ExpirationDateDialogTheme mTheme;
    private GridView mYearGridView;
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
        ExpirationDateDialogTheme theme = ExpirationDateDialogTheme.detectTheme(activity);
        ExpirationDateDialog dialog;
        if (theme == ExpirationDateDialogTheme.LIGHT) {
            dialog = new ExpirationDateDialog(activity, R.style.bt_expiration_date_dialog_light);
        } else {
            dialog = new ExpirationDateDialog(activity, R.style.bt_expiration_date_dialog_dark);
        }

        dialog.setOwnerActivity(activity);
        dialog.mTheme = theme;
        dialog.mEditText = editText;

        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bt_expiration_date_sheet);

        mAnimationDelay = getContext().getResources().getInteger(android.R.integer.config_shortAnimTime);

        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setOnShowListener(this);

        for (int i = 0; i < DateValidator.MAXIMUM_VALID_YEAR_DIFFERENCE; i++) {
            mYears.add(Integer.toString(CURRENT_YEAR + i));
        }

        final ExpirationDateItemAdapter monthAdapter = new ExpirationDateItemAdapter(getContext(), mTheme, MONTHS);
        final ExpirationDateItemAdapter yearAdapter = new ExpirationDateItemAdapter(getContext(), mTheme, mYears);

        GridView monthsGridView = findViewById(R.id.bt_expiration_month_grid_view);
        monthsGridView.setAdapter(monthAdapter);
        monthAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mHasSelectedMonth = true;
                mSelectedMonth = position;
                setExpirationDate();

                if (Integer.parseInt(MONTHS.get(position)) < CURRENT_MONTH) {
                    yearAdapter.setDisabled(Collections.singletonList(0));
                } else {
                    yearAdapter.setDisabled(new ArrayList<Integer>());
                }
            }
        });

        mYearGridView = findViewById(R.id.bt_expiration_year_grid_view);
        mYearGridView.setAdapter(yearAdapter);
        yearAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mHasSelectedYear = true;
                mSelectedYear = position;
                setExpirationDate();

                if (Integer.parseInt(mYears.get(position)) == CURRENT_YEAR) {
                    List<Integer> disabledMonths = new ArrayList<>();
                    for (int i = 0; i < MONTHS.size(); i++) {
                        if (Integer.parseInt(MONTHS.get(i)) < CURRENT_MONTH) {
                            disabledMonths.add(i);
                        }
                    }
                    monthAdapter.setDisabled(disabledMonths);
                } else {
                    monthAdapter.setDisabled(new ArrayList<Integer>());
                }
            }
        });

        mSelectedMonth = MONTHS.indexOf(mEditText.getMonth());
        if (mSelectedMonth >= 0) {
            monthAdapter.setSelected(mSelectedMonth);
        }

        mSelectedYear = mYears.indexOf(mEditText.getYear());
        if (mSelectedYear >= 0) {
            yearAdapter.setSelected(mSelectedYear);
        }
    }

    @Override
    public void show() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Activity ownerActivity = ExpirationDateDialog.this.getOwnerActivity();
                if (mEditText.isFocused() && ownerActivity != null && !ownerActivity.isFinishing()) {
                    ExpirationDateDialog.super.show();
                }
            }
        }, mAnimationDelay);
    }

    @Override
    public void onShow(DialogInterface dialog) {
        if (mSelectedYear > 0) {
            mYearGridView.smoothScrollToPosition(mSelectedYear);
        }

        mHasSelectedMonth = false;
        mHasSelectedYear = false;
    }

    private void setExpirationDate() {
        String expirationDate;
        if (mSelectedMonth == -1) {
            expirationDate = "  ";
        } else {
            expirationDate = MONTHS.get(mSelectedMonth);
        }

        if (mSelectedYear == -1) {
            expirationDate += "    ";
        } else {
            expirationDate += mYears.get(mSelectedYear);
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

                if (selectedView instanceof EditText) {
                    selectedView.requestFocus();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                                    .showSoftInput(selectedView, 0);
                        }
                    }, mAnimationDelay);
                } else if (selectedView instanceof Button) {
                    selectedView.callOnClick();
                }
            } else if (selectedView == null) {
                dismiss();
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
                if (foundView != null && foundView.isShown()) {
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
