package ywcai.ls.mobileutil.tools.Station.presenter;


import rx.functions.Action1;
import ywcai.ls.mobileutil.global.util.statics.InStallService;
import ywcai.ls.mobileutil.service.LsConnection;
import ywcai.ls.mobileutil.service.StationService;
import ywcai.ls.mobileutil.tools.Station.presenter.inf.StationActionInf;



public class StationAction implements StationActionInf {
    StationService stationService = null;
    LsConnection lsConnection;

    public StationAction() {
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
    public void addTask() {

    }

    @Override
    public void stopTask() {

    }

    @Override
    public void saveLog() {

    }
}
