package ywcai.ls.mobileutil.setting;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import me.drakeet.materialdialog.MaterialDialog;
import ywcai.ls.control.LoadingDialog;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.GlobalEvent;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.login.model.MyUser;


public class SettingFragment extends Fragment {
    private TextView login, out;
    private View view, view2;
    private SetActionInf setActionInf;
    private int clickTemp = 0;
    private final int EDIT_NICKNAME = 1,
            DEL_RECORD = 2,
            UPDATE_RECORD = 3,
            POP_HELP = 4,
            POP_CONTRACT = 5,
            LOGIN_OUT = 6,
            DEL_REMOTE = 7,
            DOWNLOAD_RECORD = 8;
    private LoadingDialog loadingDialog;
    private CacheProcess cacheProcess = CacheProcess.getInstance();
    private MaterialDialog editDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_main_setting, container, false);
        loadingDialog = new LoadingDialog(getActivity());
        //安装网格化选择菜单
        login = (TextView) view.findViewById(R.id.setting_login_in);
        out = (TextView) view.findViewById(R.id.setting_login_out);
        initAction();
        initView();
        return view;
    }

    private void initView() {
        view2 = LayoutInflater.from(this.getContext()).inflate(R.layout.pop_dialog_detail_local_input, null);
        editDialog = new MaterialDialog(this.getContext());
        editDialog.setContentView(view2);
        final MaterialEditText inputText = (MaterialEditText) view2.findViewById(R.id.detail_local_edit_input_text);
        editDialog.setTitle("修改昵称");
        editDialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.dismiss();
            }
        });
        editDialog.setPositiveButton("修改", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputText.length() >= inputText.getMinCharacters() && inputText.length() <= inputText.getMaxCharacters()) {
                    editDialog.dismiss();
                    MyUser myUser = cacheProcess.getCacheUser();
                    setActionInf.editNickName(myUser, inputText.getEditableText().toString());
                    loadingDialog.show();
                }
            }
        });
    }


    private void popInputMask() {
        editDialog.show();
    }

    private void initAction() {
        TextView edit_nickname = (TextView) view.findViewById(R.id.setting_edit_nickname_tx);
        TextView del = (TextView) view.findViewById(R.id.setting_del_record);
        TextView del_remote = (TextView) view.findViewById(R.id.setting_del_record_remote);
        del_remote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cacheProcess.getCacheUser() == null) {
                    popMaskTip("还未登录!");
                    return;
                }
                click(DEL_REMOTE);
            }
        });
        TextView update = (TextView) view.findViewById(R.id.setting_update_record);
        TextView download = (TextView) view.findViewById(R.id.setting_download_record);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cacheProcess.getCacheUser() != null) {
                    click(DOWNLOAD_RECORD);
                } else {
                    popMaskTip("还未登录!");
                }
            }
        });

        TextView pop_help = (TextView) view.findViewById(R.id.setting_pop_help);
        TextView pop_contract = (TextView) view.findViewById(R.id.setting_pop_contract);
        setActionInf = new SetAction();
        edit_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cacheProcess.getCacheUser() != null) {
                    click(EDIT_NICKNAME);
                } else {
                    popMaskTip("还未登录!");
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(AppConfig.LOGIN_ACTIVITY_PATH).navigation();
            }
        });
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退出登录.
                click(LOGIN_OUT);
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cacheProcess.getCacheLogIndex().size() <= 0) {
                    popMaskTip("已经无本地记录");
                    return;
                }
                click(DEL_RECORD);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cacheProcess.getCacheUser() == null) {
                    popMaskTip("请先登录");
                    return;
                }
                if (cacheProcess.getCacheLogIndex().size() <= 0) {
                    popMaskTip("已经无本地记录");
                    return;
                }
                click(UPDATE_RECORD);
            }
        });
        pop_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(POP_HELP);
            }
        });
        pop_contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(POP_CONTRACT);
            }
        });
    }

    private void click(int index) {
        clickTemp = index;
        switch (index) {
            case EDIT_NICKNAME:
                popInputMask();
                break;
            case DEL_RECORD:
                popMask("确认要删除所有本地记录？");
                break;
            case UPDATE_RECORD:
                popMask("上传成功后，本地记录将被清除。\n继续上传吗？");
                break;
            case POP_HELP:
                setActionInf.popHelpMsg();
                break;
            case POP_CONTRACT:
                setActionInf.popContract();
                break;
            case LOGIN_OUT:
                popMask("确定要退出当前账号?");
                break;
            case DEL_REMOTE:
                popMask("确定删除所有云端数据?");
                break;
            case DOWNLOAD_RECORD:
                popMask("下载到本地后云端数据将被清除\n确定下载到本地?");
                break;
        }
    }

    private void popMask(String tip) {
        final MaterialDialog dialog = new MaterialDialog(this.getActivity());
        dialog.setTitle(null);
        dialog.setMessage(tip);
        dialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                CacheProcess cacheProcess = CacheProcess.getInstance();
                MyUser myUser = cacheProcess.getCacheUser();
                switch (clickTemp) {
                    case UPDATE_RECORD:
                        if (myUser != null) {
                            loadingDialog.show();
                            setActionInf.updateRecord(myUser);
                        }
                        break;
                    case DEL_RECORD:
                        loadingDialog.show();
                        setActionInf.clearRecord();
                        break;
                    case LOGIN_OUT:
                        cacheProcess.setCacheUser(null);
                        updateUserInfo(null);
                        break;
                    case DEL_REMOTE:
                        if (myUser != null) {
                            loadingDialog.show();
                            setActionInf.delRemote(myUser.userid);
                        }
                        break;
                    case DOWNLOAD_RECORD:
                        if (myUser != null) {
                            loadingDialog.show();
                            setActionInf.downLoad(myUser.userid);
                        }
                        break;
                }
            }
        });
        dialog.show();
    }

    private void popMaskTip(String tip) {
        final MaterialDialog mask = new MaterialDialog(this.getActivity());
        mask.setCanceledOnTouchOutside(true);
        mask.setTitle(null);
        mask.setMessage(tip);
        mask.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mask.dismiss();
            }
        });
        mask.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onResume() {
        MyUser myUser = cacheProcess.getCacheUser();
        updateUserInfo(myUser);
        super.onResume();
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void updateDeviceList(GlobalEvent event) {
        switch (event.type) {
            case GlobalEventT.setting_qq_login_success:
                MyUser myUser = (MyUser) event.obj;
                updateUserInfo(myUser);
                break;
            case GlobalEventT.setting_dialog_close:
                loadingDialog.dismiss();
                break;
            case GlobalEventT.setting_pop_toast_tip:
                showToast(event.tip);
                break;

        }
    }

    private void showToast(String tip) {
        Toast.makeText(this.getContext(), tip, Toast.LENGTH_SHORT).show();
    }

    private void updateUserInfo(MyUser myUser) {
        RelativeLayout rl_login = (RelativeLayout) view.findViewById(R.id.setting_login_active);
        RelativeLayout rl_out = (RelativeLayout) view.findViewById(R.id.setting_un_login);
        TextView nickname = (TextView) view.findViewById(R.id.setting_nickname);
        TextView date = (TextView) view.findViewById(R.id.setting_create_date);
        RelativeLayout edit_nickname = (RelativeLayout) view.findViewById(R.id.setting_edit_nickname);
        final MaterialEditText inputText = (MaterialEditText) view2.findViewById(R.id.detail_local_edit_input_text);
        if (myUser != null) {
            rl_login.setVisibility(View.VISIBLE);
            rl_out.setVisibility(View.GONE);
            nickname.setText(myUser.nickname);
            inputText.setText(myUser.nickname);
            date.setText(myUser.createdate);
            edit_nickname.setVisibility(View.VISIBLE);
        } else {
            inputText.setText("");
            rl_login.setVisibility(View.GONE);
            rl_out.setVisibility(View.VISIBLE);
            nickname.setText("");
            date.setText("");
            edit_nickname.setVisibility(View.GONE);
        }
    }
}
