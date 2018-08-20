package ru.igarin.bgtraining.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import ru.igarin.bgtraining.R;
import ru.igarin.bgtraining.WordType;
import ru.igarin.bgtraining.db.DataWord;

public class Utils {

    public static final int getMinWordsCount(Context context) {
        int ret = 100000;
        for (WordType type : WordType.values()) {
            ret = Math.min(ret, (int) DataWord.getCount(type.name()));
        }
        return ret;
    }

    public static final String getWordsInfo(Context context) {
        StringBuilder sb = new StringBuilder();
        for (WordType type : WordType.values()) {
            sb.append(context.getString(type.str, String.valueOf(DataWord.getCount(type.name()))));
        }
        return context.getString(R.string.words_info, sb.toString());
    }

    public static void openUrl(Context context, String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        try {
            context.startActivity(i);
        } catch (ActivityNotFoundException e) {
            L.e(e);
        }
    }

    public static boolean isVisible(final View view) {
        return view != null && view.getVisibility() == View.VISIBLE;
    }

    public static void setVisibility(final View view, final boolean visible) {
        if (view != null
                && ((visible && view.getVisibility() != View.VISIBLE) || (!visible && view.getVisibility() != View.GONE))) {
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    public static void setVisibility(final View root, final int viewId, final boolean visible) {
        if (root != null) {
            setVisibility(root.findViewById(viewId), visible);
        }
    }

    public static void setVisibility(final Activity root, final int viewId, final boolean visible) {
        if (root != null) {
            setVisibility(root.findViewById(viewId), visible);
        }
    }

    // 0 - минут
    // 1 - минута
    // 2 - минуты
    // ['минут','минута','минуты'];
    public static int numEnding(int number) {

        int[] endings = new int[]{R.string.stat_title_1,
                R.string.stat_title_2,
                R.string.stat_title_3};
        int num100 = number % 100;
        int num10 = number % 10;
        if (num100 >= 5 && num100 <= 20) {
            return endings[0];
        } else if (num10 == 0) {
            return endings[0];
        } else if (num10 == 1) {
            return endings[1];
        } else if (num10 >= 2 && num10 <= 4) {
            return endings[2];
        } else if (num10 >= 5 && num10 <= 9) {
            return endings[0];
        } else {
            return endings[2];
        }
    }


}
