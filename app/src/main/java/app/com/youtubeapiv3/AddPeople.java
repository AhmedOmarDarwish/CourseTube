package app.com.youtubeapiv3;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import app.com.youtubeapiv3.adapters.peopleAdapter;
import app.com.youtubeapiv3.models.People;

public class AddPeople extends AppCompatActivity {
    ArrayList<String>users;
    ArrayAdapter arrayAdapter ;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_people );

        // Setup toolbar
        mToolbar = (Toolbar) findViewById( R.id.people_toolbar );
        setSupportActionBar(mToolbar);
        mToolbar.setTitle( "Follow People" );

        if (ParseUser.getCurrentUser().get( "isFollowing" ) == null) {
            List <String> emptyList = new ArrayList <String>();
            ParseUser.getCurrentUser().put( "isFollowing", emptyList );
        }


        LinearLayout mycourses = (LinearLayout) findViewById( R.id.my_courses );
        mycourses.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent( AddPeople.this, CoursesList.class );
                startActivity( intent );
            }
        } );

        LinearLayout newsfeed = (LinearLayout) findViewById( R.id.news_feed );
        newsfeed.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent( AddPeople.this, NewsFeed.class );
                startActivity( intent );
            }
        } );

        LinearLayout notification = (LinearLayout) findViewById( R.id.notification );
        notification.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent( AddPeople.this, Reminders.class );
                startActivity( intent );
            }
        } );

        //--------------------------------------------------


        users = new ArrayList <String>();


        arrayAdapter = new ArrayAdapter( this, android.R.layout.simple_list_item_checked, users );
        final ListView listView = (ListView) findViewById( R.id.people_list );
        listView.setChoiceMode( AbsListView.CHOICE_MODE_MULTIPLE );
        listView.setAdapter( arrayAdapter );

        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> adapterView, View view, int i, long l) {
                CheckedTextView checkedTextView = (CheckedTextView) view;
                if (checkedTextView.isChecked()) {
                    Log.i( "AppInfo", "Row is checked" );
                    ParseUser.getCurrentUser().getList( "isFollowing" ).add( users.get( i ) );
                    ParseUser.getCurrentUser().saveInBackground();
                } else {
                    Log.i( "AppInfo", "Row is not checked" );
                    ParseUser.getCurrentUser().getList( "isFollowing" ).remove( users.get( i ) );
                    ParseUser.getCurrentUser().saveInBackground();
                }
            }
        } );

        ParseQuery <ParseUser> query = ParseUser.getQuery();
//        query.whereNotEqualTo( "fullname", ParseUser.getCurrentUser().get( "fullname" ) );
        query.findInBackground( new FindCallback <ParseUser>() {
            public void done(List <ParseUser> objects, ParseException e) {
                if (e == null) {
                    // The query was successful.
                    users.clear();

                    for (ParseUser user : objects) {
                        users.add( (String) user.get( "fullname" ) );
                    }

                    arrayAdapter.notifyDataSetChanged();

                    for (String fullname : users) {
                        if (ParseUser.getCurrentUser().getList( "isFollowing" ).contains( fullname )) {
                            listView.setItemChecked( users.indexOf( fullname ), true );
                        }
                    }

                } else {
                    // Something went wrong.
                    e.printStackTrace();
                }
            }

        } );

    }

        // Create menu
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_user_list, menu);
            return true;
        }

        // Setup menu
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
           int id = item.getItemId();

           if (id == R.id.new_Post) {

               AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle( "New Post" );
               final EditText postContent = new EditText(this );
               builder.setView( postContent );

               builder.setPositiveButton( "Create", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       Log.i("AppInfo", String.valueOf( postContent.getText()));

                       ParseObject post = new ParseObject("post");
                       post.put("content", String.valueOf( postContent.getText()));
                       post.put("username", ParseUser.getCurrentUser().get( "fullname" ));
                       post.saveInBackground( new SaveCallback() {
                           @Override
                           public void done(ParseException e) {
                               if (e == null){
                                   Toast.makeText( getApplicationContext(), "Your Post created successfully ",Toast.LENGTH_LONG).show();
                               }else {
                                   Toast.makeText( getApplicationContext(), "Please Try Again ",Toast.LENGTH_LONG).show();
                               }
                           }
                       } );


                   }
               } );

                builder.setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                } );

                builder.show();

               return true;
           }

                    return super.onOptionsItemSelected(item);
            }
        }



