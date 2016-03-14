package tw.com.taipower.simpleuii;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_MENU_ACTIVITY = 0;

    TextView textView;
    EditText editText;
    CheckBox hideCheckBox;
    ListView listView;
    Spinner spinner;
    SharedPreferences sp;//
    SharedPreferences.Editor editor;//editor
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("debug", "Main menu onCreate");
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView);
        editText = (EditText)findViewById(R.id.editText);
        listView = (ListView)findViewById(R.id.listView);
        spinner = (Spinner)findViewById(R.id.spinner);

        sp=getSharedPreferences("setting", Context.MODE_PRIVATE);//從setting裡
        editor = sp.edit();//

        editText.setText(sp.getString("editText",""));//editText藉由sp拿出來

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                editor.putString("editText", editText.getText().toString());//
                editor.apply();

                //偵測enter
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    submit(v);
                }
                return false;
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //偵測虛擬鍵盤
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submit(v);
                    return true;
                }
                return false;
            }
        });

        hideCheckBox = (CheckBox)findViewById(R.id.checkBox);

        hideCheckBox.setChecked(sp.getBoolean("hideCheckBox",false));

        hideCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("hideCheckBox", hideCheckBox.isChecked());
                editor.apply();
            }
        });
        setListView();
        setSpinner();
    }
    public void setListView()
    {
        String[] data =Utils.readFile(this, "history.txt").split("\n");
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);

    }

    public void setSpinner()
    {
        String[] data =getResources().getStringArray(R.array.storeInfo);
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,data);
        spinner.setAdapter(adapter);

    }

    public void submit(View v)
    {
        //Toast.makeText(this,"HelloWorld",Toast.LENGTH_LONG).show();
        //textView.setText("ttt");//更改text屬性
        String text = editText.getText().toString();
        Utils.writeFile(this, "history.txt", text + '\n');

        if(hideCheckBox.isChecked())
        {
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();//提示訊息
            textView.setText("*****");
            editText.setText("*****");
            return;
        }
        textView.setText(text);
        editText.setText("");

        setListView();
    }
    public void goToMenu(View view)
    {
        Intent intent = new Intent();
        intent.setClass(this, DrinkMenuActivity.class);
        startActivityForResult(intent,REQUEST_CODE_MENU_ACTIVITY);
        //startActivity(intent);

        //方法一
        //Intent intent = new Intent(this, DrinkMenuAvtivity.class);

        //方法二
        // Intent intent = new Intent(MainActivity.this, DrinkMenuAvtivity.class);


        //方法三
        // Intent intent = new Intent();
        // intent.setClass(this, DrinkMenuAvtivity.class);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("debug", "Main menu onActivityResult");

        if(requestCode == REQUEST_CODE_MENU_ACTIVITY )
        {
            if(resultCode == RESULT_OK)
            {
                textView.setText(data.getStringExtra("result"));
            }
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Log.d("debug", "Main menu onStart");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d("debug","Main menu onResume");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d("debug", "Main menu onPause");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.d("debug", "Main menu onStop");
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        Log.d("debug", "Main menu onRestart");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d("debug","Main menu onDestroy");
    }
}
