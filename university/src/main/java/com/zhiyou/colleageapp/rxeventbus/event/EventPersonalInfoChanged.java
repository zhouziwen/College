package com.zhiyou.colleageapp.rxeventbus.event;

import com.zhiyou.colleageapp.domain.PersonalProfile;

/**
 * Create by LongWeiHu on 2016/6/20.
 */
public class EventPersonalInfoChanged extends EventBase{
    public EventPersonalInfoChanged(PersonalProfile profile) {
        mProfile = profile;
    }

    private PersonalProfile mProfile;

    public PersonalProfile getProfile() {
        return mProfile;
    }

    public void setProfile(PersonalProfile profile) {
        mProfile = profile;
    }
}
