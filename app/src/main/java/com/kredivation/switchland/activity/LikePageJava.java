package com.kredivation.switchland.activity;/*
package com.kredivation.switchland.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kredivation.switchland.R;
import com.kredivation.switchland.fragment.likepagelib.TinderCardAdapter;
import com.kredivation.switchland.fragment.likepagelib.KoldaListnerJava;
import com.kredivation.switchland.fragment.likepagelib.KoldaMain;
import com.kredivation.switchland.utils.ASTUIUtil;

import java.util.HashMap;


public class LikePageJava extends AppCompatActivity {
    private TinderCardAdapter adapter;
    private HashMap _$_findViewCache;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.likepage_home);
        this.initializeDeck();
        this.fillData();
        this.setUpCLickListeners();
    }

    protected void onStart() {
        super.onStart();
        ((LinearLayout) this._$_findCachedViewById(R.id.activityMain)).getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver.OnGlobalLayoutListener) (new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint({"NewApi"})
            public void onGlobalLayout() {
                int width = ((LinearLayout) LikePageJava.this._$_findCachedViewById(R.id.activityMain)).getWidth();
                int height = ((LinearLayout) LikePageJava.this._$_findCachedViewById(R.id.activityMain)).getHeight();
                if (Build.VERSION.SDK_INT >= 16) {
                    ((LinearLayout) LikePageJava.this._$_findCachedViewById(R.id.activityMain)).getViewTreeObserver().removeOnGlobalLayoutListener((ViewTreeObserver.OnGlobalLayoutListener) this);
                } else {
                    ((LinearLayout) LikePageJava.this._$_findCachedViewById(R.id.activityMain)).getViewTreeObserver().removeGlobalOnLayoutListener((ViewTreeObserver.OnGlobalLayoutListener) this);
                }

            }
        }));
    }

    private void initializeDeck() {
        ((KoldaMain) this._$_findCachedViewById(R.id.koloda)).setKolodaListener((KoldaListnerJava) (new KoldaListnerJava() {
            private int cardsSwiped;

            public final int getCardsSwiped$production_sources_for_module_app() {
                return this.cardsSwiped;
            }

            public final void setCardsSwiped$production_sources_for_module_app(int var1) {
                this.cardsSwiped = var1;
            }

            public void onNewTopCard(int position) {
            }

            public void onCardSwipedLeft(int position) {
            }

            public void onCardSwipedRight(int position) {
            }

            public void onEmptyDeck() {
            }

            public void onCardDrag(int position, View cardView, float progress) {
                DefaultImpls.onCardDrag(this, position, cardView, progress);
            }

            public void onClickRight(int position) {
                DefaultImpls.onClickRight(this, position);
            }

            public void onClickLeft(int position) {
                DefaultImpls.onClickLeft(this, position);
            }

            public void onCardSingleTap(int position) {
                DefaultImpls.onCardSingleTap(this, position);
            }

            public void onCardDoubleTap(int position) {
                DefaultImpls.onCardDoubleTap(this, position);
            }

            public void onCardLongPress(int position) {
                DefaultImpls.onCardLongPress(this, position);
            }
        }));
    }

    private void fillData() {
        Integer[] data = new Integer[]{R.drawable.a,
                R.drawable.b,
                R.drawable.c,
                R.drawable.d,
                R.drawable.e,
                R.drawable.a,
                R.drawable.b,
                R.drawable.c,
                R.drawable.d,
                R.drawable.e,
                R.drawable.e,
                R.drawable.b,
                R.drawable.d};
        this.adapter = new TinderCardAdapter((Context) this, data));
        ((KoldaMain) this._$_findCachedViewById(R.id.koloda)).setAdapter((Adapter) this.adapter);
        ((KoldaMain) this._$_findCachedViewById(R.id.koloda)).setNeedCircleLoading(true);
    }

    private void setUpCLickListeners() {
        ImageView dislike = this._$_findCachedViewById(R.id.activityMain).findViewById(R.id.dislike);
        ImageView like = this._$_findCachedViewById(R.id.activityMain).findViewById(R.id.like);

        Button skip = this.findViewById(R.id.skip);
        ASTUIUtil.setBackgroundOval(skip, R.color.blue_color, ASTUIUtil.getColor(R.color.gray), 0);
        ASTUIUtil.setBackgroundRing(dislike, R.color.light_gray_color, ASTUIUtil.getColor(R.color.light_gray_color), 0);
        ASTUIUtil.setBackgroundRing(like, R.color.light_gray_color, ASTUIUtil.getColor(R.color.light_gray_color), 0);
        dislike.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            public void onClick(View it) {
                ((KoldaMain) LikePageJava.this._$_findCachedViewById(R.id.koloda)).onClickLeft();
            }
        }));
        like.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            public void onClick(View it) {
                ((KoldaMain) LikePageJava.this._$_findCachedViewById(R.id.koloda)).onClickRight();
            }
        }));

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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
*/
