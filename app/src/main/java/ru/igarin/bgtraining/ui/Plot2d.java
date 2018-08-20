package ru.igarin.bgtraining.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class Plot2d extends View {

    public Plot2d(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Paint paintAxis;
    private Paint paintAxisMind;
    private Paint paintAxisText;
    private Paint paintPlot;
    private Paint paintPlotDotes;

    private float[] xvalues = new float[0];
    private float[] yvalues = new float[0];

    private float[] xAxisvalues = new float[0];
    private float[] yAxisvalues = new float[0];

    private float maxx, maxy;
    private int vectorLength;

    private ConfigBuilder config = new ConfigBuilder();
    private int maxTextBounds = 0;
    private Rect bounds = new Rect();
    private final int PADD = 10;
    private int xAxisTextHeight = 0;

    public void init(float[] yvalues, ConfigBuilder cfg) {
        if (cfg != null) {
            config = cfg;
        }
        this.yvalues = yvalues;
        this.vectorLength = yvalues.length;
        this.xvalues = new float[vectorLength];
        for (int i = 0; i < yvalues.length; i++) {
            this.xvalues[i] = i + 1;
        }
        this.maxx = getMax(xvalues);
        this.maxy = getMax(yvalues);

        initPaint();
        initAxisValues();
        maxTextBounds = getMaxTextBounds() + PADD;
        paintAxisText.getTextBounds(config.getXAxisText(), 0, config.getXAxisText().length(), bounds);
        xAxisTextHeight = bounds.height() + PADD;
    }

    private void initAxisValues() {
        int n = config.getNumSubAxis();
        this.xAxisvalues = new float[n];
        this.yAxisvalues = new float[n];

        for (int i = 0; i < n - 1; i++) {
            xAxisvalues[i] = Math.round(10 * (i + 1) * (maxx) / n) / 10;
            yAxisvalues[i] = Math.round(10 * (i + 1) * maxy / n) / 10;
        }
        xAxisvalues[n - 1] = maxx;
        yAxisvalues[n - 1] = maxy;
    }

    private void initPaint() {
        paintAxis = new Paint();
        paintAxis.setColor(Color.BLACK);
        paintAxis.setStrokeWidth(2);

        paintAxisMind = new Paint();
        paintAxisMind.setColor(Color.BLACK);
        paintAxisMind.setStrokeWidth(1);

        paintAxisText = new Paint();
        paintAxisText.setColor(Color.BLACK);
        paintAxisText.setTextAlign(Paint.Align.CENTER);
        paintAxisText.setTextSize(20.0f);
        paintAxisText.setAntiAlias(true);
        paintAxisText.setSubpixelText(true);
        paintAxisText.setStrokeWidth(3);

        paintPlot = new Paint();
        paintPlot.setStrokeWidth(3);
        paintPlot.setColor(Color.RED);
        paintPlot.setAntiAlias(true);

        paintPlotDotes = new Paint();
        paintPlotDotes.setStrokeWidth(1);
        paintPlotDotes.setColor(Color.RED);
        paintPlotDotes.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        float canvasHeight = getHeight();
        float canvasWidth = getWidth();
        int[] xvaluesInPixels = toPixelX(canvasWidth, maxx, xvalues);
        int[] yvaluesInPixels = toPixelY(canvasHeight, maxy, yvalues);
        int locxAxis = toPixelIntY(canvasHeight, maxy, 0);
        int locyAxis = toPixelIntX(canvasWidth, maxx, 0);

        canvas.drawColor(config.getBackroundColor());

        // axis
        int paddingHeight = (int) (canvasHeight * config.getPadding());
        int paddingWidth = (int) (canvasWidth * config.getPadding());

        // axis x
        canvas.drawLine(locyAxis, canvasHeight - locxAxis, canvasWidth - paddingWidth, canvasHeight - locxAxis,
                paintAxis);
        // axis y
        canvas.drawLine(locyAxis, paddingHeight, locyAxis, canvasHeight - locxAxis, paintAxis);

        // null
        float x0 = locyAxis - maxTextBounds * 0.5f;
        float y0 = canvasHeight - locxAxis + maxTextBounds * 0.5f;
        paintAxisText.getTextBounds(getText(0), 0, getText(0).length(), bounds);
        canvas.drawText(getText(0), x0, y0 + (bounds.bottom - bounds.top) / 2, paintAxisText);

        // sub axis
        for (int i = 0; i < xAxisvalues.length; i++) {
            drawSubAxis(canvas, xAxisvalues[i], yAxisvalues[i]);
        }

        // plot
        for (int i = 0; i < vectorLength - 1; i++) {
            canvas.drawLine(xvaluesInPixels[i], canvasHeight - yvaluesInPixels[i], xvaluesInPixels[i + 1], canvasHeight
                    - yvaluesInPixels[i + 1], paintPlot);
        }
        for (int i = 0; i < vectorLength; i++) {
            canvas.drawCircle(xvaluesInPixels[i], canvasHeight - yvaluesInPixels[i], config.getDotRadius(),
                    paintPlotDotes);
        }

        // x text axis
        x0 = canvasWidth * 0.5f;
        y0 = canvasHeight - paddingHeight - xAxisTextHeight * 0.5f;
        paintAxisText.getTextBounds(config.getXAxisText(), 0, config.getXAxisText().length(), bounds);
        canvas.drawText(config.getXAxisText(), x0, y0 + (bounds.bottom - bounds.top) / 2, paintAxisText);

    }

    private void drawSubAxis(Canvas canvas, float x, float y) {
        float canvasHeight = getHeight();
        float canvasWidth = getWidth();
        int locxAxis = toPixelIntY(canvasHeight, maxy, 0);
        int locyAxis = toPixelIntX(canvasWidth, maxx, 0);
        int paddingHeight = (int) (canvasHeight * config.getPadding());
        int paddingWidth = (int) (canvasWidth * config.getPadding());

        int xx = toPixelIntX(canvasWidth, maxx, x);
        int yy = toPixelIntY(canvasHeight, maxy, y);

        float x0 = xx;
        float y0 = canvasHeight - locxAxis + maxTextBounds * 0.5f;
        paintAxisText.getTextBounds(getText(x), 0, getText(x).length(), bounds);
        canvas.drawText(getText(x), x0, y0 + (bounds.bottom - bounds.top) / 2, paintAxisText);

        x0 = locyAxis - maxTextBounds * 0.5f;
        y0 = canvasHeight - yy;
        paintAxisText.getTextBounds(getText(x), 0, getText(x).length(), bounds);
        canvas.drawText(getText(y), x0, y0 + (bounds.bottom - bounds.top) / 2, paintAxisText);

        // axis x
        x0 = locyAxis;
        y0 = canvasHeight - yy;
        float x1 = canvasWidth - paddingWidth;
        float y1 = canvasHeight - yy;
        canvas.drawLine(x0, y0, x1, y1, paintAxisMind);

        // axis y
        x0 = xx;
        y0 = paddingHeight;
        x1 = xx;
        y1 = canvasHeight - locxAxis;
        canvas.drawLine(x0, y0, x1, y1, paintAxisMind);

    }

    private String getText(float value) {
        return String.format("%.0f", value);
    }

    private int[] toPixelY(float pixels, float max, float[] value) {

        int[] pint = new int[value.length];
        for (int i = 0; i < value.length; i++) {
            pint[i] = toPixelIntY(pixels, max, value[i]);
        }

        return (pint);
    }

    private int[] toPixelX(float pixels, float max, float[] value) {

        int[] pint = new int[value.length];
        for (int i = 0; i < value.length; i++) {
            pint[i] = toPixelIntX(pixels, max, value[i]);
        }

        return (pint);
    }

    private int toPixelIntX(float pixels, float max, float value) {
        double p;
        int pint;

        float new_max = max + max * config.getTopPadding();
        float textPart = maxTextBounds / pixels;

        p = maxTextBounds + config.getPadding() * pixels + (value / new_max)
                * (1.0f - config.getPadding() * 2 - textPart) * pixels;

        pint = (int) p;
        return (pint);
    }

    private int toPixelIntY(float pixels, float max, float value) {
        double p;
        int pint;

        float new_max = max + max * config.getTopPadding();
        float textPart = (maxTextBounds + xAxisTextHeight) / pixels;

        p = xAxisTextHeight + maxTextBounds + config.getPadding() * pixels + (value / new_max)
                * (1.0f - config.getPadding() * 2 - textPart) * pixels;

        pint = (int) p;
        return (pint);
    }

    private float getMax(float[] v) {
        float largest = 0;
        for (int i = 0; i < v.length; i++)
            if (v[i] > largest)
                largest = v[i];
        return largest;
    }

    public static class ConfigBuilder {

        private int backroundColor = Color.WHITE;
        private int plotColor = Color.RED;
        private float padding = 0.05f;
        private int numSubAxis = 4;
        private float topPadding = 0.1f;
        private int dotRadius = 4;
        private String xAxisText = "Day";

        public int getBackroundColor() {
            return backroundColor;
        }

        public int getPlotColor() {
            return plotColor;
        }

        public float getPadding() {
            return padding;
        }

        public int getNumSubAxis() {
            return numSubAxis;
        }

        public float getTopPadding() {
            return topPadding;
        }

        public int getDotRadius() {
            return dotRadius;
        }

        public String getXAxisText() {
            return xAxisText;
        }

        public ConfigBuilder() {

        }

        public ConfigBuilder setNumSubAxis(int numSubAxis) {
            this.numSubAxis = numSubAxis;
            return this;
        }

        public ConfigBuilder setBackroundColor(int backroundColor) {
            this.backroundColor = backroundColor;
            return this;
        }

        public ConfigBuilder setXAxisText(String xAxisText) {
            this.xAxisText = xAxisText;
            return this;
        }
    }

    private int getMaxTextBounds() {

        int max = 0;
        for (int i = 0; i < xAxisvalues.length; i++) {
            String num = getText(xAxisvalues[i]);
            paintAxisText.getTextBounds(num, 0, num.length(), bounds);
            if (max < bounds.height()) {
                max = bounds.height();
            }
            if (max < bounds.width()) {
                max = bounds.width();
            }
        }
        for (int i = 0; i < yAxisvalues.length; i++) {
            String num = getText(yAxisvalues[i]);
            paintAxisText.getTextBounds(num, 0, num.length(), bounds);
            if (max < bounds.height()) {
                max = bounds.height();
            }
            if (max < bounds.width()) {
                max = bounds.width();
            }
        }
        String num = getText(0);
        paintAxisText.getTextBounds(num, 0, num.length(), bounds);
        if (max < bounds.height()) {
            max = bounds.height();
        }
        if (max < bounds.width()) {
            max = bounds.width();
        }
        return max;
    }

}
