package app.com.youtubeapiv3;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.ArcMotion;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static app.com.youtubeapiv3.CoursesList.sqLiteHelper;

public class AddCourses extends AppCompatActivity {

    EditText coursename, courseabout, playlist;
    Button  btnAdd;
    ImageView imageView;
    final int REQUEST_CODE_GALLERY = 999;
    private ViewGroup container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_courses );

        init();


        imageView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        AddCourses.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        } );
        container = (ViewGroup) findViewById( R.id.container );

        setupSharedEelementTransitions1();

        View.OnClickListener dismissListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        };
        container.setOnClickListener( dismissListener );
        container.findViewById( R.id.close ).setOnClickListener( dismissListener );


        btnAdd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(coursename.getText().toString().trim().length() <= 0){
                    Toast.makeText( getApplicationContext(), "Please Enter Course Name :)", Toast.LENGTH_SHORT ).show();
                }else if(playlist.getText().toString().trim().length() <= 0){
                    Toast.makeText( getApplicationContext(), "Please Enter PLayList URL :)", Toast.LENGTH_SHORT ).show();
                }else {
                try {
                    sqLiteHelper.insertData(
                            coursename.getText().toString().trim(),
                            courseabout.getText().toString().trim(),
                            imageViewToByte( imageView ),
                            playlist.getText().toString().trim()
                    );
                    Toast.makeText( getApplicationContext(), "Added successfully!", Toast.LENGTH_SHORT ).show();
                    coursename.setText( "" );
                    courseabout.setText( "" );
                    playlist.setText( "" );
                    imageView.setImageResource( R.mipmap.coursetube_logo );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent( AddCourses.this, CoursesList.class );
                startActivity( intent );}
            }
        } );


    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream );
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent( Intent.ACTION_PICK );
                intent.setType( "image/*" );
                startActivityForResult( intent, REQUEST_CODE_GALLERY );
            } else {
                Toast.makeText( getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT ).show();
            }
            return;
        }

        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream( uri );

                Bitmap bitmap = BitmapFactory.decodeStream( inputStream );
                imageView.setImageBitmap( bitmap );

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult( requestCode, resultCode, data );
    }

    private void init() {
        coursename = (EditText) findViewById( R.id.courseName );
        courseabout = (EditText) findViewById( R.id.courseAbout );
        playlist = (EditText) findViewById( R.id.playlist );
        btnAdd = (Button) findViewById( R.id.btnAdd );
        imageView = (ImageView) findViewById( R.id.imageView );
    }
    public void setupSharedEelementTransitions1() {
        ArcMotion arcMotion = new ArcMotion();
        arcMotion.setMinimumHorizontalAngle( 50f );
        arcMotion.setMinimumVerticalAngle( 50f );

        Interpolator easeInOut = AnimationUtils.loadInterpolator( this, android.R.interpolator.fast_out_slow_in );

        MorphFabToDialog sharedEnter = new MorphFabToDialog();
        sharedEnter.setPathMotion( arcMotion );
        sharedEnter.setInterpolator( easeInOut );

        MorphDialogToFab sharedReturn = new MorphDialogToFab();
        sharedReturn.setPathMotion( arcMotion );
        sharedReturn.setInterpolator( easeInOut );

        if (container != null) {
            sharedEnter.addTarget( container );
            sharedReturn.addTarget( container );
        }
        getWindow().setSharedElementEnterTransition( sharedEnter );
        getWindow().setSharedElementReturnTransition( sharedReturn );
    }

    public void setupSharedEelementTransitions2() {
        ArcMotion arcMotion = new ArcMotion();
        arcMotion.setMinimumHorizontalAngle( 50f );
        arcMotion.setMinimumVerticalAngle( 50f );

        Interpolator easeInOut = AnimationUtils.loadInterpolator( this, android.R.interpolator.fast_out_slow_in );

        MorphTransition sharedEnter = new MorphTransition( ContextCompat.getColor( this, R.color.fab_background_color ),
                ContextCompat.getColor( this, R.color.dialog_background_color ), 100, getResources().getDimensionPixelSize( R.dimen.dialog_corners ), true );
        sharedEnter.setPathMotion( arcMotion );
        sharedEnter.setInterpolator( easeInOut );

        MorphTransition sharedReturn = new MorphTransition( ContextCompat.getColor( this, R.color.dialog_background_color ),
                ContextCompat.getColor( this, R.color.fab_background_color ), getResources().getDimensionPixelSize( R.dimen.dialog_corners ), 100, false );
        sharedReturn.setPathMotion( arcMotion );
        sharedReturn.setInterpolator( easeInOut );

        if (container != null) {
            sharedEnter.addTarget( container );
            sharedReturn.addTarget( container );
        }
        getWindow().setSharedElementEnterTransition( sharedEnter );
        getWindow().setSharedElementReturnTransition( sharedReturn );
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }

    public void dismiss() {
        setResult( Activity.RESULT_CANCELED );
        finishAfterTransition();
    }
}
