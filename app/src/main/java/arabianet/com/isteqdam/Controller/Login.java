package arabianet.com.isteqdam.Controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;

import arabianet.com.isteqdam.Model.WebService;
import arabianet.com.isteqdam.R;
import eu.amirs.JSON;

public class Login extends AppCompatActivity {

    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        spinner = (ProgressBar)findViewById(R.id.progressBarLogin);
        spinner.setVisibility(View.GONE);
    }

    public void login(View view){
        if(validateForm()){
            spinner.setVisibility(View.VISIBLE);
            String userName = ((EditText)findViewById(R.id.userNameInput)).getText().toString();
            String pass = ((EditText)findViewById(R.id.passwordInput)).getText().toString();
            HashMap<String,String> params = new HashMap<>();
            params.put("username",userName);
            params.put("password",pass);
            Response.ErrorListener onFail = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    networkError();
                }
            };
            Response.Listener<String> onSuccess = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    authenticateUser(response);

                }
            };
            WebService webService = new WebService();
            webService.loginWithUserNameAndPass(params,false,onSuccess,onFail,this);

        }
    }

    private Boolean validateForm(){
        String userName = ((EditText)findViewById(R.id.userNameInput)).getText().toString();
        String pass = ((EditText)findViewById(R.id.passwordInput)).getText().toString();

        if(!userName.equals("")&&pass.equals("")){
            Toast.makeText(this,R.string.wrongUNameOrPass,Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void networkError(){
        spinner.setVisibility(View.GONE);
        Toast.makeText(this,R.string.networkError,Toast.LENGTH_LONG).show();
    }

    private void serverError(){
        spinner.setVisibility(View.GONE);
        Toast.makeText(this,R.string.serverError,Toast.LENGTH_LONG).show();
    }

    private void authenticateUser(String response){
        JSON json = new JSON(response);
        spinner.setVisibility(View.GONE);
        if(json.key("code").stringValue().equals("200")){
            SharedPreferences sp = this.getSharedPreferences("itqanIsteqdam", MODE_PRIVATE);
            SharedPreferences.Editor spManager = sp.edit();
            spManager.putString("userID",json.key("user_id").stringValue());
            spManager.putString("SID",json.key("session_token").stringValue());
            spManager.apply();
            Intent intent = new Intent(this,Home.class);
            startActivity(intent);
        }else{
            Toast.makeText(this,R.string.wrongUNameOrPass,Toast.LENGTH_SHORT).show();
        }

    }



}
