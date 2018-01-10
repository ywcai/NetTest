package ywcai.ls.mobileutil.tools.Station.presenter;


import rx.functions.Action1;

import ywcai.ls.mobileutil.global.util.statics.InStallService;

import ywcai.ls.mobileutil.service.LsConnection;
import ywcai.ls.mobileutil.service.StationService;
import ywcai.ls.mobileutil.tools.Station.presenter.inf.MainStationActionInf;


public class MainStationAction implements MainStationActionInf {
    StationService stationService = null;
    LsConnection lsConnection;
    StationProcess stationProcess = null;

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
    public void setProcess() {
        InStallService.waitService(stationService);
        if (stationService != null) {
            stationProcess = stationService.getStationProcess();
        }
    }

    @Override
    public void saveLogLocal() {
        if (stationProcess != null) {
            stationProcess.saveLogLocal();
        }
    }

    @Override
    public void saveLogRemote() {
        if (stationProcess != null) {
            stationProcess.saveLogRemote();
        }
    }

    @Override
    public void clearTask() {
        if (stationProcess != null) {
            stationProcess.clearTask();
        }
    }

    @Override
    public void selectFlexButton(int pos) {
        if (stationProcess != null) {
            stationProcess.setFlexButton(pos);
        }
    }
}
