package arabianet.com.isteqdam.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.hbb20.CountryCodePicker;
import java.util.HashMap;

import arabianet.com.isteqdam.Model.Validator;
import arabianet.com.isteqdam.Model.WebService;
import arabianet.com.isteqdam.R;
import eu.amirs.JSON;

public class EnterMobile extends AppCompatActivity {

    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_mobile);
        spinner = (ProgressBar)findViewById(R.id.progressBarEnterMobile);
        spinner.setVisibility(View.GONE);
    }

    public void confirmPhone(View view){
        CountryCodePicker ccp = (CountryCodePicker) findViewById(R.id.countryPicker);
        EditText mobileInputField = (EditText) findViewById(R.id.phoneEditText);
        String userPhoneNumber = mobileInputField.getText().toString();
        final String mobileNumber = "00"+ccp.getSelectedCountryCode()+userPhoneNumber;

        Validator validator = new Validator();
        if(validator.validateMobileNumber(mobileNumber)){

            spinner.setVisibility(View.VISIBLE);
            Listener<String> onSuccess = new Listener<String>() {
                @Override
                public void onResponse(String response) {

                    recieveConfrimCode(response,mobileNumber);
                }
            };
            ErrorListener onFail = new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    networkError();
                }
            };
            HashMap<String,String> params = new HashMap<>();
            params.put("mobile_num",mobileNumber);
            WebService webService = new WebService();
            webService.requestConfirmCode(params,false,onSuccess,onFail,this);
        }else{
            Toast.makeText(this,R.string.wrongPhoneNumber,Toast.LENGTH_SHORT).show();
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

    private void recieveConfrimCode(String response,String mobileNumber){
        JSON json = new JSON(response);
        spinner.setVisibility(View.GONE);
        if(json.key("code").stringValue().equals("200")){
            Intent intent = new Intent(this,ConfirmCode.class);
            intent.putExtra("mobileNumber",mobileNumber);
            intent.putExtra("msgToken",json.key("token").stringValue());
            startActivity(intent);
        }else{
            serverError();
        }
    }

}
