package ywcai.ls.mobileutil.global.util.statics;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by zmy_11 on 2017/6/30.
 */

public class LsLog {
    public static void saveLog(String text) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return ;
        }
        String fileDirPath = Environment.getExternalStorageDirectory().toString() +
                File.separator +"LsLog" ;
        try {
            File fileDir = new File(fileDirPath);
            if (!fileDir.exists()) {
                fileDir.mkdir();
            }
            File f = new File(fileDir, "lsLog.log");
            if (!f.exists()) {
                f.createNewFile();
                if (!f.exists()) {
                    return ;
                }
            }
            FileOutputStream out = new FileOutputStream(f, true);
            out.write((text+"\n").getBytes("utf-8"));
            out.flush();
            out.close();
        } catch (Exception e) {

        }
    }
}
