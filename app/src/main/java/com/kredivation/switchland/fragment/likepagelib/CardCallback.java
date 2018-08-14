package com.kredivation.switchland.fragment.likepagelib;

import android.view.View;

public interface CardCallback {
    void onCardActionDown(int var1, View var2);

    void onCardDrag(int var1, View var2, float var3);

    void onCardOffset(int var1, View var2, float var3);

    void onCardActionUp(int var1, View var2, boolean var3);

    void onCardSwipedLeft(int var1, View var2, boolean var3);

    void onCardSwipedRight(int var1, View var2, boolean var3);

    void onCardMovedOnClickRight(int var1, View var2, boolean var3);

    void onCardMovedOnClickLeft(int var1, View var2, boolean var3);

    void onCardOffScreen(int var1, View var2);

    void onCardSingleTap(int var1, View var2);

    void onCardDoubleTap(int var1, View var2);

    void onCardLongPress(int var1, View var2);
}
