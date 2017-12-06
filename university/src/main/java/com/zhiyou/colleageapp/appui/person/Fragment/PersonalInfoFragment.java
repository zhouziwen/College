package com.zhiyou.colleageapp.appui.person.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.adapter.UniversalClickableAdapter;
import com.zhiyou.colleageapp.appui.adapter.listitem.PopItem;
import com.zhiyou.colleageapp.appui.adapter.viewholder.UniversalViewHolder;
import com.zhiyou.colleageapp.appui.listener.IEditPersonalInfo;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterContacts;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterUpload;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterUser;
import com.zhiyou.colleageapp.appui.mvpView.ViewSuccess;
import com.zhiyou.colleageapp.appui.person.EditPersonalInfoFragment;
import com.zhiyou.colleageapp.appui.person.MineItem;
import com.zhiyou.colleageapp.appui.widget.CircleImageView;
import com.zhiyou.colleageapp.appui.widget.PopWindowView;
import com.zhiyou.colleageapp.constants.EaseConstant;
import com.zhiyou.colleageapp.constants.FragmentTag;
import com.zhiyou.colleageapp.constants.HttpKey;
import com.zhiyou.colleageapp.constants.MineInfoKey;
import com.zhiyou.colleageapp.domain.PersonalProfile;
import com.zhiyou.colleageapp.eenum.Gender;
import com.zhiyou.colleageapp.manager.PreferenceManager;
import com.zhiyou.colleageapp.rxeventbus.RxEventBus;
import com.zhiyou.colleageapp.rxeventbus.event.EventPersonalInfoChanged;
import com.zhiyou.colleageapp.utils.AppToast;
import com.zhiyou.colleageapp.utils.CameraHandler;
import com.zhiyou.colleageapp.utils.CommonPath;
import com.zhiyou.colleageapp.utils.DialogUtils;
import com.zhiyou.colleageapp.utils.DisplayUtils;
import com.zhiyou.colleageapp.utils.EaseImageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by chuyh on 16/5/13.
 * 用户详情界面
 */
public class PersonalInfoFragment extends BaseFragment implements IEditPersonalInfo {

    private PullToRefreshListView mListView;
    private PersonalAdapter mAdapter;
    private String mUserName;
    private PresenterContacts mPresenterContacts;
    private PresenterUser mPresenterUser;
    private PopWindowView mPopGender;
    private PresenterUpload mPresenterUpload;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserName = mBundle.getString(EaseConstant.CHAT_ENTITY_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_title_listview_nodiver, container, false);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mListView = (PullToRefreshListView) view.findViewById(R.id.app_list_view);
        mAdapter = new PersonalAdapter(getContext(), R.layout.mine_item_person_info, getLabelList());
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mPresenterContacts = new PresenterContacts(new MyViewBase());
        mPresenterUser = new PresenterUser(new MyViewBase());
        getPersonalProfile(null);
    }

    private void getPersonalProfile(final PullToRefreshBase<ListView> refreshView) {
        mPresenterContacts.getContactProfile(mUserName, new ViewSuccess<PersonalProfile>() {
            @Override
            public void onSuccess(PersonalProfile personalProfile) {
                hiddenLoading();
                if (refreshView != null) {
                    refreshView.onRefreshComplete();
                }
                if (personalProfile == null) {
                    return;
                }
                mAdapter.setPersonInfo(personalProfile);
                PreferenceManager.getInstance().saveBelong(personalProfile.getBelong());
                PreferenceManager.getInstance().setCurrentUserAvatar(personalProfile.getAvatar());
            }
        });
    }

    @Override
    protected void setListener() {
        super.setListener();
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getPersonalProfile(refreshView);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case CameraHandler.REQUEST_CODE_CAMERA:
                File imgFile = new File(CameraHandler.mCameraPicPath);
                updateFile(imgFile);
                break;
            case CameraHandler.REQUEST_CODE_PICK:
                if (data == null || data.getData() == null) {
                    return;
                }
                CameraHandler.startPhotoZoom(data.getData(), PersonalInfoFragment.this);
                break;
            case CameraHandler.REQUEST_CODE_CUTTING:
                if (data == null) {
                    AppToast.showBottom("获取图片数据失败");
                    return;
                }
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    File file = EaseImageUtils.saveBitmapToFile(CommonPath.getDirUserTemp() + "userAvatar.png", photo, Bitmap.CompressFormat.PNG);
                    updateFile(file);
                }
                break;
        }
    }

    private void updateFile(File imgFile) {
        if (mPresenterUpload != null) {
            mPresenterUpload.uploadFile(imgFile, new ViewSuccess<List<String>>() {
                @Override
                public void onSuccess(List<String> strings) {
                    hiddenLoading();
                    if (strings == null || strings.isEmpty()) {
                        return;
                    }
                    Map<String, String> data = new HashMap<String, String>();
                    data.put(HttpKey.HEADER_URL, strings.get(0));
                    mPresenterUser.modifyUserInfo(data, new ViewSuccess<PersonalProfile>() {
                        @Override
                        public void onSuccess(PersonalProfile profile) {
                            PreferenceManager.getInstance().setCurrentUserAvatar(profile.getAvatar());
                            onModifySuccess(profile);
                        }
                    });
                }
            });
        }
    }


    private List<MineItem> getLabelList() {
        List<MineItem> labelList = new ArrayList<>();
        String[] settingStrings = getResources().getStringArray(R.array.mine_item_info);
        for (int i = 0; i < settingStrings.length; i++) {
            MineItem item = new MineItem(MineInfoKey.AVATAR + i, 0, settingStrings[i]);
            labelList.add(item);
        }
        return labelList;
    }

    @Override
    public void save(Integer editWitch, String newContent, Boolean isChange) {
        if (!isChange) {
            return;
        }
        Map<String, String> beChangedInfo = new HashMap<>();
        switch (editWitch) {
            case MineInfoKey.NICK:
                beChangedInfo.put(HttpKey.NICK_NAME, newContent);
                break;
            case MineInfoKey.HOME_TOWN:
                beChangedInfo.put(HttpKey.HOME_TOWN, newContent);
                break;
            case MineInfoKey.BELONG:
                beChangedInfo.put(HttpKey.BELONG, newContent);
                break;
            case MineInfoKey.DEPT:
                beChangedInfo.put(HttpKey.DEPT, newContent);
                break;
            case MineInfoKey.MAJOR:
                beChangedInfo.put(HttpKey.MAJOR, newContent);
                break;
            case MineInfoKey.STU_NO:
                beChangedInfo.put(HttpKey.STUDENT_NO, newContent);
                break;
        }

        mPresenterUser.modifyUserInfo(beChangedInfo, new ViewSuccess<PersonalProfile>() {
            @Override
            public void onSuccess(PersonalProfile personalProfile) {
                onModifySuccess(personalProfile);
            }
        });
    }

    private void onModifySuccess(PersonalProfile profile) {
        mAdapter.setPersonInfo(profile);
        RxEventBus.getDefault().postEvent(new EventPersonalInfoChanged(profile));
        hiddenLoading();
    }

    private DialogUtils mDialogUtils;

    private class PersonalAdapter extends UniversalClickableAdapter<MineItem> {
        private PersonalProfile mProfile = new PersonalProfile();

        public PersonalAdapter(Context context, int layoutId, List<MineItem> dataList) {
            super(context, layoutId, dataList);
            mDialogUtils = new DialogUtils();
        }

        @Override
        public void setViewListener(UniversalViewHolder holder) {
            holder.setClickListener(holder.getConvertView());
        }

        @Override
        public void onClickBack(View v, int position, UniversalViewHolder holder) {
            notifyDataSetChanged();
            MineItem item = getItem(position);
            int which = -1;
            String oldContent = "";

            switch (item.getId()) {
                case MineInfoKey.AVATAR:
                    if (mPresenterUpload == null) {
                        mPresenterUpload = new PresenterUpload(new MyViewBase());
                    }
                    mDialogUtils.showGetPicDialog(PersonalInfoFragment.this);
                    break;
                case MineInfoKey.NICK:
                    which = MineInfoKey.NICK;
                    oldContent = mProfile.getNick();
                    break;
                case MineInfoKey.TWO_DIMENSION:
//                    QRCodeUtil.createQRImage()
                    break;
                case MineInfoKey.GENDER:
                    choseGender(holder.getView(R.id.mine_content));
                    break;
                case MineInfoKey.HOME_TOWN:
                    which = MineInfoKey.HOME_TOWN;
                    oldContent = mProfile.getHomeTown();
                    break;
                case MineInfoKey.BELONG: //院校
                    which = MineInfoKey.BELONG;
                    oldContent = mProfile.getBelong();
                    break;
                case MineInfoKey.DEPT: //院系
                    which = MineInfoKey.DEPT;
                    oldContent = mProfile.getDept();
                    break;
                case MineInfoKey.MAJOR:
                    which = MineInfoKey.MAJOR;
                    oldContent = mProfile.getMajor();
                    break;
                case MineInfoKey.STU_NO:
                    which = MineInfoKey.STU_NO;
                    oldContent = mProfile.getStuNO();
                    break;
            }
            if (which != -1) {
                Bundle bundle = new Bundle();
                bundle.putString(EaseConstant.FRAGMENT_TAG, getTag());
                bundle.putInt(EaseConstant.EDIT_WHICH, which);
                bundle.putString(EaseConstant.EDIT_OLD_CONTENT, oldContent);
                mBaseActivity.showFragment(EditPersonalInfoFragment.class, FragmentTag.EDIT_PERSONAL_INFO, bundle, true);
            }
        }

        @Override
        public void onLongClickBack(View v, int position, UniversalViewHolder holder) {

        }

        private void setPersonInfo(PersonalProfile profile) {
            mProfile = profile;
            if (mProfile == null) {
                mProfile = new PersonalProfile();
            }
            notifyDataSetChanged();
        }

        @Override
        public void convert(UniversalViewHolder holder, MineItem item) {
            CircleImageView avatar = holder.getView(R.id.mine_avatar);
            ImageView towDimension = holder.getView(R.id.mine_two_dimension_code);
            TextView keyName = holder.getView(R.id.mine_name);
            TextView value = holder.getView(R.id.mine_content);
            value.setMaxWidth(DisplayUtils.getWidthPx() / 4 * 3);
            View divider = holder.getView(R.id.mine_top_line);
            View space = holder.getView(R.id.mine_space);
            keyName.setText(item.getName());
            String content = "";
            switch (item.getId()) {
                case MineInfoKey.AVATAR:
                    avatar.setVisibility(View.VISIBLE);
                    String url = mProfile.getAvatar();
                    if (url == null) {
                        url = PreferenceManager.getInstance().getCurrentUserAvatar();
                    }
                    Glide.with(PersonalInfoFragment.this).load(url).placeholder(R.mipmap.def_avatar).crossFade().into(avatar);
                    divider.setVisibility(View.GONE);
                    space.setVisibility(View.VISIBLE);
                    content = "";
                    break;
                case MineInfoKey.NICK:
                    content = mProfile.getNick();
                    avatar.setVisibility(View.GONE);
                    break;
                case MineInfoKey.ACCOUNT:
                    content = mProfile.getUsername();//目前的账号就是用户名
                    avatar.setVisibility(View.GONE);
                    break;
                case MineInfoKey.TWO_DIMENSION:
                    towDimension.setVisibility(View.VISIBLE);
                    avatar.setVisibility(View.GONE);
                    content = "";
                    break;
                case MineInfoKey.GENDER:
                    divider.setVisibility(View.GONE);
                    space.setVisibility(View.VISIBLE);
                    content = mProfile.getGender() == Gender.Male ? "男" : "女";
                    avatar.setVisibility(View.GONE);
                    break;
                case MineInfoKey.HOME_TOWN:
                    content = mProfile.getHomeTown();
                    avatar.setVisibility(View.GONE);
                    break;
                case MineInfoKey.BELONG: //院校
                    divider.setVisibility(View.GONE);
                    space.setVisibility(View.VISIBLE);
                    content = mProfile.getBelong();
                    avatar.setVisibility(View.GONE);
                    break;
                case MineInfoKey.DEPT: //院系
                    content = mProfile.getDept();
                    avatar.setVisibility(View.GONE);
                    break;
                case MineInfoKey.MAJOR:
                    content = mProfile.getMajor();
                    avatar.setVisibility(View.GONE);
                    break;
                case MineInfoKey.STU_NO:
                    content = mProfile.getStuNO();
                    avatar.setVisibility(View.GONE);
                    break;
            }
            value.setText(content);
        }

        /**
         * @param view
         */
        private void choseGender(View view) {
            if (mPopGender == null) {
                mPopGender = new PopWindowView(getContext(), DisplayUtils.getWidthPx() / 3);
                List<PopItem> list = new ArrayList<>();
                list.add(new PopItem(R.string.male));
                list.add(new PopItem(R.string.female));
                mPopGender.setData(list);
                mPopGender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        PopItem item = (PopItem) parent.getAdapter().getItem(position);
                        Map<String, String> genderMap = new HashMap<>();
                        switch (item.getContentTextId()) {
                            case R.string.male:
                                genderMap.put(HttpKey.SEX, String.format(Locale.getDefault(), "%s", 1));
                                break;
                            case R.string.female:
                                genderMap.put(HttpKey.SEX, String.format(Locale.getDefault(), "%s", 0));
                                break;
                        }
                        mPresenterUser.modifyUserInfo(genderMap, new ViewSuccess<PersonalProfile>() {
                            @Override
                            public void onSuccess(PersonalProfile personalProfile) {
                                onModifySuccess(personalProfile);
                            }
                        });
                        mPopGender.dismiss();
                    }
                });
            }

            mPopGender.show(view);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenterContacts.releaseAll();
        if (mDialogUtils != null) {
            mDialogUtils.release();
        }
    }
}
