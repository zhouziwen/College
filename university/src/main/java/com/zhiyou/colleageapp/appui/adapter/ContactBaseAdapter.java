package com.zhiyou.colleageapp.appui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SectionIndexer;

import com.bumptech.glide.Glide;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.adapter.viewholder.UniversalViewHolder;
import com.zhiyou.colleageapp.appui.widget.CircleImageView;
import com.zhiyou.colleageapp.appui.widget.listview.StickyListHeadersAdapter;
import com.zhiyou.colleageapp.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Create by LongWeiHu on 2016/6/1.
 * 显示联系人的 baseAdapter
 */
public class ContactBaseAdapter extends UniversalSelectAdapter<User> implements SectionIndexer,StickyListHeadersAdapter {
    protected Character[] mSectionLetters;
    protected int[] mSectionIndices;

    public ContactBaseAdapter(Context context, int layoutId, List<User> dataList) {
        super(context, layoutId, dataList);
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
    }

    public ContactBaseAdapter(Context context, int layoutId) {
        this(context,layoutId,null);
    }


    @Override
    public void onCheckedChangedCallback(CompoundButton v, boolean isChecked, int position) {

    }


    @Override
    public void setViewListener(UniversalViewHolder holder) {
        holder.setVisible(R.id.list_item_contact_check, false);
    }

    @Override
    public void onClickBack(View v, int position,UniversalViewHolder holder) {

    }

    @Override
    public void onLongClickBack(View v, int position,UniversalViewHolder holder) {

    }

    @Override
    public void convert(UniversalViewHolder holder, User user) {
        Glide.with(mContext).load(user.getAvatar()).placeholder(R.drawable.user_default_avatar).into((CircleImageView) holder.getView(R.id.list_item_contact_avatar));
        holder.setText(R.id.list_item_contact_name, user.getUsername());
    }


    public List<String> getSelectUserNameList() {
        List<User> selectUsers = getSelectedUser();
        if (selectUsers == null || selectUsers.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> nameList = new ArrayList<>();
        for (User user : selectUsers
                ) {
            nameList.add(user.getUsername());
        }

        return nameList;
    }


    @Override
    public Object[] getSections() {
         return mSectionLetters;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        if (mSectionIndices.length == 0) {
            return 0;
        }

        if (sectionIndex >= mSectionIndices.length) {
            sectionIndex = mSectionIndices.length - 1;
        } else if (sectionIndex < 0) {
            sectionIndex = 0;
        }
        return mSectionIndices[sectionIndex];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }


    /**
     * 得到所有名字首字母的分类索引：26个英文字母中的那几个的索引
     *
     * @return
     */
    private int[] getSectionIndices() {
        if (mDataList.size() == 0) {
            return new int[0];
        }
        ArrayList<Integer> sectionIndices = new ArrayList<>();
        char lastFirstChar = mDataList.get(0).getInitialLetter().charAt(0);
        sectionIndices.add(0);
        int len = mDataList.size();
        for (int i = 1; i < len; i++) {
            char pin = mDataList.get(i).getInitialLetter().charAt(0);
            if (pin != lastFirstChar) {
                lastFirstChar = pin;
                sectionIndices.add(i);
            }
        }
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        return sections;
    }

    /**
     * 得到mList中每个不同名字 的拼音的第一个字母的数组
     *
     * @return
     */
    private Character[] getSectionLetters() {
        Character[] letters = new Character[mSectionIndices.length];
        for (int i = 0; i < mSectionIndices.length; i++) {
            letters[i] = mDataList.get(i).getInitialLetter().charAt(0);

        }
        return letters;
    }

    /**
     * @param c :
     * @return :
     */
    public int getPositionForPinyin(char c) {
        for (int i = 0; i < getCount(); i++) {
            char sort = mDataList.get(i).getInitialLetter().charAt(0);
            if (sort == c) {
                return i;
            }
        }
        return -1;
    }


    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        UniversalViewHolder headerHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_sticky_header, parent, false);
            headerHolder = new UniversalViewHolder(convertView,null,null,null);
        } else {
            headerHolder = (UniversalViewHolder) convertView.getTag();
        }
        final User user = getItem(position);
        if (user == null) {
            return headerHolder.getConvertView();
        }
        headerHolder.setText(R.id.list_item_header, String.format(Locale.getDefault(), "%s%s", user.getInitialLetter().charAt(0), ""));
        return headerHolder.getConvertView();
    }

    @Override
    public long getHeaderId(int position) {
        User user = getItem(position);
        if (user != null) {
           return user.getInitialLetter().charAt(0);
        }
        return -1;
    }

    public void remove(User user) {
        mDataList.remove(user);
        notifyDataSetChanged();
    }
}
