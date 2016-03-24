package tw.com.taipower.simpleuii;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by BT on 2016/3/10.
 */
public class Utils {
    public static void writeFile(Context context,String fileName,String content )
    {
        try
        {
            FileOutputStream fos =context.openFileOutput(fileName,Context.MODE_APPEND);//APPEND上去
            //FileOutputStream fos =context.openFileOutput(fileName,Context.MODE_PRIVATE);//複寫
            fos.write(content.getBytes());
            fos.close();

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public static String readFile(Context context,String fileName )
    {

        try
        {
            FileInputStream fis = context.openFileInput(fileName);
            byte[] buffer = new byte[1024];
            fis.read(buffer, 0, buffer.length);
            fis.close();
            return new String(buffer);

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return "";
    }

    public  static  Uri getPhotoUri()
    {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        if(dir.exists() == false){
            dir.mkdir();
        }

        File file = new File(dir, "simple_photo.png");
        return Uri.fromFile(file);
    }
}
