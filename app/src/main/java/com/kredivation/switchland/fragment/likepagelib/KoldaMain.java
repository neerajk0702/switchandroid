package com.kredivation.switchland.fragment.likepagelib;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Adapter;
import android.widget.FrameLayout;

import com.kredivation.switchland.R;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.IntIterator;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.ranges.RangesKt;

public class KoldaMain extends FrameLayout {
    private int maxVisibleCards;
    private int cardPositionOffsetX;
    private int cardPositionOffsetY;
    private float cardRotationDegrees;
    private HashSet dyingViews;
    private LinkedHashSet activeViews;
    private DataSetObserver dataSetObservable;
    private boolean isNeedCircleLoading;
    @Nullable
    private Adapter adapter;
    private int adapterPosition;
    private HashMap deckMap;
    @Nullable
    private KoldaListnerJava kolodaListener;
    private int parentWidth;
    private boolean swipeEnabled;
    private CardCallback cardCallback;
    private static final int DEFAULT_MAX_VISIBLE_CARDS = 3;
    private static final float DEFAULT_ROTATION_ANGLE = 30.0F;
    private static final float DEFAULT_SCALE_DIFF = 0.04F;
    private HashMap _$_findViewCache;

    public final boolean isNeedCircleLoading() {
        return this.isNeedCircleLoading;
    }

    public final void setNeedCircleLoading(boolean var1) {
        this.isNeedCircleLoading = var1;
    }

    @Nullable
    public final Adapter getAdapter() {
        return this.adapter;
    }

    public final void setAdapter(@Nullable Adapter value) {
        DataSetObserver var10000 = this.dataSetObservable;
        Adapter var5;
        if (this.dataSetObservable != null) {
            DataSetObserver var2 = var10000;
            var5 = this.adapter;
            if (this.adapter != null) {
                var5.unregisterDataSetObserver(var2);
            }
        }

        this.removeAllViews();
        this.adapter = value;
        var5 = this.adapter;
        if (this.adapter != null) {
            var5.registerDataSetObserver(this.createDataSetObserver());
        }

        var10000 = this.dataSetObservable;
        if (this.dataSetObservable != null) {
            var10000.onChanged();
        }

    }

    @Nullable
    public final KoldaListnerJava getKolodaListener() {
        return this.kolodaListener;
    }

    public final void setKolodaListener(@Nullable KoldaListnerJava var1) {
        this.kolodaListener = var1;
    }

    public final int getParentWidth$production_sources_for_module_app() {
        return this.parentWidth;
    }

    public final void setParentWidth$production_sources_for_module_app(int var1) {
        this.parentWidth = var1;
    }

    private final void init(AttributeSet attrs) {
        TypedArray a = this.getContext().obtainStyledAttributes(attrs, R.styleable.Koloda);
        int cardLayoutId = a.getResourceId(0, -1);
        this.maxVisibleCards = a.getInt(R.styleable.Koloda_koloda_max_visible_cards, DEFAULT_MAX_VISIBLE_CARDS);
        this.cardPositionOffsetX = a.getDimensionPixelSize(R.styleable.Koloda_koloda_card_offsetX, this.getResources().getDimensionPixelSize(R.dimen.default_card_spacing));
        this.cardPositionOffsetY = a.getDimensionPixelSize(R.styleable.Koloda_koloda_card_offsetY, this.getResources().getDimensionPixelSize(R.dimen.default_card_spacing));
        this.cardRotationDegrees = a.getFloat(R.styleable.Koloda_koloda_card_rotate_angle, DEFAULT_ROTATION_ANGLE);

        a.recycle();
        if (this.isInEditMode()) {
            LayoutInflater.from(this.getContext()).inflate(cardLayoutId, (ViewGroup) this, true);
        }

    }

    private final void addCardToDeck() {
        if (this.adapterPosition < (this.adapter != null ? this.adapter.getCount() : 0)) {
            View newCard = this.adapter != null ? this.adapter.getView(this.adapterPosition, (View) null, (ViewGroup) this) : null;
            this.initializeCardPosition(newCard);
            if (newCard != null) {
                this.addView(newCard, 0);
                HashMap var10000 = this.deckMap;
                int var4 = this.adapterPosition++;
                CardOperator var6 = (CardOperator) var10000.put(newCard, new CardOperator(this, newCard, var4, this.cardCallback));
            }
        } else if (this.isNeedCircleLoading) {
            this.adapterPosition = 0;
            this.addCardToDeck();
        }

    }

    private final void initializeCardPosition(View view) {
        int childCount = this.getChildCount() - this.dyingViews.size();
        this.scaleView(view, 0.0F, childCount);
        if (view != null) {
            view.setTranslationY((float) (this.cardPositionOffsetY * childCount));
        }

        Log.i("----> elem init", String.valueOf(childCount));
        Log.i("----> translation init", String.valueOf(view != null ? view.getTranslationY() : null));
        this.setZTranslations(childCount);
    }

    private final void checkTopCard() {
        int childCount = this.getChildCount();
        this.setZTranslations(childCount);
        KoldaListnerJava var10000;
        if (childCount - 1 - this.dyingViews.size() < 0) {
            var10000 = this.kolodaListener;
            if (this.kolodaListener != null) {
                var10000.onEmptyDeck();
            }
        } else {
            View topCard = this.getChildAt(childCount - 1 - this.dyingViews.size());
            if (this.deckMap.containsKey(topCard)) {
                CardOperator var6 = (CardOperator) this.deckMap.get(topCard);
                if (var6 != null) {
                    CardOperator var3 = var6;
                    var10000 = this.kolodaListener;
                    if (this.kolodaListener != null) {
                        var10000.onNewTopCard(var3.getAdapterPosition());
                    }
                }
            }
        }

    }

    @TargetApi(21)
    private final void setZTranslations(int childCount) {
        if (Build.VERSION.SDK_INT >= 21) {
            Iterable $receiver$iv = (Iterable) RangesKt.until(0, childCount);
            Collection destination$iv$iv = new ArrayList(10);
            Iterator var5 = $receiver$iv.iterator();

            while (var5.hasNext()) {
                int item$iv$iv = ((IntIterator) var5).nextInt();
                this.getChildAt(item$iv$iv).setTranslationZ((float) (item$iv$iv * 10));
                Unit var12 = Unit.INSTANCE;
                destination$iv$iv.add(var12);
            }

            List var10000 = (List) destination$iv$iv;
        }

    }

    public final float getMaxCardWidth(View cardView) {
        Intrinsics.checkParameterIsNotNull(cardView, "cardView");
        return (float) cardView.getHeight() * (float) Math.tan(Math.toRadians((double) this.cardRotationDegrees));
    }

    public final boolean canSwipe(View card) {
        Intrinsics.checkParameterIsNotNull(card, "card");
        Log.e("====>Swipe ", String.valueOf(this.swipeEnabled));
        Log.e("====>Active viewsempty ", String.valueOf(this.activeViews.isEmpty()));
        Log.e("====>Or contains ", String.valueOf(this.activeViews.contains(card)));
        Log.e("====>Index ", String.valueOf(this.indexOfChild(card) >= this.getChildCount() - 2));
        return this.swipeEnabled && (this.activeViews.isEmpty() || this.activeViews.contains(card)) && this.indexOfChild(card) >= this.getChildCount() - 2;
    }

    private final void updateDeckCardsPosition(float progress) {
        int visibleChildCount = Math.min(this.getChildCount(), this.maxVisibleCards + 1);
        int childCount = Math.min(this.getChildCount(), this.maxVisibleCards);
        int cardsWillBeMoved = 0;
        Ref.ObjectRef cardView = new Ref.ObjectRef();
        Iterable $receiver$iv = (Iterable) RangesKt.until(0, visibleChildCount);
        Collection destination$iv$iv = (Collection) (new ArrayList(10));
        Iterator var9 = $receiver$iv.iterator();

        View var10001;
        while (var9.hasNext()) {
            int item$iv$iv = ((IntIterator) var9).nextInt();
            var10001 = this.getChildAt(item$iv$iv);
            Intrinsics.checkExpressionValueIsNotNull(var10001, "getChildAt(it)");
            cardView.element = var10001;
            if (this.deckMap.containsKey((View) cardView.element)) {
                label38:
                {
                    CardOperator var10000 = (CardOperator) this.deckMap.get((View) cardView.element);
                    if (var10000 != null) {
                        if (var10000.isBeingDragged$production_sources_for_module_app()) {
                            break label38;
                        }
                    }

                    ++cardsWillBeMoved;
                }
            }

            this.scaleView((View) cardView.element, progress, childCount - item$iv$iv - 1);
            Unit var17 = Unit.INSTANCE;
            destination$iv$iv.add(var17);
        }

        List var19 = (List) destination$iv$iv;
        if (progress != 0.0F) {
            int i = 0;

            for (int var7 = cardsWillBeMoved; i < var7; ++i) {
                var10001 = this.getChildAt(i);
                cardView.element = var10001;
                ((View) cardView.element).setTranslationY((float) (this.cardPositionOffsetY * Math.min(cardsWillBeMoved, visibleChildCount - i - 1)) - (float) this.cardPositionOffsetY * Math.abs(progress));
            }
        }

    }

    private final void scaleView(View view, float progress, int childCount) {
        float currentScale = 1.0F - (float) childCount * 0.04F;
        float nextScale = 1.0F - (float) (childCount - 1) * 0.04F;
        float scale = currentScale + (nextScale - currentScale) * Math.abs(progress);
        if (scale <= 1.0F) {
            if (view != null) {
                view.setScaleX(scale);
            }

            if (view != null) {
                view.setScaleY(scale);
            }
        }

    }

    private final void updateCardView(View card, float sideProgress) {
        float rotation = (float) ((int) this.cardRotationDegrees) * sideProgress;
        card.setRotation(rotation);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            DataSetObserver var10000 = this.dataSetObservable;
            if (this.dataSetObservable != null) {
                var10000.onChanged();
            }
        }

    }

    public void addView(View child, int index) {
        Intrinsics.checkParameterIsNotNull(child, "child");
        child.setAlpha(0.0F);
        super.addView(child, index);
        child.animate().alpha(1.0F).setDuration(300L);
    }

    public void removeAllViews() {
        super.removeAllViews();
        this.deckMap.clear();
        this.activeViews.clear();
        this.dyingViews.clear();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ViewParent var10001 = this.getParent();
        if (var10001 == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.view.View");
        } else {
            this.parentWidth = ((View) var10001).getMeasuredWidth();
        }
    }

    private final DataSetObserver createDataSetObserver() {
        this.dataSetObservable = (DataSetObserver) (new DataSetObserver() {
            public void onChanged() {
                super.onChanged();
                this.addCards();
            }

            public void onInvalidated() {
                super.onInvalidated();
                KoldaMain.this.adapterPosition = 0;
                KoldaMain.this.removeAllViews();
                this.addCards();
            }

            private final void addCards() {
                int childCount = KoldaMain.this.getChildCount() - KoldaMain.this.dyingViews.size();
                int var2 = childCount;

                for (int var3 = KoldaMain.this.maxVisibleCards; var2 < var3; ++var2) {
                    KoldaMain.this.addCardToDeck();
                }

                KoldaMain.this.checkTopCard();
            }
        });
        DataSetObserver var10000 = this.dataSetObservable;
        if (this.dataSetObservable == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.database.DataSetObserver");
        } else {
            return var10000;
        }
    }

    public final void onClickRight() {
        int childCount = this.getChildCount();
        View topCard = this.getChildAt(childCount - 1 - this.dyingViews.size());
        if (topCard != null) {
            this.activeViews.add(topCard);
            CardOperator cardOperator = (CardOperator) this.deckMap.get(topCard);
            if (cardOperator != null) {
                cardOperator.onClickRight$production_sources_for_module_app();
            }

            topCard.setRotation(10.0F);
        }

    }

    public final void onClickLeft() {
        int childCount = this.getChildCount();
        View topCard = this.getChildAt(childCount - 1 - this.dyingViews.size());
        if (topCard != null) {
            this.activeViews.add(topCard);
            CardOperator cardOperator = (CardOperator) this.deckMap.get(topCard);
            if (cardOperator != null) {
                cardOperator.onClickLeft$production_sources_for_module_app();
            }

            topCard.setRotation(-10.0F);
        }

    }

    public final void onClickSkip() {
        int childCount = this.getChildCount();
        View topCard = this.getChildAt(childCount - 1 - this.dyingViews.size());
        if (topCard != null) {
            this.activeViews.add(topCard);
            CardOperator cardOperator = (CardOperator) this.deckMap.get(topCard);
            if (cardOperator != null) {
                cardOperator.onClickSkip$production_sources_for_module_app();
            }

            topCard.setRotation(-10.0F);
        }
    }

    private final void findPositionAfterClick() {
        int childCount = Math.min(this.getChildCount(), this.maxVisibleCards);
        Iterable $receiver$iv = (Iterable) RangesKt.until(0, childCount);
        Collection destination$iv$iv = (Collection) (new ArrayList(10));
        Iterator var5 = $receiver$iv.iterator();

        while (var5.hasNext()) {
            int item$iv$iv = ((IntIterator) var5).nextInt();
            View view = this.getChildAt(item$iv$iv);
            this.scaleView(view, 0.0F, childCount - item$iv$iv - 1);
            if (view != null) {
                view.setTranslationY((float) (this.cardPositionOffsetY * (childCount - item$iv$iv - 1)));
            }

            Unit var13 = Unit.INSTANCE;
            destination$iv$iv.add(var13);
        }

        List var10000 = (List) destination$iv$iv;
    }

    public KoldaMain(Context context) {
        this(context, (AttributeSet) null);
    }

    public KoldaMain(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KoldaMain(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.maxVisibleCards = 3;
        this.cardPositionOffsetX = this.getResources().getDimensionPixelSize(R.dimen.default_card_spacing);
        this.cardPositionOffsetY = this.getResources().getDimensionPixelSize(R.dimen.default_card_spacing);
        this.cardRotationDegrees = 30.0F;


        HashSet var5 = new HashSet();
        this.dyingViews = var5;
        LinkedHashSet var6 = new LinkedHashSet();
        this.activeViews = var6;
        HashMap var7 = new HashMap();
        this.deckMap = var7;
        this.swipeEnabled = true;
        this.cardCallback = (CardCallback) (new CardCallback() {
            public void onCardActionDown(int adapterPosition, View card) {
                KoldaMain.this.activeViews.add(card);
            }

            public void onCardDrag(int adapterPosition, View card, float sideProgress) {
                KoldaMain.this.updateCardView(card, sideProgress);
                KoldaListnerJava var10000 = KoldaMain.this.getKolodaListener();
                if (var10000 != null) {
                    var10000.onCardDrag(adapterPosition, card, sideProgress);
                }

            }

            public void onCardOffset(int adapterPosition, View card, float offsetProgress) {
                KoldaMain.this.updateDeckCardsPosition(offsetProgress);
            }

            public void onCardActionUp(int adapterPosition, View card, boolean isCardNeedRemove) {
                if (isCardNeedRemove) {
                    KoldaMain.this.activeViews.remove(card);
                }

            }

            public void onCardSwipedLeft(int adapterPosition, View card, boolean notify) {
                KoldaMain.this.dyingViews.add(card);
                DataSetObserver var10000 = KoldaMain.this.dataSetObservable;
                if (var10000 != null) {
                    var10000.onChanged();
                }

                if (notify) {
                    KoldaListnerJava var4 = KoldaMain.this.getKolodaListener();
                    if (var4 != null) {
                        var4.onCardSwipedLeft(adapterPosition);
                    }
                }

            }

            public void onCardSwipedRight(int adapterPosition, View card, boolean notify) {
                KoldaMain.this.dyingViews.add(card);
                DataSetObserver var10000 = KoldaMain.this.dataSetObservable;
                if (var10000 != null) {
                    var10000.onChanged();
                }

                if (notify) {
                    KoldaListnerJava var4 = KoldaMain.this.getKolodaListener();
                    if (var4 != null) {
                        var4.onCardSwipedRight(adapterPosition);
                    }
                }

            }

            public void onCardOffScreen(int adapterPosition, View card) {
                KoldaMain.this.dyingViews.remove(card);
                KoldaMain.this.deckMap.remove(card);
                KoldaMain.this.removeView(card);
            }

            public void onCardSingleTap(int adapterPosition, View card) {
                KoldaListnerJava var10000 = KoldaMain.this.getKolodaListener();
                if (var10000 != null) {
                    var10000.onCardSingleTap(adapterPosition);
                }

            }

            public void onCardDoubleTap(int adapterPosition, View card) {
                KoldaListnerJava var10000 = KoldaMain.this.getKolodaListener();
                if (var10000 != null) {
                    var10000.onCardDoubleTap(adapterPosition);
                }

            }

            public void onCardLongPress(int adapterPosition, View card) {
                KoldaListnerJava var10000 = KoldaMain.this.getKolodaListener();
                if (var10000 != null) {
                    var10000.onCardLongPress(adapterPosition);
                }

            }

            public void onCardMovedOnClickRight(int adapterPosition, View card, boolean notify) {
                KoldaMain.this.activeViews.remove(card);
                KoldaMain.this.dyingViews.add(card);
                DataSetObserver var10000 = KoldaMain.this.dataSetObservable;
                if (var10000 != null) {
                    var10000.onChanged();
                }

                KoldaMain.this.findPositionAfterClick();
                if (notify) {
                    KoldaListnerJava var4 = KoldaMain.this.getKolodaListener();
                    if (var4 != null) {
                        var4.onClickRight(adapterPosition);
                    }
                }

            }

            public void onCardMovedOnClickLeft(int adapterPosition, View card, boolean notify) {
                KoldaMain.this.activeViews.remove(card);
                KoldaMain.this.dyingViews.add(card);
                DataSetObserver var10000 = KoldaMain.this.dataSetObservable;
                if (var10000 != null) {
                    var10000.onChanged();
                }

                KoldaMain.this.findPositionAfterClick();
                if (notify) {
                    KoldaListnerJava var4 = KoldaMain.this.getKolodaListener();
                    if (var4 != null) {
                        var4.onClickLeft(adapterPosition);
                    }
                }

            }
        });
        this.init(attrs);
    }

    // $FF: synthetic method
    public static int access$getAdapterPosition$p(KoldaMain $this) {
        return $this.adapterPosition;
    }

    // $FF: synthetic method
    public static void access$setDyingViews$p(KoldaMain $this, HashSet var1) {
        $this.dyingViews = var1;
    }

    // $FF: synthetic method
    public static void access$setMaxVisibleCards$p(KoldaMain $this, int var1) {
        $this.maxVisibleCards = var1;
    }

    // $FF: synthetic method
    public static void access$setActiveViews$p(KoldaMain $this, LinkedHashSet var1) {
        $this.activeViews = var1;
    }

    // $FF: synthetic method
    public static void access$setDataSetObservable$p(KoldaMain $this, @Nullable DataSetObserver var1) {
        $this.dataSetObservable = var1;
    }

    // $FF: synthetic method
    public static final void access$setDeckMap$p(KoldaMain $this, HashMap var1) {
        $this.deckMap = var1;
    }

    public View _$_findCachedViewById(int var1) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }

        View var2 = (View) this._$_findViewCache.get(var1);
        if (var2 == null) {
            var2 = this.findViewById(var1);
            this._$_findViewCache.put(var1, var2);
        }

        return var2;
    }

    public void _$_clearFindViewByIdCache() {
        if (this._$_findViewCache != null) {
            this._$_findViewCache.clear();
        }

    }


}
