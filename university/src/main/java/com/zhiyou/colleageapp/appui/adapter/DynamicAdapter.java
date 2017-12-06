package com.zhiyou.colleageapp.appui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zhiyou.colleageapp.appui.adapter.viewholder.UniversalViewHolder;
import com.zhiyou.colleageapp.domain.Dynamic;
import com.zhiyou.colleageapp.eenum.ResourceType;

import java.util.List;


/**
 * Author ： LongWeiHu on 2016/5/16.
 * 多布局类型的Adapter
 */
public abstract class DynamicAdapter extends BaseAdapter{

    protected List<Dynamic> mDataList;
    protected Context mContext;
    protected LayoutInflater mInflater;
    /**
     * 显示文本的布局id
     */
    private int mTextLayoutId;
    /**
     * 显示图片的布局的id
     */
    private int mPicLayoutId;

    private ResourceType  mResourceType = ResourceType.text;

    public DynamicAdapter(Context context, int textLayoutId, int picLayoutId, List<Dynamic> dataList){
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mDataList = dataList;
        this.mTextLayoutId = textLayoutId;
        this.mPicLayoutId = picLayoutId;
    }



    @Override
    public int getCount()
    {
        return mDataList.size();
    }

    @Override
    public Dynamic getItem(int position){
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UniversalViewHolder holder ;
        mResourceType = getItem(position).getResourceType();
        switch (mResourceType) {

            default:
            case text:
                holder = UniversalViewHolder.getViewHolder(mInflater, convertView, parent, mTextLayoutId, position);
                convertText(holder, getItem(position));
                mResourceType = ResourceType.text;
                break;
            case pic:
                holder = UniversalViewHolder.getViewHolder(mInflater, convertView, parent, mPicLayoutId, position);
                convertPic(holder, getItem(position));
                mResourceType = ResourceType.pic;
                break;

        }

        return holder.getConvertView();

    }



    public abstract void convertText(UniversalViewHolder holder, Dynamic entity);
    public abstract void convertPic(UniversalViewHolder holder,Dynamic entity);


}
