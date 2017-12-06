package com.zhiyou.colleageapp.appui.life.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.CommonAdapter;
import com.zhiyou.colleageapp.appui.ViewHolder;
import com.zhiyou.colleageapp.appui.model.TaoyuCategory;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterLife;
import com.zhiyou.colleageapp.appui.mvpView.ViewSuccess;
import com.zhiyou.colleageapp.appui.widget.ShowBigImageActivity;
import com.zhiyou.colleageapp.appui.widget.listview.HorizontalListView;
import com.zhiyou.colleageapp.constants.HttpKey;
import com.zhiyou.colleageapp.httpservice.ApiFactory;
import com.zhiyou.colleageapp.httpservice.ApiResult;
import com.zhiyou.colleageapp.utils.AppLog;
import com.zhiyou.colleageapp.utils.AppToast;
import com.zhiyou.colleageapp.utils.CameraHandler;
import com.zhiyou.colleageapp.utils.DialogUtils;
import com.zhiyou.colleageapp.utils.ImageTools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/5/30.
 */
public class TaoyuPubFragment extends BaseFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.taoyu_fragment_pub, container, false);
    }

    private HorizontalListView mListView;
    private CommonAdapter<String> mAdapter;

    private RelativeLayout mAddImg;
    private TextView mSelectClass;
    private EditText mContent, mPrice, mSchool;
    private ImageView mImageView;
    private ArrayList<String> mList;
    private ArrayList<TaoyuCategory> mCategoryList;

    private PresenterLife mPresenterLife;

    private String mPostCategoryId, mPostPrice, mPostSchool, mPostContent;
    private List<String> mPostImg;

    @Override
    protected void initView(View view) {
        super.initView(view);
        mAppTitleBar.setTitle("发布");
        mAppTitleBar.getAction().setText("提交");
        mAppTitleBar.getAction().setOnClickListener(this);
        //存放本地图片地址
        mList = new ArrayList<>();
        mPostImg = new ArrayList<>();
        mPresenterLife = new PresenterLife(new MyViewBase());

        mContent = (EditText) view.findViewById(R.id.taoyu_edit_content);
        mPrice = (EditText) view.findViewById(R.id.taoyu_pub_price);
        mSchool = (EditText) view.findViewById(R.id.taoyu_pub_school);

        mImageView = (ImageView) view.findViewById(R.id.taoyu_edit_addimg_tempiv);
        mAddImg = (RelativeLayout) view.findViewById(R.id.taoyu_edit_addimg);
        mSelectClass = (TextView) view.findViewById(R.id.taoyu_pub_lei);
        mAddImg.setOnClickListener(this);
        mSelectClass.setOnClickListener(this);
        mListView = (HorizontalListView) view.findViewById(R.id.taoyu_edit_addimg_listview);

        mAdapter = new CommonAdapter<String>(mContext, R.layout.item_taoyu_pub, new CommonAdapter.Callback() {
            @Override
            public void click(View v) {
                int n = (int) v.getTag();
                mList.remove(n);
                if (mPostImg.size() > n)
                    mPostImg.remove(n);
                mAdapter.bindData(mList);
            }
        }) {
            @Override
            public void conver(ViewHolder holder, String s) {
                holder.setImgBitmap(R.id.taoyu_item_pub_iv, ImageTools.getPhotoFromSDCard(s));
                holder.setClick(R.id.taoyu_item_pub_iv_delete, this);
            }
        };
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, ShowBigImageActivity.class);
                intent.putStringArrayListExtra("path", mList);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.taoyu_edit_addimg:
                selectPicture();
                break;
            case R.id.taoyu_pub_lei:
                if (mCategoryList != null) {
                    Intent intent = new Intent(mContext, TaoyuCategoryActivity.class);
                    intent.putParcelableArrayListExtra("list", mCategoryList);
                    startActivityForResult(intent, CameraHandler.CATEGORY);
                } else {
                    mPresenterLife.getTaoyuCategory(new ViewSuccess<ArrayList<TaoyuCategory>>() {
                        @Override
                        public void onSuccess(ArrayList<TaoyuCategory> taoyuCategories) {
                            mCategoryList = taoyuCategories;
                            Intent intent = new Intent(mContext, TaoyuCategoryActivity.class);
                            intent.putParcelableArrayListExtra("list", taoyuCategories);
                            startActivityForResult(intent, CameraHandler.CATEGORY);
                        }
                    });
                }
                break;
            case R.id.app_title_action:
                mPostPrice = mPrice.getText().toString().trim();
                mPostSchool = mSchool.getText().toString().trim();
                mPostContent = mContent.getText().toString().trim();

                if (!TextUtils.isEmpty(mPostPrice) && !TextUtils.isEmpty(mPostSchool) && !TextUtils.isEmpty(mPostContent) && !TextUtils.isEmpty(mPostCategoryId)) {
                    AppLog.instance().d(mPostImg.toString());
                    mPresenterLife.pubTaoyu(EMClient.getInstance().getCurrentUser(), mPostImg.toArray(new String[mPostImg.size()]), mPostCategoryId, mPostPrice, mPostContent, new ViewSuccess() {
                        @Override
                        public void onSuccess(Object o) {
                            popSelfNoHiddenKeyBoard();
                        }
                    });
                } else {
                    AppToast.showCenterText("请完善信息");
                }
                break;
        }
    }

    private final int SCALE = 5;//照片缩小比例
    private DialogUtils mDialogUtils;
    private void selectPicture() {
        if (mDialogUtils == null) {
            mDialogUtils = new DialogUtils();
        }
        mDialogUtils.showGetPicDialog(TaoyuPubFragment.this);
    }

    private String filename;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CameraHandler.REQUEST_CODE_CAMERA:
                Bitmap bitmap = BitmapFactory.decodeFile(CameraHandler.mCameraPicPath);
                Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
                //由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
                bitmap.recycle();
                upLoadImage(newBitmap);
                break;
            case CameraHandler.REQUEST_CODE_PICK:
                if (data == null || data.getData() == null) {
                    return;
                }
                CameraHandler.startPhotoZoom(data.getData(),TaoyuPubFragment.this);
                break;
            case CameraHandler.REQUEST_CODE_CUTTING:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap photo = extras.getParcelable("data");

                        upLoadImage(photo);
                    }
                }
                break;
            case CameraHandler.CATEGORY:
                if (data != null) {
                    mPostCategoryId = data.getStringExtra("id");
                    mSelectClass.setText(data.getStringExtra("name"));
                }
                break;
            default:
                break;
        }
    }

    private void upLoadImage(Bitmap bitmap) {
        //临时路径Environment.getExternalStorageDirectory() + "/image.jpg"
        //保存路径Environment.getExternalStorageDirectory().getAbsolutePath() + String.valueOf(System.currentTimeMillis()) + ".png"
        filename = String.valueOf(System.currentTimeMillis());
        ImageTools.savePhotoToSDCard(bitmap, Environment.getExternalStorageDirectory().getAbsolutePath(), filename);

        AppLog.instance().d("存入list中的图片路径");
        AppLog.instance().d(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + filename + ".png");
        mList.add(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + filename + ".png");
        mAdapter.bindData(mList);

        //单张图片上传
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + filename + ".png"));
        ApiFactory.getFartory().getFileService().uploadImage(HttpKey.APPSECRET, filename + ".png", body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<ApiResult<List<String>>, List<String>>() {
                    @Override
                    public List<String> call(ApiResult<List<String>> listApiResult) {
                        if (listApiResult.getStatus() == 2000)
                            return listApiResult.getData();
                        else
                            return null;
                    }
                })
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> strings) {
                        if (strings != null) {
                            AppLog.instance().d(strings);
                            mPostImg.add(strings.get(0));
                        } else
                            AppToast.showCenterText("上传失败");
                    }
                });

    }

}
