package com.kredivation.switchland.fragment.likepagelib;

import android.view.View;

import kotlin.jvm.internal.Intrinsics;

public interface KoldaListnerJava {

    void onNewTopCard(int var1);

    void onCardDrag(int var1, View var2, float var3);

    void onCardSwipedLeft(int var1);

    void onCardSwipedRight(int var1);

    void onClickRight(int var1);

    void onClickLeft(int var1);

    void onCardSingleTap(int var1);

    void onCardDoubleTap(int var1);

    void onCardLongPress(int var1);

    void onEmptyDeck();

    public static class DefaultImpls {
        public static void onNewTopCard(KoldaListnerJava $this, int position) {
        }

        public static void onCardDrag(KoldaListnerJava $this, int position, View cardView, float progress) {
            Intrinsics.checkParameterIsNotNull(cardView, "cardView");
        }

        public static void onCardSwipedLeft(KoldaListnerJava $this, int position) {
        }

        public static void onCardSwipedRight(KoldaListnerJava $this, int position) {
        }

        public static void onClickRight(KoldaListnerJava $this, int position) {
        }

        public static void onClickLeft(KoldaListnerJava $this, int position) {
        }

        public static void onCardSingleTap(KoldaListnerJava $this, int position) {
        }

        public static void onCardDoubleTap(KoldaListnerJava $this, int position) {
        }

        public static void onCardLongPress(KoldaListnerJava $this, int position) {
        }

        public static void onEmptyDeck(KoldaListnerJava $this) {
        }
    }
}
