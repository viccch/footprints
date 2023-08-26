package io.github.viccch.footprints.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Timer;
import java.util.TimerTask;

public class SoftKeyboardUtils {
    /**
     * 延时弹出软键盘
     *
     * @param activity 当前activity
     * @param view     当前获取焦点的view
     * @param delay    延时弹出的时间
     */
    public static void delayShowSoftInput(final Activity activity, final View view, final long delay) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showSoftInput(view);
                        }
                    });
                }
            }, delay);
    }

    /**
     * 弹出软键盘
     * @param view 当前获取焦点的view
     */
    public static void showSoftInput(final View view) {
        if (view != null) {
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.requestFocus();//获取焦点
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            //if (!imm.isActive()) //没有显示键盘，弹出
                imm.showSoftInput(view, 0);
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param view         当前获取焦点的view
     * @param isClearFocus true释放焦点， false保留焦点
     */
    public static void hideSoftKeyboard(final View view, final boolean isClearFocus) {
        if (view != null) {
            if (isClearFocus) view.clearFocus();
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) //有显示键盘，隐藏
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}