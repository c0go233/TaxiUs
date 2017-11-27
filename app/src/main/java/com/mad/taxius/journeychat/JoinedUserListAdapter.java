package com.mad.taxius.journeychat;

/**
 * Created by kisungTae on 19/10/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mad.taxius.R;
import com.mad.taxius.model.JoinedUser;

import java.util.ArrayList;

/**
 * Adapter class that handles the recyclerview of joined users
 */

public class JoinedUserListAdapter extends RecyclerView.Adapter<JoinedUserListAdapter.ViewHolder> {

    /**
     * ViewHolder class that contains widgets that are used in joined user item view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTv, emailTv;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.item_joined_user_name_tv);
            emailTv = (TextView) itemView.findViewById(R.id.item_joined_user_email_tv);
        }
    }

    private Context mContext;
    private ArrayList<JoinedUser> mJoinedUsers;

    public JoinedUserListAdapter(Context context, ArrayList<JoinedUser> joinedUsers) {
        this.mContext = context;
        this.mJoinedUsers = joinedUsers;
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
                .inflate(R.layout.item_joined_user, parent, false);
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
        JoinedUser joinedUser = mJoinedUsers.get(position);
        holder.emailTv.setText(joinedUser.getEmail());
        holder.nameTv.setText(joinedUser.getName());
    }

    /**
     * Get the total item count of joined user list
     *
     * @return item count of joined user list
     */
    @Override
    public int getItemCount() {
        return mJoinedUsers.size();
    }
}