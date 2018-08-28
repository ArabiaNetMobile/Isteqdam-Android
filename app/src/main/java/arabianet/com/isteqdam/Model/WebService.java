package arabianet.com.isteqdam.Model;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.StringRequest;
import android.provider.Settings.Secure;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class WebService {
    private String getBaseURL;
    private String postBaseURL;
    private String APIKey;
    private String APIToken;

    public WebService(){
//        this.postBaseURL = "https://arabia2web.net/client6.119/program/webservice.php?f=";
//        this.getBaseURL = "https://arabia2web.net/client6.119/program/webservice-get.php?f=";
//        this.APIKey = "_com_229mNi6_22";
//        this.APIToken = "8@*****h^@@B!^^^G@****g@^g";
        this.postBaseURL = "http://192.168.3.16/isteqdam/program/webservice.php?f=";
        this.getBaseURL = "http://192.168.3.16/isteqdam/program/webservice-get.php?f=";
        this.APIKey = "_com_i8j3b8n7jj";
        this.APIToken = "8!6^5h%g$$G";
    }

    private void callFunctionOnWebService(String functionName, final HashMap<String,String> functionParams, Boolean isGetRequest,
                                          Listener<String> onSuccess, ErrorListener onFail,AppCompatActivity currentView){

        if (isGetRequest){
            String url = getBaseURL + functionName;
            functionParams.put(this.APIKey,this.APIToken);
            StringRequest request = new StringRequest(Method.GET,url,onSuccess,onFail){
                @Override
                protected Map<String,String>getParams(){
                    return functionParams;
                }
            };
            Network.getInstance(currentView).addToRequestQueue(request);
        }else{
            String url = postBaseURL + functionName;
            functionParams.put(this.APIKey,this.APIToken);
            StringRequest request = new StringRequest(Method.POST,url,onSuccess,onFail){
                @Override
                protected Map<String,String>getParams(){
                    return functionParams;
                }
            };
            Network.getInstance(currentView).addToRequestQueue(request);
        }

    }

    public void requestConfirmCode(final HashMap<String,String> functionParams, Boolean isGetRequest,
                                   Listener<String> onSuccess, ErrorListener onFail,AppCompatActivity currentView){
        callFunctionOnWebService("send_sms",functionParams,isGetRequest,onSuccess,onFail,currentView);
    }

    public void validateConfirmCode(final HashMap<String,String> functionParams, Boolean isGetRequest,
                                   Listener<String> onSuccess, ErrorListener onFail,AppCompatActivity currentView){
        callFunctionOnWebService("checkConfirmation",functionParams,isGetRequest,onSuccess,onFail,currentView);
    }

    public void loginWithUserID( Boolean isGetRequest,Listener<String> onSuccess,
                                 ErrorListener onFail,AppCompatActivity currentView){
        String deviceID = Secure.getString(currentView.getContentResolver(),
                Secure.ANDROID_ID);
        SharedPreferences sp = currentView.getSharedPreferences("itqanIsteqdam", MODE_PRIVATE);
        String userID = sp.getString("userID","");
        HashMap<String,String> functionParams = new HashMap<>();
        functionParams.put("device_ip",deviceID);
        functionParams.put("user_id",userID);
        callFunctionOnWebService("loginWithUserId",functionParams,isGetRequest,onSuccess,onFail,currentView);
    }

    public void registerUser(final HashMap<String,String> functionParams, Boolean isGetRequest,
                                    Listener<String> onSuccess, ErrorListener onFail,AppCompatActivity currentView){
        callFunctionOnWebService("regiesterNewUser",functionParams,isGetRequest,onSuccess,onFail,currentView);
    }

    public void isUserLogged( Boolean isGetRequest,Listener<String> onSuccess,
                                 ErrorListener onFail,AppCompatActivity currentView){

        SharedPreferences sp = currentView.getSharedPreferences("itqanIsteqdam", MODE_PRIVATE);
        String userID = sp.getString("userID","");
        String SID = sp.getString("SID","");
        HashMap<String,String> functionParams = new HashMap<>();
        functionParams.put("session_token",SID);
        functionParams.put("user_id",userID);
        callFunctionOnWebService("isLogged",functionParams,isGetRequest,onSuccess,onFail,currentView);
    }

    public void loginWithUserNameAndPass(final HashMap<String,String> functionParams, Boolean isGetRequest,
                             Listener<String> onSuccess, ErrorListener onFail,AppCompatActivity currentView){
        String deviceID = Secure.getString(currentView.getContentResolver(),
                Secure.ANDROID_ID);
        functionParams.put("device_ip",deviceID);
        callFunctionOnWebService("loginWithUsernamePassword",functionParams,isGetRequest,onSuccess,onFail,currentView);
    }



}
