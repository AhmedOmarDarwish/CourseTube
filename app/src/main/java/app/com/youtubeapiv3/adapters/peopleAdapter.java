package app.com.youtubeapiv3.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.com.youtubeapiv3.R;
import app.com.youtubeapiv3.models.People;

public class peopleAdapter extends ArrayAdapter<People> {

    public peopleAdapter(Activity context, ArrayList<People> words){

        super(context,0, words);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // check if the existing view is being reused , otherwise inflate the view
        View listItemView = convertView ;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate( R.layout.people_list_item, parent, false );
        }

        People currentUser = getItem(position);

        TextView name = (TextView) listItemView.findViewById(R.id.name);
        name.setText(currentUser.getName());

        ImageView wordImageView = (ImageView) listItemView.findViewById(R.id.user_image);
        wordImageView.setImageResource(currentUser.getmImageResourceId());

        return listItemView ;
    }
}



