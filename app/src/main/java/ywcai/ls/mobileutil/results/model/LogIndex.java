package ywcai.ls.mobileutil.results.model;

/**
 * Created by zmy_11 on 2017/10/3.
 */

public class LogIndex {
    public int cacheTypeIndex;//取自AppConfig中的索引
    public String cacheFileName;//名字等于TYPE+"-"+START_TIME格式。后台使用，前台不展示
    public String aliasFileName;//自行编辑的别名，在列表中显示，默认显示当前时间。
    public String remarks;//备注信息。默认为pingState中ip,max,min,avg,loss,total的格式化信息。
    public String logTime;
}
