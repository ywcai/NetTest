package ywcai.ls.mobileutil.tools.ScanPort.model;


public class ScanPortState {
    //0等待开始任务,1任务运行中，2任务完成,等待处理数据，-1表示没有任务
    public int scanTaskState = -1;
    public String targetIp = "";
    //    public List<Integer> targetPorts = new ArrayList<>();
    public int startPort = 0, endPort = 0;

    public void reset() {
        scanTaskState = -1;
    }
}
