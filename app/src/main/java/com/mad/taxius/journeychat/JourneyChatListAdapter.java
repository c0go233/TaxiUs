package com.mad.taxius.journeychat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mad.taxius.R;
import com.mad.taxius.model.ChatMessage;

import java.util.ArrayList;

/**
 * Adapter class that handles the recyclerview of chat messages
 */

public class JourneyChatListAdapter extends RecyclerView.Adapter<JourneyChatListAdapter.ViewHolder> {

    /**
     * ViewHolder class that contains widgets that are used in chat message view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView senderNameTv, messageValueTv, timeTv;

        public ViewHolder(View itemView) {
            super(itemView);
            senderNameTv = (TextView) itemView.findViewById(R.id.item_chat_message_sender_name_tv);
            messageValueTv = (TextView) itemView.findViewById(R.id.item_chat_message_message_value_tv);
            timeTv = (TextView) itemView.findViewById(R.id.item_chat_message_time_tv);
        }
    }

    private Context mContext;
    private ArrayList<ChatMessage> mChatMessages;

    public JourneyChatListAdapter(Context context, ArrayList<ChatMessage> chatMessages) {
        this.mContext = context;
        this.mChatMessages = chatMessages;
    }

    /**
     * Called when its Viewholder is created
     *
     * @param parent   is the parent view group
     * @param viewType is the type of view
     * @return created ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View journeyChatMessageView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_message, parent, false);
        return new ViewHolder(journeyChatMessageView);
    }

    /**
     * Binds the widgets of ViewHolder to actual data to be displayed
     *
     * @param holder   is the viewholder to be bound
     * @param position is the position of viewholder in the recycler view
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatMessage chatMessage = mChatMessages.get(position);
        holder.senderNameTv.setText(chatMessage.getSenderName());
        holder.messageValueTv.setText(chatMessage.getMessageValue());
        holder.timeTv.setText(chatMessage.getTimestamp());
    }

    /**
     * Get the total item count of message list
     *
     * @return item count of message list
     */
    @Override
    public int getItemCount() {
        return mChatMessages.size();
    }
}
