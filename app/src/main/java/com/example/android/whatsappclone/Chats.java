package com.example.android.whatsappclone;

/**
 * Created by MAC-WFR on 8/13/17.
 */

public class Chats {
    private String mSender;
    private String mReceiver;
    private String mContent;

    public Chats(String sender, String receiver, String content) {
        mContent = content;
        mReceiver = receiver;
        mSender = sender;
    }

    public String getSender() {
        return mSender;
    }

    public String getReceiver() {
        return mReceiver;
    }

    public String getContent() {
        return mContent;
    }
}
