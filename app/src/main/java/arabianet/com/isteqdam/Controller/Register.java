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

import arabianet.com.isteqdam.Model.Authentication;
import arabianet.com.isteqdam.Model.Validator;
import arabianet.com.isteqdam.Model.WebService;
import arabianet.com.isteqdam.R;
import eu.amirs.JSON;

public class Register extends AppCompatActivity {

    String userName = "";
    String password = "";
    private ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        spinner = (ProgressBar)findViewById(R.id.progressBarRegister);
        spinner.setVisibility(View.GONE);
        Bundle b = getIntent().getExtras();
        userName = b.getString("mobileNumber");
        password = b.getString("confirmCode");
    }

    public void registerUser(View view){
        if(validateForm()){
            spinner.setVisibility(View.VISIBLE);
            String fName = ((EditText)findViewById(R.id.fNameInputField)).getText().toString();
            String lName = ((EditText)findViewById(R.id.lastNameInput)).getText().toString();
            String email = ((EditText)findViewById(R.id.emailInput)).getText().toString();
            String identity = ((EditText)findViewById(R.id.identityInput)).getText().toString();
            String nickName = fName + " " + lName;
            HashMap<String,String> params = new HashMap<>();
            params.put("username",this.userName);
            params.put("password",this.password);
            params.put("nickname",nickName);
            params.put("email",email);
            params.put("firstname",fName);
            params.put("lastname",lName);
            params.put("identity_no",identity);
            params.put("mobile",this.userName);
            Response.Listener<String> onSuccess = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    authenticateUser(response);
                }
            };
            Response.ErrorListener onFail = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    networkError();
                }
            };

            WebService webService = new WebService();
            webService.registerUser(params,false,onSuccess,onFail,this);
        }
    }

    private Boolean validateForm(){

        String fName = ((EditText)findViewById(R.id.fNameInputField)).getText().toString();
        String lName = ((EditText)findViewById(R.id.lastNameInput)).getText().toString();
        String email = ((EditText)findViewById(R.id.emailInput)).getText().toString();

        Validator validator = new Validator();

        if(!validator.validateName(fName)){
            Toast.makeText(this,R.string.wrongFName,Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!validator.validateName(lName)){
            Toast.makeText(this,R.string.wrongLName,Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!validator.validateEmail(email)){
            Toast.makeText(this,R.string.wrongEmail,Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void authenticateUser(String response){
        JSON json = new JSON(response);
        Authentication auth = new Authentication();
        Boolean authenticationResult = auth.authenticateUser(json.key("data"),this);
        if (authenticationResult){
            WebService webService = new WebService();
            Response.Listener<String> onSuccess = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    logInUser(response);
                }
            };
            Response.ErrorListener onFail = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    networkError();
                }
            };
            webService.loginWithUserID(false,onSuccess,onFail,this);
        }
    }

    private void networkError(){
        spinner.setVisibility(View.GONE);
        Toast.makeText(this,R.string.networkError,Toast.LENGTH_LONG).show();
    }

    private void serverError(){
        spinner.setVisibility(View.GONE);
        Toast.makeText(this,R.string.serverError,Toast.LENGTH_LONG).show();
    }

    private void logInUser(String response){
        JSON json = new JSON(response);
        spinner.setVisibility(View.GONE);
        if(json.key("code").stringValue().equals("200")){
            SharedPreferences sp = this.getSharedPreferences("itqanIsteqdam", MODE_PRIVATE);
            SharedPreferences.Editor spManager = sp.edit();
            spManager.putString("SID",json.key("session_token").stringValue());
            spManager.apply();
            Intent intent = new Intent(this,Home.class);
            startActivity(intent);
        }else{
            serverError();
        }
    }

}
