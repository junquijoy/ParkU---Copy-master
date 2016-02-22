package parku.com.au.parku.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import parku.com.au.parku.R;
import parku.com.au.parku.network.RetroClient;
import parku.com.au.parku.network.model.User;
import parku.com.au.parku.util.EncryptPassword;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText etUsername, etPassword;
    private Button btnLogin, btnSignup;

    private String strUsername, strPassword, encryptedPassword;

    private RetroClient retroClient;
    private EncryptPassword encryptPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        etUsername = (EditText) findViewById(R.id.username_editText);
        etPassword = (EditText) findViewById(R.id.password_editText);


        btnLogin = (Button) findViewById(R.id.login_button);
        btnLogin.setOnClickListener(this);

        btnSignup = (Button) findViewById(R.id.signup_button);
        btnSignup.setOnClickListener(this);

        retroClient = new RetroClient();
        encryptPassword = new EncryptPassword();



    }



    @Override
    public void onClick(View v) {

        // On click login button
        if (v.getId() == R.id.login_button) {

            // Pass input data to a variable
            strUsername = etUsername.getText().toString();
            strPassword = etPassword.getText().toString();
            encryptedPassword = encryptPassword.computeSHAHash(strPassword);

            // Check input data
            if (strUsername.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Registraion Failed. Check above information", Toast.LENGTH_SHORT).show();
                etUsername.setError("Enter Username");

                if (strPassword.isEmpty()) {
                    etPassword.setError("Enter Password");
                }

            } else if (strPassword.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Registraion Failed. Check above information", Toast.LENGTH_SHORT).show();
                etPassword.setError("Enter Password");

            } else {
                loginUser();
            }

            // On click Signup button
        } else if (v.getId() == R.id.signup_button) {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        }
    }

    public void loginUser() {

        User user = new User();
        user.setUsername(strUsername);
        user.setPassword(encryptedPassword);

        retroClient.getApiService().loginUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                User user = response.body();

                    Toast.makeText(LoginActivity.this, "User = " + user.getUsername() + "Password = " + user.getPassword(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                    startActivity(intent);

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.v("", "ON FAILURE!!!!");
                Toast.makeText(LoginActivity.this, "ERROR !!! " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

}
