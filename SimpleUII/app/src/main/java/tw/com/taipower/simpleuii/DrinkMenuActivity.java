package tw.com.taipower.simpleuii;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class DrinkMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("debug", "Drink menu onCreate");
        setContentView(R.layout.activity_drink_menu);
    }

    public void add(View view) {
        Button button = (Button) view;
        int number = Integer.parseInt(button.getText().toString());
        number++;
        button.setText(String.valueOf(number));
    }

    public void cancel(View view) {
        finish();//onDestory()
    }

    public void done(View view)
    {
        Intent data =new Intent();
        data.putExtra("result","order done");
        setResult(RESULT_OK,data);
        finish();
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        Log.d("debug", "Drink menu onStart");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d("debug","Drink menu onResume");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d("debug","Drink menu onPause");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.d("debug","Drink menu onStop");
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        Log.d("debug","Drink menu onRestart");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d("debug","Drink menu onDestroy");
    }
}
