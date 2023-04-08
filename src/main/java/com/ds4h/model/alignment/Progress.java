package com.ds4h.model.alignment;

public class Progress {
    private static int progress = 0;
    private static int maxProgress;

    public static void increase() {
        progress++;
    }

    public static void clear() {
        progress = 0;
    }

    public static int getProgress() {
        System.out.println(progress);
        System.out.println(maxProgress);
        return (100*progress/maxProgress);
    }

    public static int getMaxProgress() {
        return maxProgress;
    }
    public static void setMaxProgress(int max) {
        maxProgress = max;
    }
}
