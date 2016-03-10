package tw.com.taipower.simpleuii;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    TextView editText;
    CheckBox hideCheckBox;
    SharedPreferences sp;//
    SharedPreferences.Editor editor;//editor
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView);
        editText = (TextView)findViewById(R.id.editText);

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
                editor.putBoolean("hideCheckBox",hideCheckBox.isChecked());
                editor.apply();
            }
        });
    }

    public void submit(View v)
    {
        //Toast.makeText(this,"HelloWorld",Toast.LENGTH_LONG).show();
        //textView.setText("ttt");//更改text屬性
        String text = editText.getText().toString();


        if(hideCheckBox.isChecked())
        {
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();//提示訊息
            textView.setText("*****");
            editText.setText("*****");
            return;
        }
        textView.setText(text);
        editText.setText("");
    }
}
