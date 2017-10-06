package ywcai.ls.mobileutil.global.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ywcai.ls.mobileutil.global.model.LogIndex;
import ywcai.ls.mobileutil.global.model.User;
import ywcai.ls.mobileutil.global.model.instance.MainApplication;
import ywcai.ls.mobileutil.global.util.statics.LsLog;
import ywcai.ls.mobileutil.tools.Ping.model.PingState;

public class CacheProcess {
    private final String USER = "USER";
    private final String PING_STATE = "PING_STATE";
    private final String LOG_INDEX = "LOG_INDEX";
    File file = MainApplication.getInstance().getFilesDir();

    //登陆信息缓存
    public void setCacheUser(User user) {
        user.setSign();
        Gson gson = new Gson();
        String cache = gson.toJson(user, User.class);
        setCache(USER, cache);
    }

    public User getCacheUser() {
        String cache = getCache(USER);
        if (cache.equals("null")) {
            return null;
        }
        Gson gson = new Gson();
        User user = gson.fromJson(cache, User.class);
        if (user == null) {
            return null;
        }
        if (!user.isVal()) {
            return null;
        }
        return user;
    }

    public void setCachePingState(PingState pingState) {
        Gson gson = new Gson();
        String cache = gson.toJson(pingState, PingState.class);
        setCache(PING_STATE, cache);
    }

    //缓存正在PING测试的状态
    public PingState getCachePingState() {
        String cache = getCache(PING_STATE);
        if (cache.equals("null")) {
            return null;
        }
        Gson gson = new Gson();
        PingState pingState = null;
        try {
            pingState = gson.fromJson(cache, PingState.class);
        } catch (Exception e) {

        }
        if (pingState == null) {
            return null;
        }
        return pingState;
    }

    public void setPingResult(String logFlag, List<Float> list) {
        //// TODO: 2017/10/3 当null时，清除该缓存文件
        if(list==null)
        {
            deleteCache(logFlag);
            return;
        }
        String cache = "null";
        LsLog.saveLog(cache);
        try {
            Gson gson = new Gson();
            cache = gson.toJson(list,new TypeToken<List<Float>>() {
            }.getType());
        } catch (Exception e) {
        }
        setCache(logFlag, cache);
    }

    public List<Float> getPingResult(String logFlag) {
        String cache = getCache(logFlag);
        Gson gson = new Gson();
        List<Float> list = null;
        if (!cache.equals("null")) {
            try {
                list = gson.fromJson(cache, new TypeToken<List<Float>>() {
                }.getType());
            } catch (Exception e) {

            }
        }
        return list;
    }

    public void addCacheLogIndex(LogIndex logIndex) {
        List<LogIndex> temp = getCacheLogIndex();
        if (temp == null) {
            temp = new ArrayList<LogIndex>();
        }
        temp.add(logIndex);
        Gson gson = new Gson();
        String cache = gson.toJson(temp);
        setCache(LOG_INDEX, cache);
    }

    public List<LogIndex> getCacheLogIndex() {
        String strLogIndex = getCache(LOG_INDEX);
        List<LogIndex> indexList = null;
        if (!strLogIndex.equals("null")) {
            Gson gson = new Gson();
            try {
                indexList = gson.fromJson(strLogIndex, new TypeToken<List<LogIndex>>() {
                }.getType());
            } catch (Exception e) {

            }
        }
        return indexList;
    }

    //
    //持久化的通用方法
    private void setCache(String fileName, String cache) {
        File f = new File(file, fileName);
        FileOutputStream fos = null;
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            fos = MainApplication.getInstance().getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(cache.getBytes("utf-8"));
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCache(String fileName) {
        String cache = "null";
        File f = new File(file, fileName);
        if (f.exists()) {
            try {
                FileInputStream in = new FileInputStream(f);
                InputStreamReader inputStreamReader = new InputStreamReader(in, "utf-8");
                BufferedReader reader = new BufferedReader(inputStreamReader);
                cache = "";
                String s = "";
                while ((s = reader.readLine()) != null) {
                    cache += s + "\n";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "null";
            }
        }
        return cache;
    }
    private void deleteCache(String fileName)
    {
        File f = new File(file, fileName);
        if (f.exists()) {
            try {
                f.delete();
            } catch (Exception e) {

            }
        }
    }
}
