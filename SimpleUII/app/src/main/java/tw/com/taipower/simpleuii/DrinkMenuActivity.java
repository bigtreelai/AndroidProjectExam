package tw.com.taipower.simpleuii;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        //data.putExtra("result","order done");

        JSONArray array = this.getData();
        data.putExtra("result",array.toString());
        setResult(RESULT_OK,data);
        finish();
    }

    public JSONArray getData()
    {
        LinearLayout rootLinearLayout =(LinearLayout)findViewById(R.id.root);
        int count = rootLinearLayout.getChildCount();

        JSONArray array =new JSONArray();

        for(int i = 0;i<count-1;i++)
        {
            Log.d("testtttttttttt",new Integer(i).toString());
            LinearLayout ll = (LinearLayout)rootLinearLayout.getChildAt(i);
            TextView drinkNameTextView =(TextView)ll.getChildAt(0);
            Button lButton =(Button)ll.getChildAt(1);
            Button mButton =(Button)ll.getChildAt(2);

            String drinkName =drinkNameTextView.getText().toString();
            int lNumber = Integer.parseInt(lButton.getText().toString());
            int mNumber = Integer.parseInt(mButton.getText().toString());


            JSONObject object = new  JSONObject();
            try {
                object.put("drinkName",drinkName);
                object.put("lNumber",lNumber);
                object.put("mNumber",mNumber);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(object);
        }
        Log.d("testttttttttttttttt","55555");
        return array;
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
