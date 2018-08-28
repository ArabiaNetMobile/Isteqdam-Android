package arabianet.com.isteqdam.Controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import arabianet.com.isteqdam.R;

public class NoInternet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this,R.string.noInterntConn,Toast.LENGTH_LONG);
    }
}
