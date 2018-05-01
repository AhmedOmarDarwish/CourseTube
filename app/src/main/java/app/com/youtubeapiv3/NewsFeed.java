package app.com.youtubeapiv3;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsFeed extends AppCompatActivity {
    ListView listview;
    SimpleAdapter simpleAdapter;
    List <Map <String, String>> postData = new ArrayList <Map <String, String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_news_feed );

        listview = (ListView) findViewById( R.id.posts_list );
        postData = new ArrayList <Map <String, String>>();
        simpleAdapter = new SimpleAdapter( this, postData, android.R.layout.simple_list_item_2, new String[]{"username", "content"}, new int[]{android.R.id.text1, android.R.id.text2} );

        ParseQuery <ParseObject> query = ParseQuery.getQuery( "post" );
        query.whereContainedIn( "username", ParseUser.getCurrentUser().getList( "isFollowing" ) );
        query.orderByAscending( "createdAt" );
        query.setLimit( 20 );
        query.findInBackground( new FindCallback <ParseObject>() {
            public void done(List <ParseObject> postObjects, ParseException e) {
                if (e == null) {
                    if (postObjects.size() > 0) {


                        for (ParseObject postObject : postObjects) {
                            Map <String, String> post = new HashMap <String, String>( 2 );
                            post.put( "username", postObject.getString( "username" ) );
                            post.put( "content", postObject.getString( "content" ) );
                            postData.add( post );
                        }

                        listview.setAdapter( simpleAdapter );

                    }
                } else {
                    Log.d( "score", "Error: " + e.getMessage() );
                }
            }
        } );


        LinearLayout mycourses = (LinearLayout) findViewById( R.id.my_courses );
        mycourses.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent( NewsFeed.this, CoursesList.class );
                startActivity( intent );
            }
        } );

        LinearLayout addpeople = (LinearLayout) findViewById( R.id.add_people );
        addpeople.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent( NewsFeed.this, AddPeople.class );
                startActivity( intent );
            }
        } );

        LinearLayout notification = (LinearLayout) findViewById( R.id.notification );
        notification.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent( NewsFeed.this, Reminders.class );
                startActivity( intent );
            }
        } );


         }

    }
