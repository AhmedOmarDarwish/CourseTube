package app.com.youtubeapiv3.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

import app.com.youtubeapiv3.R;
import app.com.youtubeapiv3.models.Message;

/**
 * Created by paulodichone on 4/14/15.
 */
public class ChatAdapter extends ArrayAdapter<Message> {
    private String mUserId;

    public ChatAdapter(Context context, String userId, List<Message> messages) {
        super(context, 0, messages);
        mUserId = userId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate( R.layout.chat_row, parent, false);

            final ViewHolder holder = new ViewHolder();
            holder.imageLeft = (ImageView) convertView.findViewById(R.id.ProfileLeft);
            holder.imageRight = (ImageView) convertView.findViewById(R.id.ProfileRight);
            holder.body = (TextView) convertView.findViewById(R.id.tvBody);
            holder.body_recived = (TextView) convertView.findViewById(R.id.tvBody_recieved);

            convertView.setTag(holder);
        }

        final Message message = (Message)getItem(position);
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        final boolean isMe = message.getUserId().equals(mUserId);

        if (isMe) {
            holder.imageRight.setVisibility( View.VISIBLE);
            holder.imageLeft.setVisibility( View.GONE);
            holder.body.setVisibility( View.VISIBLE);
            holder.body_recived.setVisibility( View.GONE);
            holder.body.setGravity( Gravity.CENTER_VERTICAL | Gravity.RIGHT);

        }else {

            holder.imageLeft.setVisibility( View.VISIBLE);
            holder.imageRight.setVisibility( View.GONE);
            holder.body_recived.setVisibility( View.VISIBLE);
            holder.body.setVisibility( View.GONE);
            holder.body_recived.setGravity( Gravity.CENTER_VERTICAL | Gravity.LEFT);

        }

        final ImageView profileView = isMe ? holder.imageRight : holder.imageLeft;
        final TextView chatView = isMe ? holder.body : holder.body_recived;
        Picasso.with(getContext()).load(getProfileGravatar(message.getUserId())).into(profileView);
        holder.body.setText(message.getBody());
        holder.body_recived.setText(message.getBodyRecieved());

        return convertView;
    }


    //Let's create a gravatar image - we will be using a hash value obtain from userid
    private static String getProfileGravatar(final String userId) {

        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "http://www.gravatar.com/avatar/" + hex + "?d=identicon";

    }

    class ViewHolder {
        public ImageView imageLeft;
        public ImageView imageRight;
        public TextView body;
        public TextView body_recived;
    }
}
