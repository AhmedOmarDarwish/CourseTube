package app.com.youtubeapiv3;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class Forgot_Password_Activity extends AppCompatActivity {
    EditText forgetPasswordView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Parse.initialize(this);

        forgetPasswordView = (EditText) findViewById(R.id.forget_email);
        final Button forgot_button = (Button) findViewById(R.id.forgot_password_button);
        forgot_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validating the log in data
                boolean validationError = false;

                StringBuilder validationErrorMessage = new StringBuilder("Please, insert ");
                if (isEmpty(forgetPasswordView)) {
                    validationError = true;
                    validationErrorMessage.append("an Email");
                }

                validationErrorMessage.append(".");

                if (validationError) {
                    Toast.makeText(Forgot_Password_Activity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
                    return;
                }

                //Setting up a progress dialog
                final ProgressDialog dlg = new ProgressDialog(Forgot_Password_Activity.this);
                dlg.setTitle("Please, wait a moment.");
                dlg.setMessage("sending passowrd reset link to you email...");
                dlg.show();

                ParseUser user = new ParseUser();
                user.setEmail(forgetPasswordView.getText().toString());

                ParseUser.requestPasswordResetInBackground(forgetPasswordView.getText().toString(), new RequestPasswordResetCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // An email was successfully sent with reset instructions.
                            alertDisplayer("Sucessfully Send", "password reset link was send to you email!", false);

                        } else {
                            // Something went wrong. Look at the ParseException to see what's up.
                            ParseUser.logOut();
                            alertDisplayer("failed send", "failed to send ", true);
                            Toast.makeText(Forgot_Password_Activity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private boolean isEmpty(EditText text) {
        if (text.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }


    private void alertDisplayer(String title, String message, final boolean error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Forgot_Password_Activity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (!error) {
                            Intent intent = new Intent(Forgot_Password_Activity.this, LogIn_Activity.class);
                            intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}
