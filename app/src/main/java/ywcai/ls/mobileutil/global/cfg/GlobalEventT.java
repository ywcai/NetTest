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
    public static final int ping_set_float_btn_visible = 90013;//隐藏漂浮的结果处理按钮
    public static final int ping_set_form_pause = 90014;//任务被暂停


    public static final int result_update_list = 90016;//更新结果记录列表
    public static final int result_update_tag_status = 90015;//初始化时恢复Tag的选择状态
    public static final int result_update_top_btn_status = 91000;//更新顶部全选按钮状态
    public static final int result_toast_tip=91001;
    public static final int result_remote_item_head=91003;//显示remote的item首页

    public static final int wifi_set_main_title_tip = 90017;
    public static final int wifi_refresh_first_info = 90018;//更新首页的结果记录列表
    public static final int wifi_set_lock_btn_status = 90019;//是否锁定信号
    public static final int wifi_set_lock_save_btn_visible = 90020;//是否可以显示锁定图标
    public static final int wifi_set_task_btn_status = 90021;//是否监测该信号强度变换。
    public static final int wifi_refresh_save_tags = 90023;//刷新需要保存记录的标签
    public static final int wifi_set_channel_btn_status = 90024;//选择2.4G还是5G
    public static final int wifi_recovery_channel_select_2d4g = 90025;
    public static final int wifi_recovery_channel_select_5g = 90026;
    public static final int wifi_set_select_entry_info = 90027;//主界面的容器顶部被选择信号的详细信息
    public static final int wifi_refresh_two_list = 90028;

    public static final int wifi_refresh_two_chart_line = 90030;
    public static final int wifi_set_item_btn_hide_status = 90031;
    public static final int wifi_notify_top_notification = 90032;
    public static final int wifi_refresh_three_bar = 90033;
    public static final int wifi_refresh_three_pie = 90034;
    public static final int wifi_refresh_three_line = 90035;
    public static final int wifi_switch_2d4g = 90036;
    public static final int wifi_refresh_channel_level = 90037;
    public static final int wifi_refresh_frequency_level = 90038;
    public static final int wifi_set_receive_flag = 90039;


    public static final int station_set_entry_change = 90050;
    public static final int station_set_cell_change = 90051;
    public static final int station_set_cell_change1 = 90052;
    public static final int station_refresh_entry_info = 90053;
    public static final int station_set_toolbar_center_text = 90054;
    public static final int station_refresh_cell_log_info = 90055;
    public static final int station_refresh_signal_log_info = 90056;
    public static final int station_refresh_chart_entry_record = 90057;
    public static final int station_switch_top_btn = 90060;


    public static final int scan_lan_ping_index_result = 90090;
    public static final int scan_lan_refresh_local_ip = 90091;
    public static final int scan_lan_ping_stop = 90092;

    public static final int scan_port_set_card_run_info_run = 90101;
    public static final int scan_port_set_card_run_info_none = 90102;
    public static final int scan_port_set_card_run_info_end = 90103;
    public static final int scan_port_set_card_run_info_new = 90104;
    public static final int scan_port_add_radar_result = 90105;
    public static final int scan_port_refresh_radar_progress = 90106;
    public static final int scan_port_recovery_radar_data = 90107;


    public static final int sensor_set_snack_tip = 90200;
    public static final int sensor_recovery_tag_state = 90201;
    public static final int sensor_set_tags = 90202;
    public static final int sensor_set_card_info = 90203;

    public static final int speed_test_info = 90302;
    public static final int speed_yibiao_read_data = 90303;
    public static final int speed_set_progress = 90304;
    public static final int speed_set_complete = 90305;
    public static final int speed_set_running_receive_task = 90306;
    public static final int speed_set_ready = 90308;

    public static final int orientation_refresh = 90400;

    public static final int global_pop_operator_dialog = 90501;
    public static final int global_pop_loading_dialog = 90502;
    public static final int global_pop_snack_tip = 90503;


    public static final int detail_local_refresh_record = 90600;
    public static final int result_local_clear_record = 90601;
    public static final int detail_remote_refresh_record=90602;
    public static final int detail_remote_refresh_aliasname=90603;

    public static final int setting_qq_login_success = 90700;
    public static final int setting_dialog_close=90701;
    public static final int setting_pop_toast_tip = 90702;



}
