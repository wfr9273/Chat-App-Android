package com.example.android.whatsappclone;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.analytics.internal.zzy.A;
import static com.google.android.gms.analytics.internal.zzy.f;

/**
 * Created by MAC-WFR on 8/13/17.
 */

public class ChatsAdapter extends ArrayAdapter<Chats> {
    String userToChatWith;

    public ChatsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Chats> objects,
                        String userToChat) {
        super(context, resource, objects);
        userToChatWith = userToChat;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RelativeLayout view = (RelativeLayout) convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = (RelativeLayout) inflater.inflate(R.layout.chat_list_item, parent, false);
        }
        TextView chat1 = (TextView) view.findViewById(R.id.chat1);
        TextView chat2 = (TextView) view.findViewById(R.id.chat2);

        if (getItem(position).getSender().equals(userToChatWith)) {
            chat1.setBackgroundColor(getContext().getResources().getColor(android.R.color.darker_gray));
            chat1.setText(getItem(position).getContent());
        } else {
            chat2.setBackgroundColor(getContext().getResources().getColor(android.R.color.holo_blue_light));
            chat2.setText(getItem(position).getContent());
        }

        return view;
    }
}
