package app.com.youtubeapiv3.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.com.youtubeapiv3.R;
import app.com.youtubeapiv3.models.courses;

public class coursesAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList <courses> coursesList;

    public coursesAdapter(Context context, int layout, ArrayList <courses> coursesList) {
        this.context = context;
        this.layout = layout;
        this.coursesList = coursesList;
    }

    @Override
    public int getCount() {
        return coursesList.size();
    }

    @Override
    public Object getItem(int position) {
        return coursesList.get( position );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView coursename, courseabout;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder holder = new ViewHolder();
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            row = inflater.inflate( layout, null );
            holder.imageView = (ImageView) row.findViewById( R.id.course_icon );
            holder.coursename = (TextView) row.findViewById( R.id.course_name );
            holder.courseabout = (TextView) row.findViewById( R.id.about );
            row.setTag( holder );
        }
        else {
            holder = (ViewHolder) row.getTag();
        }
        courses courses = coursesList.get(position);

        holder.coursename.setText(courses.getCoursename());
        holder.courseabout.setText(courses.getAbout());
        byte[] image = courses.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        holder.imageView.setImageBitmap(bitmap);

        return row;
    }

}