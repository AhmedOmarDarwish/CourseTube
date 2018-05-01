package app.com.youtubeapiv3;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import app.com.youtubeapiv3.adapters.coursesAdapter;
import app.com.youtubeapiv3.fragments.PlayListFragment;
import app.com.youtubeapiv3.models.courses;


public class CoursesList extends AppCompatActivity {
    ListView listview;
    ArrayList <courses> courseslist;
    coursesAdapter coursesAdapter = null;
    public static SQLiteHelper sqLiteHelper;
    private int RC_LOGIN = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_courses_list );
        sqLiteHelper = new SQLiteHelper( this, "COURSESDB.sqlite", null, 1 );
        sqLiteHelper.queryData( "CREATE TABLE IF NOT EXISTS COURSES(Id INTEGER PRIMARY KEY AUTOINCREMENT, coursename VARCHAR, courseabout VARCHAR, image BLOB,playlist VARCHAR)" );
        LinearLayout newsfeed = (LinearLayout) findViewById( R.id.news_feed );
        newsfeed.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent( CoursesList.this, NewsFeed.class );
                startActivity( intent );
            }
        } );

        LinearLayout addpeople = (LinearLayout) findViewById( R.id.add_people );
        addpeople.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent( CoursesList.this, AddPeople.class );
                startActivity( intent );
            }
        } );

        LinearLayout notification = (LinearLayout) findViewById( R.id.notification );
        notification.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent( CoursesList.this, Reminders.class );
                startActivity( intent );
            }
        } );

        listview = (ListView) findViewById( R.id.Courses_list_item );
        courseslist = new ArrayList <>();
        coursesAdapter = new coursesAdapter( this, R.layout.activity_courses, courseslist );
        listview.setAdapter( coursesAdapter );
        Cursor cursor = sqLiteHelper.getData( "SELECT * FROM COURSES" );
        courseslist.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt( 0 );
            String coursename = cursor.getString( 1 );
            String courseabout = cursor.getString( 2 );
            byte[] courseicon = cursor.getBlob( 3 );
            String playlist = cursor.getString( 4 );

            courseslist.add( new courses( id, coursename, courseabout, courseicon, playlist ) );
        }
        coursesAdapter.notifyDataSetChanged();


        final FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById( R.id.adddd );
        floatingActionButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent( CoursesList.this, AddCourses.class );
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation( CoursesList.this, floatingActionButton, getString( R.string.transition_dialog ) );
                startActivityForResult( intent, RC_LOGIN, options.toBundle() );
            }
        } );

        listview.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
                courses courses = courseslist.get( position );
                PlayListFragment.setPlaylistId( courses.getPlaylist() );
                Intent intent = new Intent( CoursesList.this, CourseTools.class );
                startActivity( intent );
            }
        } );

        listview.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView <?> parent, View view, final int position, long id) {

                CharSequence[] items = {"Update", "Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder( CoursesList.this );
                dialog.setTitle( "Choose an action" );
                dialog.setItems( items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            // update
                            Cursor c = sqLiteHelper.getData( "SELECT id FROM COURSES" );
                            ArrayList <Integer> arrID = new ArrayList <Integer>();
                            while (c.moveToNext()) {
                                arrID.add( c.getInt( 0 ) );
                            }
                            // show dialog update at here
                            showDialogUpdate( CoursesList.this, arrID.get( position ) );

                        } else {
                            // delete
                            Cursor c = sqLiteHelper.getData( "SELECT id FROM COURSES" );
                            ArrayList <Integer> arrID = new ArrayList <Integer>();
                            while (c.moveToNext()) {
                                arrID.add( c.getInt( 0 ) );
                            }
                            showDialogDelete( arrID.get( position ) );
                        }
                    }
                } );
                dialog.show();
                return true;
            }
        } );


    }


    ImageView courseicon;

    private void showDialogUpdate(Activity activity, final int position) {

        final Dialog dialog = new Dialog( activity );
        dialog.setContentView( R.layout.update_course_activity );
        dialog.setTitle( "Update" );
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        courseicon = (ImageView) dialog.findViewById( R.id.courseicon );
        final EditText coursename = (EditText) dialog.findViewById( R.id.courseName );
        final EditText about = (EditText) dialog.findViewById( R.id.courseAbout );
        final EditText playlist = (EditText) dialog.findViewById( R.id.playlist );
        Button btnUpdate = (Button) dialog.findViewById( R.id.btnUpdate );

        // set width for dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        // set height for dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels* 0.75);
        dialog.getWindow().setLayout( width, height );
        dialog.show();

        courseicon.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                // request photo library
                ActivityCompat.requestPermissions(
                        CoursesList.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        888
                );
            }
        } );

        btnUpdate.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(coursename.getText().toString().trim().length() <= 0){
                    Toast.makeText( getApplicationContext(), "Please Enter Course Name :)", Toast.LENGTH_SHORT ).show();
                }else if(playlist.getText().toString().trim().length() <= 0){
                    Toast.makeText( getApplicationContext(), "Please Enter PLayList URL :)", Toast.LENGTH_SHORT ).show();
                }else {
                    try {
                        sqLiteHelper.updateData(
                                coursename.getText().toString().trim(),
                                about.getText().toString().trim(),
                                AddCourses.imageViewToByte( courseicon ),
                                playlist.getText().toString().trim(),
                                position
                        );
                        dialog.dismiss();
                        Toast.makeText( getApplicationContext(), "Update successfully!!!", Toast.LENGTH_SHORT ).show();
                    } catch (Exception error) {
                        Log.e( "Update error", error.getMessage() );
                    }
                    updatecourseslist();
                }}
        } );
    }

    private void showDialogDelete(final int idcourse) {
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder( CoursesList.this );

        dialogDelete.setTitle( "Warning!!" );
        dialogDelete.setMessage( "Are you sure you want to this delete?" );
        dialogDelete.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    sqLiteHelper.deleteData( idcourse );
                    Toast.makeText( getApplicationContext(), "Delete successfully!!!", Toast.LENGTH_SHORT ).show();
                } catch (Exception e) {
                    Log.e( "error", e.getMessage() );
                }
                updatecourseslist();
            }
        } );

        dialogDelete.setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        } );
        dialogDelete.show();
    }

    private void updatecourseslist() {
        // get all data from sqlite
        Cursor cursor = sqLiteHelper.getData( "SELECT * FROM COURSES" );
        courseslist.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt( 0 );
            String coursename = cursor.getString( 1 );
            String about = cursor.getString( 2 );
            byte[] image = cursor.getBlob( 3 );
            String playlist = cursor.getString( 4 );

            courseslist.add( new courses( id, coursename, about, image, playlist ) );
        }
        coursesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == 888) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent( Intent.ACTION_PICK );
                intent.setType( "image/*" );
                startActivityForResult( intent, 888 );
            } else {
                Toast.makeText( getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT ).show();
            }
            return;
        }
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 888 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream( uri );
                Bitmap bitmap = BitmapFactory.decodeStream( inputStream );
                courseicon.setImageBitmap( bitmap );

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult( requestCode, resultCode, data );

    }


}
