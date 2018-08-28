package arabianet.com.isteqdam.Model;

import android.content.Context;
import android.content.SharedPreferences;

import eu.amirs.JSON;

import static android.content.Context.MODE_PRIVATE;

public class Authentication {

    public Boolean authenticateUser(JSON userAuthentication, Context currentView){
        if(!userAuthentication.key("code").stringValue().equals("301")){
            SharedPreferences sp = currentView.getSharedPreferences("itqanIsteqdam", MODE_PRIVATE);
            SharedPreferences.Editor spManager = sp.edit();
            spManager.putString("userID",userAuthentication.key("user_id").stringValue());
            spManager.apply();
            return true;
        }else{
            return false;
        }
    }

    public int authorizeUser(JSON userAuthorization,Context currentView){
        if(userAuthorization.key("code").stringValue().equals("4000")){
            return -1; //Means user has been blocked
        }else if(userAuthorization.key("code").stringValue().equals("3000")){
            return 0; //Means user has been logged out from system and should re-login
        }else{
            return 1;//Means user still exist logged in system and can use app services
        }
    }

}
