package me.duck.hooktest.bean;

import androidx.annotation.Keep;

@Keep
public class UseBean {
    private boolean isHook;
    private int level;

    public UseBean(boolean isHook, int level) {
        this.isHook = isHook;
        this.level = level;
    }

    public boolean isHook() {
        return isHook;
    }

    public void setHook(boolean hook) {
        isHook = hook;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
