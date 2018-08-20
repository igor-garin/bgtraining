package ru.igarin.bgtraining.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;

public class AnimationUtils {

    private static final int ANIMATION_DURATION = 300;

    private static void expandInner(final View view, final View parentView, final ViewGroup rootContainer,
                                    final int targtetHeight) {

        setHeight(view, 0);
        view.setVisibility(View.VISIBLE);
        final Animation animation = new Animation() {

            private final Rect rect = new Rect();
            private final Rect parentVisibleRect = new Rect();

            ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;

            private final Runnable checkViewRunnable = new Runnable() {
                @Override
                public void run() {
                    checkForViewInsideVisibleArea();
                }
            };

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {

                int neededHeight = interpolatedTime == 1 ? LayoutParams.WRAP_CONTENT
                        : (int) (targtetHeight * interpolatedTime);
                setHeight(view, neededHeight);

                final ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();

                if (globalLayoutListener == null) {
                    globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

                        @Override
                        public void onGlobalLayout() {

                            if (globalLayoutListener == null) {
                                removeGlobalLayoutListener(viewTreeObserver, this);
                            }

                            checkForViewInsideVisibleArea();
                        }
                    };

                    viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener);
                }

                if (globalLayoutListener != null && interpolatedTime == 1) {
                    runInUIThread(checkViewRunnable);
                    globalLayoutListener = null;
                }

                view.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }

            private void checkForViewInsideVisibleArea() {

                if (rootContainer.indexOfChild(parentView) == -1) {
                    return;
                }

                parentVisibleRect.left = 0;
                parentVisibleRect.top = 0;
                parentVisibleRect.right = parentView.getWidth();
                parentVisibleRect.bottom = parentView.getHeight();

                rootContainer.offsetDescendantRectToMyCoords(parentView, parentVisibleRect);

                if (parentVisibleRect.top < 0 || parentVisibleRect.bottom > rootContainer.getHeight()) {

                    rect.left = parentView.getLeft();
                    rect.top = parentView.getTop();
                    rect.right = parentView.getRight();
                    rect.bottom = parentView.getBottom();

                    parentView.requestRectangleOnScreen(rect, true);
                }
            }
        };

        animation.setDuration(ANIMATION_DURATION);
        view.startAnimation(animation);
    }

    public static void expand(final View view, final View parentView, final ViewGroup rootContainer,
                              final int targtetHeight) {
        if (view == null || parentView == null || rootContainer == null) {
            return;
        }

        view.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        expandInner(view, parentView, rootContainer, targtetHeight);
    }

    public static void expand(final View view, final ViewGroup parentView) {
        expand(view, parentView, parentView);
    }

    public static void expand(final View view, final View parentView, final ViewGroup rootContainer) {
        if (view == null || parentView == null || rootContainer == null) {
            return;
        }

        view.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        int targetHeight = view.getMeasuredHeight();
        expandInner(view, parentView, rootContainer, targetHeight);
    }

    public static void collapse(final View view) {
        collapse(view, null);
    }

    public static void collapse(final View view, final AnimationListener animationListener) {
        if (view == null) {
            return;
        }

        final int initialHeight = view.getMeasuredHeight();

        final Animation animation = new Animation() {
            @Override
            protected void applyTransformation(final float interpolatedTime, final Transformation t) {
                if (interpolatedTime == 1) {
                    view.setVisibility(View.GONE);
                } else {
                    setHeight(view, initialHeight - (int) (initialHeight * interpolatedTime));
                    view.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        animation.setDuration(ANIMATION_DURATION);
        // return animation;
        if (animationListener != null) {
            animation.setAnimationListener(animationListener);
        }
        view.startAnimation(animation);
    }

    public static void show(final View view) {
        if (Build.VERSION.SDK_INT >= 11) {
            final Animation animation = new Animation() {
                @TargetApi(11)
                @Override
                protected void applyTransformation(final float interpolatedTime, final Transformation t) {
                    view.setAlpha(interpolatedTime);
                }
            };

            animation.setDuration(ANIMATION_DURATION);
            // return animation;
            view.startAnimation(animation);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void hide(final View view, final boolean makeInvisible) {
        if (Build.VERSION.SDK_INT >= 11) {
            final Animation animation = new Animation() {
                @TargetApi(11)
                @Override
                protected void applyTransformation(final float interpolatedTime, final Transformation t) {
                    view.setAlpha(1.f - interpolatedTime);
                }
            };

            animation.setDuration(ANIMATION_DURATION);
            // return animation;
            view.startAnimation(animation);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public static void animateHeight(final View animated, final int from, final int to, final long duration,
                                     final Interpolator interpolator, final AnimationListener animationListener) {
        final Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                final int newHeight = (int) (from + (to - from) * interpolatedTime);
                setHeight(animated, newHeight);
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        animation.setDuration(duration);
        animation.setInterpolator(interpolator);
        if (animationListener != null) {
            animation.setAnimationListener(animationListener);
        }
        animated.startAnimation(animation);
    }

    public static void animateHeight(final View animated, final int from, final int to,
                                     final AnimationListener animationListener) {
        animateHeight(animated, from, to, ANIMATION_DURATION, new AnticipateOvershootInterpolator(0.4f),
                animationListener);
    }

    public static void animateRotate(final View animated, float fromDegrees, float toDegrees, AnimationListener listener) {
        if (animated == null) {
            return;
        }

        final RotateAnimation rotate = new RotateAnimation(fromDegrees, toDegrees, animated.getWidth() / 2,
                animated.getHeight() / 2);
        rotate.setDuration(ANIMATION_DURATION);
        rotate.setAnimationListener(listener);
        animated.startAnimation(rotate);
    }

    public static void setHeight(final View view, final int height) {
        if (view != null) {
            final ViewGroup.LayoutParams params = view.getLayoutParams();
            // noinspection ConstantConditions
            params.height = height;
            view.setLayoutParams(params);
        }
    }

    public static void setHeight(final View root, final int viewId, final int height) {
        if (root != null) {
            setHeight(root.findViewById(viewId), height);
        }
    }

    public static void setHeight(final Activity root, final int viewId, final int height) {
        if (root != null) {
            setHeight(root.findViewById(viewId), height);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void removeGlobalLayoutListener(final ViewTreeObserver viewTreeObserver,
                                                  final ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (viewTreeObserver == null) {
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            viewTreeObserver.removeGlobalOnLayoutListener(listener);
        } else {
            viewTreeObserver.removeOnGlobalLayoutListener(listener);
        }
    }

    public static void removeGlobalLayoutListener(final View view,
                                                  final ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (view == null) {
            return;
        }
        removeGlobalLayoutListener(view.getViewTreeObserver(), listener);
    }

    public static Handler runInUIThread(final Runnable runnable) {
        return runInUIThread(runnable, false);
    }

    public static Handler runInUIThread(final Runnable runnable, boolean runImmediatelyIfPossible) {
        if (runnable == null) {
            return null;
        }

        final Handler handler;
        Looper mainLooper = Looper.getMainLooper();
        if (runImmediatelyIfPossible && (Thread.currentThread() == mainLooper.getThread())) {
            handler = null;
            runnable.run();
        } else {
            handler = new Handler(mainLooper);
            handler.post(runnable);
        }

        return handler;
    }
}
