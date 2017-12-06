package com.zhiyou.colleageapp.appui.person;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.listener.IRegistEventBus;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterContacts;
import com.zhiyou.colleageapp.appui.mvpView.ViewSuccess;
import com.zhiyou.colleageapp.appui.person.Fragment.LoveFragment;
import com.zhiyou.colleageapp.appui.person.Fragment.PersonalInfoFragment;
import com.zhiyou.colleageapp.appui.person.Fragment.SettingFragment;
import com.zhiyou.colleageapp.appui.person.adapter.MineAdapter;
import com.zhiyou.colleageapp.appui.widget.CircleImageView;
import com.zhiyou.colleageapp.constants.FragmentTag;
import com.zhiyou.colleageapp.domain.PersonalProfile;
import com.zhiyou.colleageapp.eenum.Gender;
import com.zhiyou.colleageapp.manager.PreferenceManager;
import com.zhiyou.colleageapp.rxeventbus.RxEventBus;
import com.zhiyou.colleageapp.rxeventbus.event.EventBase;
import com.zhiyou.colleageapp.rxeventbus.event.EventPersonalInfoChanged;
import com.zhiyou.colleageapp.rxeventbus.event.EventUserLoginSuccess;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by wujiaolong on 16/5/13.
 *
 */
public class MineFragment extends BaseFragment implements IRegistEventBus {
    private ListView listView;
    private CircleImageView currentUserAvar;
    private ImageView mGenderImg;
    private TextView currentUserTv, mBelong;
    private PresenterContacts mPresenterUser;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_person_layout, container, false);
    }
    @Override
    protected void showUnLoginCover() {
    }
    @Override
    public void onResume() {
        super.onResume();
        hiddenAppMainTabLoading();
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        listView = (ListView) view.findViewById(R.id.person_listview);
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_person_header, listView, false);
        currentUserTv = (TextView) headerView.findViewById(R.id.txt_name_header_person);
        currentUserAvar = (CircleImageView) headerView.findViewById(R.id.img_header_person);
        mBelong = (TextView) headerView.findViewById(R.id.mine_header_belong);
        mGenderImg = (ImageView) headerView.findViewById(R.id.mine_gender);
        listView.addHeaderView(headerView);
        MineAdapter adapter = new MineAdapter(getContext(), getData());
        listView.setAdapter(adapter);
        refresh();
    }

    protected void refresh() {
        String url = PreferenceManager.getInstance().getCurrentUserAvatar();
        if (!TextUtils.isEmpty(url)) {
            Glide.with(mBaseActivity).load(url).placeholder(R.mipmap.def_avatar).into(currentUserAvar);
            mBelong.setText(PreferenceManager.getInstance().getBelong());
            currentUserTv.setText(EMClient.getInstance().getCurrentUser());
        } else {
            if (mPresenterUser == null) {
                mPresenterUser = new PresenterContacts();
            }
            mPresenterUser.getContactProfile("", new ViewSuccess<PersonalProfile>() {
                @Override
                public void onSuccess(PersonalProfile profile) {
                    hiddenLoading();
                    Glide.with(MineFragment.this).load(profile.getAvatar()).placeholder(R.mipmap.def_avatar).into(currentUserAvar);
                    mBelong.setText(profile.getBelong());
                    currentUserTv.setText(profile.getUsername());
                }
            });
        }
    }


    List<MineItem> getData() {
        List<MineItem> mineList = new ArrayList<>();
        int[] imgRes = {
                R.drawable.mine_dynamic,
                R.drawable.mine_confession,
                R.drawable.mine_task,
                R.drawable.mine_schoole_coin,
                R.drawable.mine_score,
                R.drawable.mine_trade_record,
                R.drawable.mine_collect,
                R.drawable.mine_setting};

        String[] settingStrings = getResources().getStringArray(R.array.person_item_title);
        for (int i = 0; i < settingStrings.length; i++) {
            MineItem item = new MineItem(i, imgRes[i], settingStrings[i]);
            mineList.add(item);
        }
        return mineList;
    }

    @Override
    protected void setListener() {
        super.setListener();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: By LongWeiHu 2016/5/20
                switch (position) {
                    case 0:
                        mBaseActivity.showFragment(PersonalInfoFragment.class, FragmentTag.MINE_2_INFO, null, true);
                        break;
                    case 1:
                        break;
                    case 2:
                        mBaseActivity.showFragment(LoveFragment.class, FragmentTag.MINE_2_LOVE, null, true);
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        mBaseActivity.showFragment(SettingFragment.class, FragmentTag.MINE_2_SETTING, null, true);
                        break;
                }
            }
        });
    }

    private void setText(TextView view, String text) {
        if (view != null) {
            view.setText(text);
        }
    }

    @Override
    public void registerEventBus() {
        Subscription subscription = RxEventBus.getDefault().onReceiveEvent().map(new Func1<Object, EventBase>() {
            @Override
            public EventBase call(Object obj) {
                return (EventBase) obj;
            }
        }).subscribe(new Action1<EventBase>() {
            @Override
            public void call(EventBase event) {
                if (event instanceof EventPersonalInfoChanged) {
                    PersonalProfile personalProfile = ((EventPersonalInfoChanged) event).getProfile();
                    if (personalProfile != null) {
                        mBelong.setText(personalProfile.getBelong());
                        Gender gender = personalProfile.getGender();
                        mGenderImg.setImageResource(gender == Gender.Male ? R.drawable.male : R.drawable.female);
                        Glide.with(mBaseActivity).load(personalProfile.getAvatar()).placeholder(R.mipmap.def_avatar).into(currentUserAvar);
                    }
                } else if (event instanceof EventUserLoginSuccess) {
                    refresh();
                }

            }
        });
        addSubscription(subscription);
    }
}
