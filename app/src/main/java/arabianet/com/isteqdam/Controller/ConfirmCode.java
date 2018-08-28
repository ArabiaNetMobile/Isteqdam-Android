package arabianet.com.isteqdam.Controller;

import android.content.Context;
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

public class ConfirmCode extends AppCompatActivity {

    String msgToken = "";
    String mobileNumber = "";
    String confirmCode = "";
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_code);
        spinner = (ProgressBar)findViewById(R.id.progressBarConfirmCode);
        spinner.setVisibility(View.GONE);
        Bundle b = getIntent().getExtras();
        msgToken = b.getString("msgToken");
        mobileNumber = b.getString("mobileNumber");
    }

    public void confirmCode(View view){

        String firstChar = ((EditText)findViewById(R.id.firstChar)).getText().toString();
        String secondChar = ((EditText)findViewById(R.id.secondChar)).getText().toString();
        String thirdChar = ((EditText)findViewById(R.id.thirdChar)).getText().toString();
        String fourthChar = ((EditText)findViewById(R.id.fourthChar)).getText().toString();
        String fifthChar = ((EditText)findViewById(R.id.fifthChar)).getText().toString();
        String sixthChar = ((EditText)findViewById(R.id.sixthChar)).getText().toString();

        confirmCode = firstChar + secondChar + thirdChar;
        confirmCode += fourthChar + fifthChar + sixthChar;

        Validator validator = new Validator();

       if(validator.validateConfirmCode(confirmCode)){
           spinner.setVisibility(View.VISIBLE);
           Response.Listener<String> onSuccess = new Response.Listener<String>() {
               @Override
               public void onResponse(String response) {
                    validateConfirmCode(response);
               }
           };
           Response.ErrorListener onFail = new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError error) {
                   networkError();
               }
           };
           HashMap<String,String> params = new HashMap<>();
           params.put("confirm_code",confirmCode);
           params.put("token",msgToken);
           WebService webService = new WebService();
           webService.validateConfirmCode(params,false,onSuccess,onFail,this);
       }else{
           Toast.makeText(this,R.string.wrongConfirmCode,Toast.LENGTH_LONG).show();

       }


    }

    public void changeNumber(View view){
        finish();
    }

    public void resendCode(View view){
        spinner.setVisibility(View.VISIBLE);
        Response.Listener<String> onSuccess = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                recieveConfrimCode(response,mobileNumber);
            }
        };
        Response.ErrorListener onFail = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                networkError();
            }
        };
        HashMap<String,String> params = new HashMap<>();
        params.put("mobile_num",mobileNumber);
        WebService webService = new WebService();
        webService.requestConfirmCode(params,false,onSuccess,onFail,this);

    }

    private void networkError(){
        spinner.setVisibility(View.GONE);
        Toast.makeText(this,R.string.networkError,Toast.LENGTH_LONG).show();
    }

    private void serverError(){
        spinner.setVisibility(View.GONE);
        Toast.makeText(this,R.string.serverError,Toast.LENGTH_LONG).show();
    }

    private void recieveConfrimCode(String response,String mobileNumber){
        JSON json = new JSON(response);
        spinner.setVisibility(View.GONE);
        if(json.key("code").stringValue().equals("200")){
            this.msgToken = json.key("token").stringValue();
            Toast.makeText(this,R.string.msgSentSuccess,Toast.LENGTH_SHORT).show();
        }else{
            serverError();
        }
    }

    private void validateConfirmCode(String response){
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
        }else{
            Intent intent = new Intent(this,Register.class);
            intent.putExtra("confirmCode",this.confirmCode);
            intent.putExtra("mobileNumber",this.mobileNumber);
            startActivity(intent);
        }
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
