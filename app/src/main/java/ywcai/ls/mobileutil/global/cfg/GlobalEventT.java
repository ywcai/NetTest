package ywcai.ls.mobileutil.global.cfg;

public class GlobalEventT {
    public static final int un_login = 80001;
    public static final int is_login = 80002;
    public static final int is_Run = 80003;


    public static final int test_tip = 99999;

    //Ping包返回标识
    public static final int ping_update_chart_point = 90001;//更新当前序列坐标Y值
    public static final int ping_update_chart_desc = 90002;//更新图片描述


    //Ping包读取缓存更新UI
    public static final int ping_set_bar_size_package = 90003;//更新包大小进度条
    public static final int ping_set_bar_size_thread = 90004;//更新线程大小进度条
    public static final int ping_set_input_text_ip = 90005;//更新线程大小进度条
    public static final int ping_set_chart_data_size = 90006;//初始数据宽度，与包大小匹配
    public static final int ping_repair_chart_line = 90007;//补全丢失曲线

    public static final int ping_set_form_free = 90008;//操作表单禁用，正在运行任务，按钮变为停止按钮
    public static final int ping_set_form_busy = 90009;//操作表单启用，无任务，按钮变为开始按钮
    public static final int ping_pop_operator_dialog = 90010;//弹出模态窗口，处理任务结果

    public static final int ping_pop_loading_dialog = 90011;//弹出加载中窗口
    public static final int ping_close_loading_dialog = 90012;//关闭加载窗口并可带提示TIP
    public static final int ping_set_float_btn_visible = 90013;//隐藏漂浮的结果处理按钮
    public static final int ping_set_form_pause = 90014;//任务被暂停

//    public static final int menu_synchronise_cache=90015;

    public static final int result_update_list = 90016;//更新结果记录列表
    public static final int result_update_tag_status = 90015;//初始化时恢复Tag的选择状态
    public static final int result_update_top_btn_status = 91000;//更新顶部全选按钮状态

    public static final int wifi_set_main_title_tip = 90017;
    public static final int wifi_refresh_first_info = 90018;//更新首页的结果记录列表
    public static final int wifi_set_lock_btn_status = 90019;//是否锁定信号
    public static final int wifi_set_lock_save_btn_visible = 90020;//是否可以显示锁定图标
    public static final int wifi_set_task_btn_status = 90021;//是否监测该信号强度变换。
    public static final int wifi_set_first_main_toast = 90022;//主界面的容器底部描述
    public static final int wifi_refresh_save_tags = 90023;//刷新需要保存记录的标签
    public static final int wifi_set_channel_btn_status = 90024;//选择2.4G还是5G
    public static final int wifi_recovery_channel_select_2d4g = 90025;
    public static final int wifi_recovery_channel_select_5g = 90026;
    public static final int wifi_set_select_entry_info = 90027;//主界面的容器顶部被选择信号的详细信息
    public static final int wifi_refresh_two_list = 90028;
    public static final int wifi_pop_menu = 90029;
    public static final int wifi_refresh_two_chart_line = 90030;
    public static final int wifi_set_item_btn_hide_status = 90031;
    public static final int wifi_notify_top_notification = 90032;
    public static final int wifi_refresh_three_bar = 90033;
    public static final int wifi_refresh_three_pie = 90034;
    public static final int wifi_refresh_three_line = 90035;
    public static final int wifi_switch_2d4g = 90036;
    public static final int wifi_refresh_channel_level = 90037;
    public static final int wifi_refresh_frequency_level = 90038;
    public static final int wifi_main_bottom_tip = 90039;

    public static final int station_set_entry_change = 90050;
    public static final int station_set_cell_change = 90051;
    public static final int station_set_cell_change1 = 90052;
    public static final int station_refresh_entry_info = 90053;
    public static final int station_set_toolbar_center_text = 90054;
    public static final int station_refresh_cell_log_info = 90055;
    public static final int station_refresh_signal_log_info = 90056;
    public static final int station_refresh_chart_entry_record = 90057;
    public static final int station_pop_dialog = 90058;
    public static final int station_bottom_snack_tip = 90059;
    public static final int station_switch_top_btn = 90060;
}
