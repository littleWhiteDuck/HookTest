package me.duck.hooktest.bean;

import androidx.annotation.Keep;

@Keep
public class TestGson {

    private int testInt;
    private boolean testBoolean;
    private String testString;

    public TestGson(int testInt, boolean testBoolean, String testString) {

        this.testInt = testInt;
        this.testBoolean = testBoolean;
        this.testString = testString;
    }

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }

    public boolean isTestBoolean() {
        return testBoolean;
    }

    public void setTestBoolean(boolean testBoolean) {
        this.testBoolean = testBoolean;
    }

    public int getTestInt() {
        return testInt;
    }

    public void setTestInt(int testInt) {
        this.testInt = testInt;
    }
}
