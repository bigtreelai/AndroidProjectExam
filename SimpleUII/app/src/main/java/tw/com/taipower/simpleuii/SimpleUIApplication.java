package tw.com.taipower.simpleuii;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by BT on 2016/3/21.
 */
public class SimpleUIApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);

        Parse.initialize(this);
    }
}
