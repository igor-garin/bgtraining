package ru.igarin.bgtraining.ui;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

import ru.igarin.bgtraining.R;

public class RanDirView extends LinearLayout {

    private static Random randomGenerator = new Random();

    private int[] dir_images = {R.drawable.down, R.drawable.up, R.drawable.aside};
    private int[] dir_texts = {R.string.down_desk, R.string.up_desk, R.string.side_desk};

    private ImageView mImageView;
    private TextView mTextView;

    public RanDirView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_dir, this);

        mImageView = (ImageView) this.findViewById(R.id.dir_image);
        mTextView = (TextView) this.findViewById(R.id.dir_text);
        changeDir();
    }

    public void changeDir() {

        mTextView.setVisibility(View.INVISIBLE);
        mImageView.setImageResource(R.drawable.clock);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                int id = randomGenerator.nextInt(3);
                mTextView.setText(dir_texts[id]);
                mImageView.setImageResource(dir_images[id]);
                mTextView.setVisibility(View.VISIBLE);
            }
        }, 300);

    }
}
