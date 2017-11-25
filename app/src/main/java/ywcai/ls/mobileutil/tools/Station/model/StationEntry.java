package ywcai.ls.mobileutil.tools.Station.model;

public class StationEntry {
    public int lac = -1, cid = -1, rsp = -1, netType = -1;

    @Override
    public String toString() {
        return "StationEntry{" +
                "lac=" + lac +
                ", cid=" + cid +
                ", rsp=" + rsp +
                ", netType=" + netType +
                '}';
    }

}
