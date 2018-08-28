package arabianet.com.isteqdam.Controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import arabianet.com.isteqdam.R;

public class Block extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this,R.string.blocked,Toast.LENGTH_LONG);
    }
}
