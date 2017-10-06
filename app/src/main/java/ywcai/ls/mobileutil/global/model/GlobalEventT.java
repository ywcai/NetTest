package ywcai.ls.mobileutil.global.model;

public  class GlobalEventT {
    public static final int un_login=80001;
    public static final int is_login=80002;
    public static final int is_Run=80003;


    //Ping包返回标识
    public static final int ping_update_chart_point=90001;//更新当前序列坐标Y值
    public static final int ping_update_chart_desc=90002;//更新图片描述


    //Ping包读取缓存更新UI
    public static final int ping_set_bar_size_package=90003;//更新包大小进度条
    public static final int ping_set_bar_size_thread=90004;//更新线程大小进度条
    public static final int ping_set_input_text_ip=90005;//更新线程大小进度条
    public static final int ping_set_chart_data_size=90006;//初始数据宽度，与包大小匹配
    public static final int ping_repair_chart_line=90007;//补全丢失曲线

    public static final int ping_set_form_free=90008;//操作表单禁用，正在运行任务，按钮变为停止按钮
    public static final int ping_set_form_busy=90009;//操作表单启用，无任务，按钮变为开始按钮
    public static final int ping_pop_operator_dialog=90010;//弹出模态窗口，处理任务结果

    public static final int ping_pop_loading_dialog=90011;//弹出加载中窗口
    public static final int ping_close_loading_dialog=90012;//关闭加载窗口并可带提示TIP
    public static final int ping_set_float_btn_visible=90013;//隐藏漂浮的结果处理按钮
    public static final int ping_set_form_pause=90014;//任务被暂停


}
