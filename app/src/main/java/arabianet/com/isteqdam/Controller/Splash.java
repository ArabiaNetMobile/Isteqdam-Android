package arabianet.com.isteqdam.Controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import arabianet.com.isteqdam.Model.Authentication;
import arabianet.com.isteqdam.Model.WebService;
import arabianet.com.isteqdam.R;
import eu.amirs.JSON;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setStartScreen();
    }

    private void setStartScreen(){
        SharedPreferences sp = this.getSharedPreferences("itqanIsteqdam", MODE_PRIVATE);
        String SID = sp.getString("SID","");
        System.out.println("SID : " + SID);
        if(SID == ""){
            Intent intent = new Intent(this,EnterMobile.class);
            startActivity(intent);
        }else{
            Response.Listener<String> onSuccess = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    authorizeUser(response);
                }
            };
            Response.ErrorListener onFail = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    networkError();
                }
            };
            WebService webservice = new WebService();
            webservice.isUserLogged(false,onSuccess,onFail,this);
        }

    }

    private void authorizeUser(String response){
        JSON json = new JSON(response);
        Authentication auth = new Authentication();
        int authorizationResult = auth.authorizeUser(json,this);

        if(authorizationResult == -1){
            Intent intent = new Intent(this,Block.class);
            startActivity(intent);
        }else if(authorizationResult == 0){
            Intent intent = new Intent(this,Login.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this,Home.class);
            startActivity(intent);
        }
    }

    private void networkError(){
        Intent intent = new Intent(this,NoInternet.class);
        System.out.println("No Internet");
        startActivity(intent);
    }

}
