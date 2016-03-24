package tw.com.taipower.simpleuii;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_MENU_ACTIVITY = 0;
    private static final int REQUEST_CODE_CAMERA = 1;

    TextView textView;
    EditText editText;
    CheckBox hideCheckBox;
    ListView listView;
    Spinner spinner;
    SharedPreferences sp;//
    SharedPreferences.Editor editor;//editor
    String menuResult="";
    List<ParseObject> queryResult;
    ImageView photoview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("debug", "Main menu onCreate");
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView);
        editText = (EditText)findViewById(R.id.editText);
        listView = (ListView)findViewById(R.id.listView);
        spinner = (Spinner)findViewById(R.id.spinner);
        photoview = (ImageView)findViewById(R.id.imageView);
        sp=getSharedPreferences("setting", Context.MODE_PRIVATE);//從setting裡
        editor = sp.edit();//

        editText.setText(sp.getString("editText", ""));//editText藉由sp拿出來

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

        hideCheckBox.setChecked(sp.getBoolean("hideCheckBox", false));

        hideCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("hideCheckBox", hideCheckBox.isChecked());
                editor.apply();
            }
        });



        ParseObject testObject = new ParseObject("HomeworkParse");
        testObject.put("sid", "And26307");
        testObject.put("email", "lai@gmail.com");
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d("debug", e.toString());
                }
            }
        });
//        setListView();

//        setSpinner();

        this.setHistory();
        this.setStoreInfo();

    }

//    public void setListView()
//    {
//        String[] data =Utils.readFile(this, "history.txt").split("\n");
//        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
//        listView.setAdapter(adapter);
//
//    }

//    public void setSpinner()
//    {
//        String[] data =getResources().getStringArray(R.array.storeInfo);
//        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,data);
//        spinner.setAdapter(adapter);
//
//    }

    public void submit(View v)
    {
        //Toast.makeText(this,"HelloWorld",Toast.LENGTH_LONG).show();
        //textView.setText("ttt");//更改text屬性
        String text = editText.getText().toString();

        ParseObject orderObject =new ParseObject("Order");

        orderObject.put("note", text);
        orderObject.put("storeInfo", spinner.getSelectedItem());
        orderObject.put("menu", menuResult);

        orderObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(MainActivity.this, "Submit OK", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Submit Fail", Toast.LENGTH_LONG).show();
                }
            }
        });
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

        //setListView();
        setHistory();
    }

    public void goToMenu(View view)
    {
        Intent intent = new Intent();
        intent.setClass(this, DrinkMenuActivity.class);
        startActivityForResult(intent, REQUEST_CODE_MENU_ACTIVITY);
        //startActivity(intent);

        //方法一
        //Intent intent = new Intent(this, DrinkMenuAvtivity.class);

        //方法二
        // Intent intent = new Intent(MainActivity.this, DrinkMenuAvtivity.class);


        //方法三
        // Intent intent = new Intent();
        // intent.setClass(this, DrinkMenuAvtivity.class);

    }

    private void setHistory()
    {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Order");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG);
                    return;
                }
                queryResult = list;
                List<Map<String, String>> data = new ArrayList<>();
                //Log.d("OrderObject", queryResult.toString());
                Log.d("queryResult", queryResult.size() + "");


                int count;
                String JObjectString = "";
                for (int i = 0; i < queryResult.size(); i++) {
                    ParseObject object = queryResult.get(i);
                    count = 0;
                    String note = object.getString("note");
                    String storeInfo = object.getString("storeInfo");
                    String menu = object.getString("menu");
                    //String s=menu.toString();

                    //取得每筆的訂單
                    JObjectString = menu.toString();
                    Log.d("999", JObjectString+"length="+JObjectString.length());
                    /*
                    if(!s.equals("") && s!=null && s.length()>2)
                    {
                        if(i==0)
                        {
                            JObjectString += s.replace("]", "") + ",";
                        }
                        else if(i == queryResult.size()-1)
                        {
                            JObjectString += s.replace("[", "") ;
                        }
                        else
                        {
                            s=s.replace("[", "");
                            s=s.replace("]", "");
                            JObjectString+=s+",";
                        }
                    }*/


                    //Log.d("Menu", menu.length() + "");
                    //Log.d("Menu2", menu.toString());

                    /*如果JObjectString不是空值 && 不是空字串 && 字串長度>2(因為有些字串只有[])
                      就執行countDrinkNum，可以取得每筆訂單總數
                    */
                    if(!JObjectString.equals("") && JObjectString!=null && JObjectString.length()>2)
                    {
                        count = countDrinkNum(JObjectString);
                    }
                    Map<String, String> item = new HashMap<>();
                    item.put("note", note);
                    item.put("storeInfo", storeInfo);
                    item.put("drinkNum", count+"");//把COUNT放進去

                    data.add(item);
                }
                //JObjectString=JObjectString.substring(0,JObjectString.length()-1)+"]";
                Log.d("JObjectString", JObjectString);


                String[] from = {"note", "storeInfo", "drinkNum"};
                int[] to = {R.id.note, R.id.storeInfo, R.id.drinkNum};

                SimpleAdapter simpleAdapter = new SimpleAdapter(
                        MainActivity.this, data, R.layout.listview_item, from, to);

                listView.setAdapter(simpleAdapter);
            }
        });
    }

    private int countDrinkNum(String JObjectString)
    {
        int count =0;
        Log.d("SSS11111", JObjectString);
        try {
            JSONArray array =new JSONArray(JObjectString);
            int lNumber ;
            int mNumber;
            int l;
            int m;
            for(int i=0 ;i<array.length();i++)
            {
                 lNumber =0;
                 mNumber= 0;
                 l = 0;
                 m = 0;
                JSONObject object = array.getJSONObject(i);

                //String name = object.getString("name");
                Log.d("JObjectStringName", i + ":" + object.getString("name"));

                //如果object有這個lNumber key，取value出來
                if(!object.isNull("lNumber"))
                {

                    lNumber = object.getInt("lNumber");
                    Log.d("lNumber", lNumber+"");
                }

                //如果object有這個l key，取value出來
                if(!object.isNull("l"))
                {
                    l = object.getInt("l");
                    Log.d("l", l+"");
                }

                //如果object有這個mNumber key，取value出來
                if(!object.isNull("mNumber"))
                {
                    mNumber = object.getInt("mNumber");
                    Log.d("mNumber", mNumber+"");
                }

                //如果object有這個m key，取value出來
                if(!object.isNull("m"))
                {
                    m = object.getInt("m");
                    Log.d("m", m+"");
                }

                //count每筆裡的杯數
                count+=lNumber+mNumber+l+m;
            }

        } catch (JSONException e) {
            Log.d("888", "888");
            e.printStackTrace();
        }

        Log.d("countSSS", count+"");

        //回傳每筆杯數
        return count;

    }
    private void setStoreInfo()
    {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("StoreInfo");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG);
                    return;
                }

                String[] stores = new String[list.size()];
                for(int i =0; i<list.size();i++)
                {
                    ParseObject object =list.get(i);
                    stores[i]=object.getString("name")+","+object.getString("address");
                }

                ArrayAdapter<String> storeAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, stores);
                spinner.setAdapter(storeAdapter);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("debug", "Main menu onActivityResult");
        Log.d("00000", "requestCode="+requestCode);
        if(requestCode == REQUEST_CODE_MENU_ACTIVITY )
        {
            if(resultCode == RESULT_OK)
            {
                //textView.setText(data.getStringExtra("result"));
                menuResult= data.getStringExtra("result");
                Log.d("555 menuResult:", menuResult);
                try {
                JSONArray array =new JSONArray(menuResult);
                    String text="";
                    for(int i=0 ;i<array.length();i++)
                    {
                        JSONObject order = array.getJSONObject(i);

                            String name =order.getString("drinkName");
                            String lNumber =String.valueOf(order.getInt("lNumber"));
                            String mNumber =String.valueOf(order.getInt("mNumber"));
                        text += name+ "大杯："+ lNumber+ "、中杯："+ mNumber+ "\n";

                    }
                    Log.d("11111 Result:", "111111");
                    //photoview.setImageURI(Utils.getPhotoUri());
                    Log.d("22222 Result:", "22222");
                    textView.setText(text);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        else if(requestCode == REQUEST_CODE_CAMERA)
        {
            Log.d("99999 Camera in:", "OK");
            if(resultCode == RESULT_OK){
                //photoview.setImageURI(Utils.getPhotoUri());

                Log.d("DMode Camera Result:", "OK1");
                photoview.setImageURI(Utils.getPhotoUri());
                Log.d("DMode Camera Result:", "OK2");
            }
        }
        /*
        else
        {
            Log.d("777777 Result:", "777777");
            //photoview.setImageURI(Utils.getPhotoUri());
            Log.d("888888 Result:", "888888");
        }*/
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
        Log.d("debug", "Main menu onDestroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d("DebugMode", "main menu onOptionsItemSelected");
        int id = item.getItemId();

        if(id == R.id.action_take_photo){
            Toast.makeText(MainActivity.this, "take Photo", Toast.LENGTH_LONG).show();
            this.goToCamera();
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToCamera()
    {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Utils.getPhotoUri());
        startActivityForResult(intent, REQUEST_CODE_CAMERA);

        //this.startActivity(intent);
    }
}
