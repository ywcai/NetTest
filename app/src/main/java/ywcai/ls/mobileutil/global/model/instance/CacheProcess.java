package ywcai.ls.mobileutil.global.model.instance;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.login.model.MyUser;
import ywcai.ls.mobileutil.results.model.LogIndex;
import ywcai.ls.mobileutil.results.model.ResultState;
import ywcai.ls.mobileutil.results.model.TaskTotal;
import ywcai.ls.mobileutil.tools.Ping.model.PingState;
import ywcai.ls.mobileutil.tools.ScanPort.model.ScanPortResult;
import ywcai.ls.mobileutil.tools.ScanPort.model.ScanPortState;
import ywcai.ls.mobileutil.tools.Sensor.model.SensorState;
import ywcai.ls.mobileutil.tools.Speed.model.SpeedState;
import ywcai.ls.mobileutil.tools.Station.model.StationEntry;
import ywcai.ls.mobileutil.tools.Station.model.StationState;
import ywcai.ls.mobileutil.tools.Wifi.model.WifiEntry;
import ywcai.ls.mobileutil.tools.Wifi.model.WifiState;

public class CacheProcess {


    private static Object lock = new Object();
    private static CacheProcess cacheProcess = null;
    private final String RESULT_STATE = "RESULT_STATE";
    private final String PING_STATE = "PING_STATE";
    private final String WIFI_STATE = "WIFI_STATE";
    private final String STATION_STATE = "STATION_STATE";
    private final String SCAN_PORT_STATE = "SCAN_PORT_STATE";
    private final String SCAN_PORT_RESULT = "SCAN_PORT_RESULT";
    private final String SENSOR_STATE = "SENSOR_STATE";
    private final String SPEED_STATE = "SPEED_STATE";

    private final String USER = "USER";
    private final String LOG_INDEX = "LOG_INDEX";
    private final String TASK_TOTAL = "TASK_TOTAL";


    File file = MainApplication.getInstance().getFilesDir();


    private CacheProcess() {
    }

    public static CacheProcess getInstance() {
        synchronized (lock) {
            if (cacheProcess == null) {
                cacheProcess = new CacheProcess();
            }
        }
        return cacheProcess;
    }


    /*
    *GLOBAL Module
    *--------------------------------------------------------------------------------------------------
    * */
    public void setCache(String fileName, String cache) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            fos.write(cache.getBytes("utf-8"));
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCache(String fileName) {
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

    public void deleteCache(String fileName) {
        File f = new File(file, fileName);
        if (f.exists()) {
            try {
                f.delete();
            } catch (Exception e) {

            }
        }
    }


    /*
    *Login Module
    *--------------------------------------------------------------------------------------------------
    * */
    public void setCacheUser(MyUser user) {
        if (user == null) {
            setCache(USER, "null");
            return;
        }
        Gson gson = new Gson();
        String cache = gson.toJson(user, MyUser.class);
        setCache(USER, cache);
    }

    public MyUser getCacheUser() {
        String cache = getCache(USER);
        if (cache.equals("null")) {
            return null;
        }
        Gson gson = new Gson();
        MyUser user = gson.fromJson(cache, MyUser.class);
        return user;
    }


    /*
    *MainActivity Module
    *--------------------------------------------------------------------------------------------------
    * */
    public TaskTotal getCacheTaskTotal() {
        TaskTotal taskTotal = null;
        String cache = getCache(TASK_TOTAL);
        if (cache.equals("null")) {
            return new TaskTotal();
        }
        Gson gson = new Gson();
        try {
            taskTotal = gson.fromJson(cache, TaskTotal.class);
        } catch (Exception e) {
            taskTotal = null;
        }
        if (taskTotal == null) {
            return new TaskTotal();
        }
        return taskTotal;
    }

    public void setCacheTaskTotal(TaskTotal taskTotal) {
        Gson gson = new Gson();
        String cache = gson.toJson(taskTotal, TaskTotal.class);
        setCache(TASK_TOTAL, cache);
    }

    public ResultState getResultState() {
        String cache = getCache(RESULT_STATE);
        if (cache.equals("null")) {
            return new ResultState();
        }
        Gson gson = new Gson();
        ResultState resultState = null;
        try {
            resultState = gson.fromJson(cache, ResultState.class);
        } catch (Exception e) {

        }
        if (resultState == null) {
            return new ResultState();
        }
        return resultState;
    }


    /*
    *Result Module
    *--------------------------------------------------------------------------------------------------
    * */
    public void addCacheLogIndex(LogIndex logIndex) {
        if (logIndex == null) {
            return;
        }
        List<LogIndex> temp = getCacheLogIndex();
        if (temp == null) {
            temp = new ArrayList<LogIndex>();
        }
        temp.add(logIndex);
        Gson gson = new Gson();
        String cache = gson.toJson(temp);
        setCache(LOG_INDEX, cache);
        TaskTotal taskTotal = getCacheTaskTotal();
        taskTotal.savedCount = temp.size();
        setCacheTaskTotal(taskTotal);
    }

    public void addCacheLogIndex(List<LogIndex> list) {
        if (list == null) {
            return;
        }
        List<LogIndex> temp = getCacheLogIndex();
        if (temp == null) {
            temp = new ArrayList<LogIndex>();
        }
        temp.addAll(list);
        Gson gson = new Gson();
        String cache = gson.toJson(temp);
        setCache(LOG_INDEX, cache);
        TaskTotal taskTotal = getCacheTaskTotal();
        taskTotal.savedCount = temp.size();
        setCacheTaskTotal(taskTotal);
    }

    public void setCacheLogIndex(List<LogIndex> list) {
        if (list == null) {
            //清除所有日志
            setCache(LOG_INDEX, "null");
            TaskTotal taskTotal = getCacheTaskTotal();
            taskTotal.savedCount = 0;
            setCacheTaskTotal(taskTotal);
            return;
        }
        if (list.size() <= 0) {
            //清除所有日志
            setCache(LOG_INDEX, "null");
            TaskTotal taskTotal = getCacheTaskTotal();
            taskTotal.savedCount = 0;
            setCacheTaskTotal(taskTotal);
            return;
        }
        Gson gson = new Gson();
        String cache = gson.toJson(list);
        setCache(LOG_INDEX, cache);
        TaskTotal taskTotal = getCacheTaskTotal();
        taskTotal.savedCount = list.size();
        setCacheTaskTotal(taskTotal);
    }

    public List<LogIndex> getCacheLogIndex() {
        String strLogIndex = getCache(LOG_INDEX);
        List<LogIndex> indexList = new ArrayList<LogIndex>();
        if (strLogIndex.equals("null") || strLogIndex.equals("")) {
            return indexList;
        }
        Gson gson = new Gson();
        try {
            indexList = gson.fromJson(strLogIndex, new TypeToken<List<LogIndex>>() {
            }.getType());
        } catch (Exception e) {
        }
        if (indexList == null) {
            indexList = new ArrayList<>();
        }
        return indexList;
    }

    public void setResultState(ResultState resultState) {
        String cache = "null";
        if (resultState == null) {
            setCache(RESULT_STATE, cache);
        } else {
            Gson gson = new Gson();
            try {
                cache = gson.toJson(resultState);
            } catch (Exception e) {
            }
        }
        setCache(RESULT_STATE, cache);
    }


    /*
    *PING Module
    *--------------------------------------------------------------------------------------------------
    * */
    public void setCachePingState(PingState pingState) {
        Gson gson = new Gson();
        String cache = gson.toJson(pingState, PingState.class);
        setCache(PING_STATE, cache);
        TaskTotal taskTotal = getCacheTaskTotal();
        if (!pingState.isRunning && !pingState.isProcessResult && pingState.send != 0) {
            taskTotal.state[AppConfig.INDEX_PING] = 100;
        } else {
            taskTotal.state[AppConfig.INDEX_PING] = pingState.send * 100 / pingState.packageCount;
        }
        taskTotal.autoCount();
        setCacheTaskTotal(taskTotal);
    }

    public PingState getCachePingState() {
        String cache = getCache(PING_STATE);
        if (cache.equals("null")) {
            return new PingState();
        }
        Gson gson = new Gson();
        PingState pingState = null;
        try {
            pingState = gson.fromJson(cache, PingState.class);
        } catch (Exception e) {

        }
        if (pingState == null) {
            return new PingState();
        }
        return pingState;
    }

    public void setPingResult(String logFlag, List<Float> list) {
        //当null时，清除该缓存文件
        if (list == null) {
            deleteCache(logFlag);
            return;
        }
        String cache = "null";
        try {
            Gson gson = new Gson();
            cache = gson.toJson(list, new TypeToken<List<Float>>() {
            }.getType());
        } catch (Exception e) {
            cache = "null";
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


    /*
    *WIFI Module
    *--------------------------------------------------------------------------------------------------
    * */
    public void deleteWifiTask(String logName) {
        deleteCache(logName);
    }

    public void setWifiTaskResult(WifiEntry wifiEntry) {
        //当null时，清除该缓存文件
        List list = getWifiTaskResult(wifiEntry.logFlag);
        list.add(0, wifiEntry.dbm);
        String cache = "null";
        try {
            Gson gson = new Gson();
            cache = gson.toJson(list, new TypeToken<List<Integer>>() {
            }.getType());
        } catch (Exception e) {
        }
        setCache(wifiEntry.logFlag, cache);
    }

    public List<Integer> getWifiTaskResult(String logFlag) {
        String cache = getCache(logFlag);
        Gson gson = new Gson();
        List<Integer> list = new ArrayList<>();
        if (!cache.equals("null")) {
            try {
                list = gson.fromJson(cache, new TypeToken<List<Integer>>() {
                }.getType());
            } catch (Exception e) {

            }
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public WifiState getWifiState() {
        String cache = getCache(WIFI_STATE);
        if (cache.equals("null")) {
            return new WifiState();
        }
        Gson gson = new Gson();
        WifiState wifiState = null;
        try {
            wifiState = gson.fromJson(cache, WifiState.class);
        } catch (Exception e) {

        }
        if (wifiState == null) {
            return new WifiState();
        }
        return wifiState;
    }

    public void setWifiState(WifiState wifiState) {
        String cacheWifiState = "null";
        if (wifiState != null) {
            Gson gson = new Gson();
            try {
                cacheWifiState = gson.toJson(wifiState);
            } catch (Exception e) {
            }
        }
        setCache(WIFI_STATE, cacheWifiState);
    }


    /*
      *Station Module
      *--------------------------------------------------------------------------------------------------
      * */
    public StationState getStationState() {
        String cache = getCache(STATION_STATE);
        if (cache.equals("null")) {
            return new StationState();
        }
        Gson gson = new Gson();
        StationState stationState = null;
        try {
            stationState = gson.fromJson(cache, StationState.class);
        } catch (Exception e) {

        }
        if (stationState == null) {
            return new StationState();
        }
        return stationState;
    }

    public void setStationState(StationState stationState) {
        String cache = "null";
        if (stationState == null) {
            setCache(STATION_STATE, cache);
        } else {
            Gson gson = new Gson();
            try {
                cache = gson.toJson(stationState);
            } catch (Exception e) {
            }
        }
        setCache(STATION_STATE, cache);
    }

    public List<StationEntry> getStationRecord(String logName) {
        String cache = getCache(logName);
        if (cache.equals("null")) {
            return new ArrayList<>();
        }
        Gson gson = new Gson();
        List<StationEntry> stationStateList = null;
        try {
            stationStateList = gson.fromJson(cache, new TypeToken<List<StationEntry>>() {
            }.getType());
        } catch (Exception e) {

        }
        if (stationStateList == null) {
            return new ArrayList<>();
        }
        return stationStateList;
    }

    public void setStationRecord(String logName, StationEntry stationEntry) {
        String cache = "null";
        if (stationEntry == null) {
            deleteCache(logName);
            return;
        }
        List<StationEntry> list = getStationRecord(logName);
        list.add(0, stationEntry);
        Gson gson = new Gson();
        try {
            cache = gson.toJson(list, new TypeToken<List<StationEntry>>() {
            }.getType());
        } catch (Exception e) {

        }
        setCache(logName, cache);
    }

    /*
    *ScanPort Module
    *--------------------------------------------------------------------------------------------------
    * */
    public ScanPortState getScanPortState() {
        String cache = getCache(SCAN_PORT_STATE);
        if (cache.equals("null")) {
            return new ScanPortState();
        }
        Gson gson = new Gson();
        ScanPortState scanPortState = null;
        try {
            scanPortState = gson.fromJson(cache, ScanPortState.class);
        } catch (Exception e) {

        }
        if (scanPortState == null) {
            return new ScanPortState();
        }
        return scanPortState;
    }

    public void setScanPortState(ScanPortState scanPortState) {
        String cache = "null";
        if (scanPortState == null) {
            setCache(SCAN_PORT_STATE, cache);
        } else {
            Gson gson = new Gson();
            try {
                cache = gson.toJson(scanPortState);
            } catch (Exception e) {
            }
        }
        setCache(SCAN_PORT_STATE, cache);
    }

    public ScanPortResult getScanPortResult() {
        String cache = getCache(SCAN_PORT_RESULT);
        if (cache.equals("null")) {
            return new ScanPortResult();
        }
        Gson gson = new Gson();
        ScanPortResult scanPortResult = null;
        try {
            scanPortResult = gson.fromJson(cache, ScanPortResult.class);
        } catch (Exception e) {

        }
        if (scanPortResult == null) {
            return new ScanPortResult();
        }
        return scanPortResult;
    }

    public void setScanPortResult(ScanPortResult scanPortResult) {
        String cache = "null";
        if (scanPortResult == null) {
            setCache(SCAN_PORT_RESULT, cache);
        } else {
            Gson gson = new Gson();
            try {
                cache = gson.toJson(scanPortResult);
            } catch (Exception e) {
            }
        }
        setCache(SCAN_PORT_RESULT, cache);
    }

    /*
    *Sensor Module
    *--------------------------------------------------------------------------------------------------
    * */
    public SensorState getSensorState() {
        String cache = getCache(SENSOR_STATE);
        if (cache.equals("null")) {
            return new SensorState();
        }
        Gson gson = new Gson();
        SensorState sensorState = null;
        try {
            sensorState = gson.fromJson(cache, SensorState.class);
        } catch (Exception e) {

        }
        if (sensorState == null) {
            return new SensorState();
        }
        return sensorState;
    }

    public void setSensorState(SensorState sensorState) {
        String cache = "null";
        if (sensorState == null) {
            setCache(SENSOR_STATE, cache);
        } else {
            Gson gson = new Gson();
            try {
                cache = gson.toJson(sensorState);
            } catch (Exception e) {
            }
        }
        setCache(SENSOR_STATE, cache);
    }

        /*
    *SpeedTest Module
    *--------------------------------------------------------------------------------------------------
    * */

    public SpeedState getSpeedState() {

        String cache = getCache(SPEED_STATE);
        if (cache.equals("null")) {
            return new SpeedState();
        }
        Gson gson = new Gson();
        SpeedState speedState = null;
        try {
            speedState = gson.fromJson(cache, SpeedState.class);
        } catch (Exception e) {

        }
        if (speedState == null) {
            return new SpeedState();
        }
        return speedState;
    }

    public void setSpeedState(SpeedState speedState) {
        String cache = "null";
        if (speedState == null) {
            setCache(SPEED_STATE, cache);
        } else {
            Gson gson = new Gson();
            try {
                cache = gson.toJson(speedState);
            } catch (Exception e) {
            }
        }
        setCache(SPEED_STATE, cache);
    }
}

