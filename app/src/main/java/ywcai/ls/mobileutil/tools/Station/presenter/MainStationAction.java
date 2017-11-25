package ywcai.ls.mobileutil.tools.Station.presenter;


import com.github.mikephil.charting.charts.LineChart;

import rx.functions.Action1;
import ywcai.ls.mobileutil.global.util.statics.InStallService;
import ywcai.ls.mobileutil.service.LsConnection;
import ywcai.ls.mobileutil.service.StationService;
import ywcai.ls.mobileutil.tools.Station.presenter.inf.MainStationActionInf;


public class MainStationAction implements MainStationActionInf {
    StationService stationService = null;
    LsConnection lsConnection;

    public MainStationAction() {
        lsConnection = new LsConnection(new Action1() {
            @Override
            public void call(Object o) {
                if (o != null) {
                    stationService = (StationService) o;
                } else {
                    stationService = null;
                }
            }
        });
        InStallService.bindService(StationService.class, lsConnection);
    }


    @Override
    public void startWork() {
        if(stationService!=null)
        {
            stationService.stationProcess.startProcess();
        }
    }

    @Override
    public void addTask() {
        if(stationService!=null)
        {
            stationService.stationProcess.addTask();
        }
    }

    @Override
    public void removeTask() {
        if(stationService!=null)
        {
            stationService.stationProcess.removeTask();
        }
    }

    @Override
    public void popOperatorMenu(boolean isShow) {
        if(stationService!=null)
        {
            stationService.stationProcess.popOperatorMenu(isShow);
        }
    }

    @Override
    public void saveLogLocal() {
        if(stationService!=null)
        {
            stationService.stationProcess.saveLogLocal();
        }
    }

    @Override
    public void saveLogRemote() {
        if(stationService!=null)
        {
            stationService.stationProcess.saveLogRemote();
        }
    }

    @Override
    public void clearTask() {
        if(stationService!=null)
        {
            stationService.stationProcess.clearTask();
        }
    }

    @Override
    public void saveBitmap(LineChart lineChart) {
        if(stationService!=null)
        {
            stationService.stationProcess.saveBitmap(lineChart);
        }
    }

    @Override
    public void setToDtMode(boolean isDtMode) {
        if(stationService!=null)
        {
//            stationService.stationProcess.setToDtMode(isDtMode);
        }
    }

    @Override
    public void sendToRemoteReal(boolean isSend) {
        if(stationService!=null)
        {
//            stationService.stationProcess.sendToRemoteReal(isSend);
        }
    }
}
