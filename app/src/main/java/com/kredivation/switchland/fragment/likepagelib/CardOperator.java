package com.kredivation.switchland.fragment.likepagelib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.support.v4.view.VelocityTrackerCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

public class CardOperator {
    GestureDetector.SimpleOnGestureListener cardGestureListener;
    private final GestureDetector gestureDetector;
    private boolean isSwipedOffScreen;
    private ObjectAnimator currentCardAnimator;
    private float initialTouchX;
    private float initialTouchY;
    private int activePointerId;
    private boolean isBeingDragged;
    private float initialCardPositionX;
    private float initialCardPositionY;
    private AnimationCycleJava animationCycle;
    private VelocityTracker velocityTrecker;
    private final KoldaMain koloda;
    private final View cardView;
    private final int adapterPosition;
    private final CardCallback cardCallback;
    private static final int DEFAULT_OFF_SCREEN_ANIMATION_DURATION = 600;
    private static final int DEFAULT_OFF_SCREEN_FLING_ANIMATION_DURATION = 150;
    private static final int DEFAULT_RESET_ANIMATION_DURATION = 600;

    public final boolean isBeingDragged$production_sources_for_module_app() {
        return this.isBeingDragged;
    }

    public final void setBeingDragged$production_sources_for_module_app(boolean var1) {
        this.isBeingDragged = var1;
    }

    public final void animateOffScreenLeft(int duration, boolean notify, boolean isClicked) {
        float targetY = this.cardView.getY();
        float var10000;
        if (this.initialCardPositionY > this.cardView.getY()) {
            var10000 = targetY - (this.initialCardPositionY - this.cardView.getY());
        } else {
            var10000 = targetY + (this.cardView.getY() - this.initialCardPositionY) * (float) 3;
        }

        float maxCardWidth = this.koloda.getMaxCardWidth(this.cardView);
        float transitionX = (float) (this.koloda.getParentWidth$production_sources_for_module_app() / 2 + this.cardView.getWidth() / 2) + this.cardView.getX() + Math.abs(maxCardWidth / (float) 2);
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("x", new float[]{this.cardView.getX(), this.cardView.getX() - transitionX});
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", new float[]{this.cardView.getY(), this.cardView.getY() * (float) 2});
        Intrinsics.checkExpressionValueIsNotNull(pvhX, "pvhX");
        Intrinsics.checkExpressionValueIsNotNull(pvhY, "pvhY");
        this.animateCardOffScreen(duration, pvhX, pvhY);
        if (!isClicked) {
            this.cardCallback.onCardSwipedLeft(this.adapterPosition, this.cardView, notify);
        } else {
            this.cardCallback.onCardMovedOnClickLeft(this.adapterPosition, this.cardView, notify);
        }

    }

    public final void animateOffScreenRight(int duration, boolean notify, boolean isClicked) {
        float targetY = this.cardView.getY();
        float var10000;
        if (this.initialCardPositionY > this.cardView.getY()) {
            var10000 = targetY - (this.initialCardPositionY - this.cardView.getY());
        } else {
            var10000 = targetY + (this.cardView.getY() - this.initialCardPositionY) * (float) 3;
        }

        float maxCardWidth = this.koloda.getMaxCardWidth(this.cardView);
        float transitionX = (float) (this.koloda.getParentWidth$production_sources_for_module_app() - (this.koloda.getParentWidth$production_sources_for_module_app() / 2 - this.cardView.getWidth() / 2)) + this.cardView.getX() + Math.abs(maxCardWidth / (float) 2);
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("x", new float[]{this.cardView.getX(), this.cardView.getX() + transitionX});
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", new float[]{this.cardView.getY(), this.cardView.getY() * (float) 2});
        Intrinsics.checkExpressionValueIsNotNull(pvhX, "pvhX");
        Intrinsics.checkExpressionValueIsNotNull(pvhY, "pvhY");
        this.animateCardOffScreen(duration, pvhX, pvhY);
        if (!isClicked) {
            this.cardCallback.onCardSwipedRight(this.adapterPosition, this.cardView, notify);
        } else {
            this.cardCallback.onCardMovedOnClickRight(this.adapterPosition, this.cardView, notify);
        }

    }

    private final void animateCardOffScreen(int duration, PropertyValuesHolder pvhX, PropertyValuesHolder pvhY) {
        this.swipedCardOffScreen();
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setValues(new PropertyValuesHolder[]{pvhX, pvhY});
        valueAnimator.addUpdateListener((ValueAnimator.AnimatorUpdateListener) (new ValueAnimator.AnimatorUpdateListener() {
            public final void onAnimationUpdate(ValueAnimator it) {
                View var10000 = CardOperator.this.getCardView();
                Object var10001 = it.getAnimatedValue("x");
                if (var10001 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.Float");
                } else {
                    var10000.setTranslationX((Float) var10001 - CardOperator.this.initialCardPositionX);
                    var10000 = CardOperator.this.getCardView();
                    var10001 = it.getAnimatedValue("y");
                    if (var10001 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type kotlin.Float");
                    } else {
                        var10000.setTranslationY((Float) var10001 - CardOperator.this.initialCardPositionY);
                    }
                }
            }
        }));
        valueAnimator.addListener((Animator.AnimatorListener) (new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                Intrinsics.checkParameterIsNotNull(animation, "animation");
                super.onAnimationEnd(animation);
                CardOperator.this.getCardCallback().onCardOffScreen(CardOperator.this.getAdapterPosition(), CardOperator.this.getCardView());
            }
        }));
        valueAnimator.start();
    }

    private final void checkCardPosition() {
        if (this.cardBeyondLeftBorder()) {
            this.animateOffScreenLeft(600, true, false);
        } else if (this.cardBeyondRightBorder()) {
            this.animateOffScreenRight(600, true, false);
        } else {
            this.resetCardPosition(DEFAULT_RESET_ANIMATION_DURATION);
        }

    }

    private final void resetCardPosition(int duration) {
        this.currentCardAnimator = ObjectAnimator.ofPropertyValuesHolder(this.cardView, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat(View.X, new float[]{this.initialCardPositionX}), PropertyValuesHolder.ofFloat(View.Y, new float[]{this.initialCardPositionY}), PropertyValuesHolder.ofFloat(View.ROTATION, new float[]{0.0F})}).setDuration((long) duration);
        ObjectAnimator var10000 = this.currentCardAnimator;
        if (this.currentCardAnimator != null) {
            var10000.setDuration(200L);
        }

        var10000 = this.currentCardAnimator;
        if (this.currentCardAnimator != null) {
            var10000.addUpdateListener((ValueAnimator.AnimatorUpdateListener) (new ValueAnimator.AnimatorUpdateListener() {
                public final void onAnimationUpdate(ValueAnimator it) {
                    CardOperator.this.updateCardProgress();
                }
            }));
        }

        var10000 = this.currentCardAnimator;
        if (this.currentCardAnimator != null) {
            var10000.addListener((Animator.AnimatorListener) (new AnimatorListenerAdapter() {
                public void onAnimationStart(Animator animation, boolean isReverse) {
                    super.onAnimationStart(animation, isReverse);
                    CardOperator.this.animationCycle = AnimationCycleJava.ANIMATION_IN_PROGRESS;
                }

                public void onAnimationEnd(Animator animation) {
                    Intrinsics.checkParameterIsNotNull(animation, "animation");
                    CardOperator.this.setBeingDragged$production_sources_for_module_app(false);
                    CardOperator.this.animationCycle = AnimationCycleJava.NO_ANIMATION;
                    super.onAnimationEnd(animation);
                }
            }));
        }

        var10000 = this.currentCardAnimator;
        if (this.currentCardAnimator != null) {
            var10000.start();
        }

    }

    private final void swipedCardOffScreen() {
        this.isSwipedOffScreen = true;
        this.cardView.setEnabled(false);
    }

    private final void updateCardProgress() {
        float sideProgress = (this.cardView.getX() + (float) (this.cardView.getWidth() / 2) - (float) (this.koloda.getParentWidth$production_sources_for_module_app() / 2)) / (float) (this.koloda.getParentWidth$production_sources_for_module_app() / 2);
        if (sideProgress > 1.0F) {
            sideProgress = 1.0F;
        } else if (sideProgress < -1.0F) {
            sideProgress = -1.0F;
        }

        this.updateCardProgress(sideProgress);
    }

    private final void updateCardProgress(float sideProgress) {
        this.cardCallback.onCardDrag(this.adapterPosition, this.cardView, sideProgress);
        float cardOffsetProgress = Math.max(Math.abs(this.cardView.getX() / (float) this.cardView.getWidth()), Math.abs(this.cardView.getY() / (float) this.cardView.getHeight()));
        if (!this.isSwipedOffScreen || cardOffsetProgress <= (float) 1) {
            this.cardCallback.onCardOffset(this.adapterPosition, this.cardView, sideProgress);
        }

    }

    private final boolean cardBeyondLeftBorder() {
        return this.cardView.getX() + (float) (this.cardView.getWidth() / 2) < (float) this.koloda.getParentWidth$production_sources_for_module_app() / 4.0F;
    }

    private final boolean cardBeyondRightBorder() {
        return this.cardView.getX() + (float) (this.cardView.getWidth() / 2) > (float) this.koloda.getParentWidth$production_sources_for_module_app() - (float) this.koloda.getParentWidth$production_sources_for_module_app() / 4.0F;
    }

    private final boolean onTouchView(View view, MotionEvent event) {
        if (!this.koloda.canSwipe(this.cardView)) {
            return false;
        } else if (this.gestureDetector.onTouchEvent(event)) {
            return true;
        } else {
            int action = event.getActionMasked();
            VelocityTracker var10000;
            int pointerId;
            int pointerIndex;
            switch (action) {
                case 0:
                    if (this.velocityTrecker == null) {
                        this.velocityTrecker = VelocityTracker.obtain();
                    } else {
                        var10000 = this.velocityTrecker;
                        if (this.velocityTrecker != null) {
                            var10000.clear();
                        }
                    }

                    if (this.animationCycle == AnimationCycleJava.NO_ANIMATION && !this.isBeingDragged) {
                        this.isBeingDragged = true;
                        this.cardCallback.onCardActionDown(this.adapterPosition, this.cardView);
                        ObjectAnimator var11 = this.currentCardAnimator;
                        if (this.currentCardAnimator != null) {
                            var11.cancel();
                        }

                        this.initialCardPositionX = this.cardView.getX();
                        this.initialCardPositionY = this.cardView.getY();
                        pointerId = event.getActionIndex();
                        this.initialTouchX = event.getX(pointerId);
                        this.initialTouchY = event.getY(pointerId);
                        this.activePointerId = event.getPointerId(0);
                    }
                    break;
                case 1:
                case 3:
                    this.activePointerId = -1;
                    this.checkCardPosition();
                    this.cardCallback.onCardActionUp(this.adapterPosition, this.cardView, this.cardBeyondLeftBorder() || this.cardBeyondRightBorder());
                    break;
                case 2:
                    var10000 = this.velocityTrecker;
                    if (this.velocityTrecker != null) {
                        var10000.addMovement(event);
                    }

                    pointerId = event.getPointerId(event.getActionIndex());
                    var10000 = this.velocityTrecker;
                    if (this.velocityTrecker != null) {
                        var10000.computeCurrentVelocity(pointerId, 40.0F);
                    }

                    pointerIndex = event.findPointerIndex(this.activePointerId);
                    if (pointerIndex != -1) {
                        float dx = event.getX(pointerIndex) - this.initialTouchX;
                        float dy = event.getY(pointerIndex) - this.initialTouchY;
                        float posX = this.cardView.getX() + dx + Math.abs(VelocityTrackerCompat.getXVelocity(this.velocityTrecker, pointerId));
                        float posY = this.cardView.getY() + dy + Math.abs(VelocityTrackerCompat.getYVelocity(this.velocityTrecker, pointerId));
                        this.cardView.setX(posX);
                        this.cardView.setY(posY);
                        this.updateCardProgress();
                    }
                case 4:
                case 5:
                default:
                    break;
                case 6:
                    pointerId = event.getActionIndex();
                    pointerIndex = event.getPointerId(pointerId);
                    if (pointerIndex == this.activePointerId) {
                        int newPointerIndex = pointerId == 0 ? 1 : 0;
                        this.initialTouchX = event.getX(newPointerIndex);
                        this.initialTouchY = event.getY(newPointerIndex);
                        this.activePointerId = event.getPointerId(newPointerIndex);
                    }
            }

            return true;
        }
    }

    public final void onClickRight$production_sources_for_module_app() {
        this.initialCardPositionX = this.cardView.getX();
        this.initialCardPositionY = this.cardView.getY();
        this.animateOffScreenRight(600, true, true);
    }

    public final void onClickLeft$production_sources_for_module_app() {
        this.initialCardPositionX = this.cardView.getX();
        this.initialCardPositionY = this.cardView.getY();
        this.animateOffScreenLeft(600, true, true);
    }

    public final void onClickSkip$production_sources_for_module_app() {
        this.initialCardPositionX = this.cardView.getPivotX();
        this.initialCardPositionY = this.cardView.getPivotY();
        this.animateOffScreenLeft(600, true, true);
    }

    public final KoldaMain getKoloda() {
        return this.koloda;
    }

    public final View getCardView() {
        return this.cardView;
    }

    public final int getAdapterPosition() {
        return this.adapterPosition;
    }

    public final CardCallback getCardCallback() {
        return this.cardCallback;
    }

    public CardOperator(KoldaMain koloda, View cardView, int adapterPosition, CardCallback cardCallback) {
        super();
        Intrinsics.checkParameterIsNotNull(koloda, "koloda");
        Intrinsics.checkParameterIsNotNull(cardView, "cardView");
        Intrinsics.checkParameterIsNotNull(cardCallback, "cardCallback");
        this.koloda = koloda;
        this.cardView = cardView;
        this.adapterPosition = adapterPosition;
        this.cardCallback = cardCallback;
        cardGestureListener = new GestureDetector.SimpleOnGestureListener() {
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if ((e1 != null ? e1.getX() : 0.0F) > (e2 != null ? e2.getX() : 0.0F) && CardOperator.this.cardBeyondLeftBorder()) {
                    CardOperator.this.getCardCallback().onCardActionUp(CardOperator.this.getAdapterPosition(), CardOperator.this.getCardView(), true);
                    CardOperator.this.animateOffScreenLeft(CardOperator.DEFAULT_OFF_SCREEN_FLING_ANIMATION_DURATION, true, false);
                    return true;
                } else if ((e1 != null ? e1.getX() : 0.0F) < (e2 != null ? e2.getX() : 0.0F) && CardOperator.this.cardBeyondRightBorder()) {
                    CardOperator.this.getCardCallback().onCardActionUp(CardOperator.this.getAdapterPosition(), CardOperator.this.getCardView(), true);
                    CardOperator.this.animateOffScreenRight(CardOperator.DEFAULT_OFF_SCREEN_FLING_ANIMATION_DURATION, true, false);
                    return true;
                } else {
                    return false;
                }
            }

            public boolean onDoubleTap(MotionEvent e) {
                CardOperator.this.getCardCallback().onCardDoubleTap(CardOperator.this.getAdapterPosition(), CardOperator.this.getCardView());
                return super.onDoubleTap(e);
            }

            public boolean onSingleTapConfirmed(MotionEvent e) {
                CardOperator.this.getCardCallback().onCardSingleTap(CardOperator.this.getAdapterPosition(), CardOperator.this.getCardView());
                return super.onSingleTapConfirmed(e);
            }

            public void onLongPress(MotionEvent e) {
                CardOperator.this.getCardCallback().onCardLongPress(CardOperator.this.getAdapterPosition(), CardOperator.this.getCardView());
                super.onLongPress(e);
            }
        };
        this.gestureDetector = new GestureDetector(this.cardView.getContext(), this.cardGestureListener, (Handler) null, true);
        this.animationCycle = AnimationCycleJava.NO_ANIMATION;
        this.cardView.setOnTouchListener((View.OnTouchListener) (new View.OnTouchListener() {
            public final boolean onTouch(View view, MotionEvent event) {
                CardOperator var10000 = CardOperator.this;
                Intrinsics.checkExpressionValueIsNotNull(view, "view");
                Intrinsics.checkExpressionValueIsNotNull(event, "event");
                return var10000.onTouchView(view, event);
            }
        }));
    }

    // $FF: synthetic method
    public static final void access$setInitialCardPositionX$p(CardOperator $this, float var1) {
        $this.initialCardPositionX = var1;
    }

    // $FF: synthetic method
    public static final void access$setInitialCardPositionY$p(CardOperator $this, float var1) {
        $this.initialCardPositionY = var1;
    }

    // $FF: synthetic method
    public static final AnimationCycleJava access$getAnimationCycle$p(CardOperator $this) {
        return $this.animationCycle;
    }
}

