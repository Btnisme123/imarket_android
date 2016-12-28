package com.example.framgia.imarketandroid.util;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.example.framgia.imarketandroid.R;

import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * Created by hoavt on 18/08/2016.
 */
public class SystemUtil {
    public static String formatMoneyStr(long money) {
        DecimalFormat dec = new DecimalFormat(Constants.FORMAT_MONEY);
        return " " + dec.format(money) + Constants.VND_CURRENCY_UNIT;
    }

    public static String getCurDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMAT_DATE);
        return sdf.format(Calendar.getInstance().getTime());
    }

    public static long getTimeNowSecond() {
        Calendar c = Calendar.getInstance();
        return c.getTimeInMillis() / Constants.MINUTE;
    }

    public static String formatTimeNow(Context context, long timeNowSecond) {
        if (timeNowSecond < Constants.MINUTE)
            return context.getString(R.string.justnow);
        else if (timeNowSecond < Constants.HOUR)
            return (timeNowSecond / Constants.MINUTE) + " " + context.getString(R.string.min_ago);
        else if (timeNowSecond < Constants.DAY)
            return (timeNowSecond / Constants.HOUR) + " " + context.getString(R.string.hour_ago);
        else
            return (timeNowSecond / Constants.DAY) + " " + context.getString(R.string.day_ago);
    }

    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    public static void makeTextViewResizable(final Context context,
                                             final TextView tv,
                                             final int maxLine,
                                             final String expandText,
                                             final boolean viewMore) {
        if (tv.getTag() == null) tv.setTag(tv.getText());

        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                int lineEndIndex;
                if (maxLine == 0)
                    lineEndIndex = tv.getLayout().getLineEnd(0);
                else if (maxLine > 0 && tv.getLineCount() >= maxLine)
                    lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                else
                    lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                StringBuilder text = new StringBuilder();
                String threeDots = context.getString(R.string.three_dots);
                if ((viewMore && lineEndIndex - threeDots.length() <= 0) || (!viewMore && lineEndIndex <= 0))
                    return;
                if (viewMore)
                    text.append(tv.getText().subSequence(0,
                            lineEndIndex - threeDots.length()).toString() + threeDots);
                else
                    text.append(tv.getText().subSequence(0, lineEndIndex).toString());
                text.append(Constants.LINE_BREAK_HTML + expandText);
                tv.setText(text);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
                tv.setText(addClickablePartTextViewResizable(context,
                                Html.fromHtml(tv.getText().toString()), tv, expandText, viewMore),
                        TextView.BufferType.SPANNABLE);
            }
        });
    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Context context,
                                               final Spanned strSpanned, final TextView tv,
                                               final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);
        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    tv.setLayoutParams(tv.getLayoutParams());
                    tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                    tv.invalidate();
                    if (viewMore)
                        makeTextViewResizable(context, tv, -1,
                                context.getString(R.string.view_less),
                                false);
                    else
                        makeTextViewResizable(context, tv,
                                Constants.MAX_LINE_SPAN_TEXT, context.getString(R.string.view_more),
                                true);
                }
            },
            str.indexOf(spanableText),
            str.indexOf(spanableText) + spanableText.length(),
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            ssb.setSpan(new ForegroundColorSpan(Color.BLUE),
                    str.indexOf(spanableText),
                    str.indexOf(spanableText) + spanableText.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ssb;
    }
}
