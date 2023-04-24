package com.example.voicechatgpt.Modal;

public class Message {
    String mText;
    boolean isSentByUser;

    public Message(String mText, boolean isSentByUser) {
        this.mText = mText;
        this.isSentByUser = isSentByUser;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public boolean isSentByUser() {
        return isSentByUser;
    }

    public void setSentByUser(boolean sentByUser) {
        isSentByUser = sentByUser;
    }
}

