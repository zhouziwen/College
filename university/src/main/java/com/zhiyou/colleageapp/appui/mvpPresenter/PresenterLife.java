package com.zhiyou.colleageapp.appui.mvpPresenter;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.model.PartTime;
import com.zhiyou.colleageapp.appui.model.TaoyuCategory;
import com.zhiyou.colleageapp.appui.model.TreeHole;
import com.zhiyou.colleageapp.appui.model.TreeHoleComment;
import com.zhiyou.colleageapp.appui.model.TreeHoleDetailInfo;
import com.zhiyou.colleageapp.appui.mvpMode.ModeLife;
import com.zhiyou.colleageapp.appui.mvpView.ViewBase;
import com.zhiyou.colleageapp.appui.mvpView.ViewLife;
import com.zhiyou.colleageapp.appui.mvpView.ViewSuccess;
import com.zhiyou.colleageapp.constants.HttpKey;
import com.zhiyou.colleageapp.httpservice.ApiResult;
import com.zhiyou.colleageapp.httpservice.UrlConstant;

import java.util.List;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * Create by chuyh on 2016/5/30.
 */
public class PresenterLife<T> extends PresenterBase {

    private ModeLife mModeLife;
    private ViewLife<T> mViewLife;

    public PresenterLife(ViewLife<T> viewLife) {
        super(viewLife);
        mViewLife = viewLife;
        mModeLife = new ModeLife();
    }

    public PresenterLife(ViewBase viewBase) {
        super(viewBase);
        mModeLife = new ModeLife();
    }


    public void getLifeInfo() {
        mModeLife.getLifeInfo(UrlConstant.UserInfo.mFiledMap)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mViewLife.onViewStart(R.string.start);
                    }
                })
                .subscribe(new Subscriber<ApiResult>() {
                               @Override
                               public void onCompleted() {
                                   mViewLife.onViewComplete();
                               }

                               @Override
                               public void onError(Throwable e) {
                                   mViewLife.onViewError(R.string.local_fail, e);
                                   e.printStackTrace();
                               }

                               @Override
                               public void onNext(ApiResult apiResult) {
                                   if (apiResult.getStatus() == HttpKey.SUCCESS) {
                                       mViewLife.onViewSuccess((T) apiResult.getData());
                                   } else {
                                       mViewLife.onViewFail(R.string.local_fail, apiResult.getMessage());
                                   }
                               }
                           }
                );
    }

    public void getTreeHoleList(final ViewSuccess<List<TreeHole>> viewSuccess) {
        mModeLife.getTreeHoleList(UrlConstant.UserInfo.mFiledMap).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                mViewBase.onViewStart(R.string.start);
            }
        })
                .subscribe(new Subscriber<ApiResult<List<TreeHole>>>() {
                               @Override
                               public void onCompleted() {
                                   mViewBase.onViewComplete();
                               }

                               @Override
                               public void onError(Throwable e) {
                                   mViewBase.onViewError(R.string.local_fail, e);
                                   e.printStackTrace();
                               }

                               @Override
                               public void onNext(ApiResult<List<TreeHole>> apiResult) {
                                   if (apiResult.getStatus() == HttpKey.SUCCESS) {
                                       viewSuccess.onSuccess(apiResult.getData());
                                   } else {
                                       mViewBase.onViewFail(R.string.local_fail, apiResult.getMessage());
                                   }
                               }
                           }
                );
    }

    public void getTreeHoleCommentList(String id, final ViewSuccess<TreeHoleDetailInfo> viewSuccess) {
        mModeLife.getTreeHoleComment(UrlConstant.UserInfo.mFiledMap, id).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                mViewBase.onViewStart(R.string.start);
            }
        })
                .subscribe(new Subscriber<ApiResult<TreeHoleDetailInfo>>() {
                               @Override
                               public void onCompleted() {
                                   mViewBase.onViewComplete();
                               }

                               @Override
                               public void onError(Throwable e) {
                                   mViewBase.onViewError(R.string.fail, e);
                                   e.printStackTrace();
                               }

                               @Override
                               public void onNext(ApiResult<TreeHoleDetailInfo> apiResult) {
                                   if (apiResult.getStatus() == HttpKey.SUCCESS) {
                                       viewSuccess.onSuccess(apiResult.getData());
                                   } else {
                                       mViewBase.onViewFail(R.string.fail, apiResult.getMessage());
                                   }
                               }
                           }
                );
    }

    public void getSchoolActList() {
        mModeLife.getSchoolActList(UrlConstant.UserInfo.mFiledMap).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                mViewLife.onViewStart(R.string.start);
            }
        })
                .subscribe(new Subscriber<ApiResult>() {
                               @Override
                               public void onCompleted() {
                                   mViewLife.onViewComplete();
                               }

                               @Override
                               public void onError(Throwable e) {
                                   mViewLife.onViewError(R.string.local_fail, e);
                                   e.printStackTrace();
                               }

                               @Override
                               public void onNext(ApiResult apiResult) {
                                   if (apiResult.getStatus() == HttpKey.SUCCESS) {
                                       mViewLife.onViewSuccess((T) apiResult.getData());
                                   } else {
                                       mViewLife.onViewFail(R.string.local_fail, apiResult.getMessage());
                                   }
                               }
                           }
                );
    }

    public void getSchoolActDetail(String id) {
        mModeLife.getSchoolActDetail(UrlConstant.UserInfo.mFiledMap, id).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                mViewLife.onViewStart(R.string.start);
            }
        })
                .subscribe(new Subscriber<ApiResult>() {
                               @Override
                               public void onCompleted() {
                                   mViewLife.onViewComplete();
                               }

                               @Override
                               public void onError(Throwable e) {
                                   mViewLife.onViewError(R.string.local_fail, e);
                                   e.printStackTrace();
                               }

                               @Override
                               public void onNext(ApiResult apiResult) {
                                   if (apiResult.getStatus() == HttpKey.SUCCESS) {
                                       mViewLife.onViewSuccess((T) apiResult.getData());
                                   } else {
                                       mViewLife.onViewFail(R.string.local_fail, apiResult.getMessage());
                                   }
                               }
                           }
                );
    }

    public void getOfferList() {
        mModeLife.getOfferList(UrlConstant.UserInfo.mFiledMap).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                mViewLife.onViewStart(R.string.start);
            }
        })
                .subscribe(new Subscriber<ApiResult>() {
                               @Override
                               public void onCompleted() {
                                   mViewLife.onViewComplete();
                               }

                               @Override
                               public void onError(Throwable e) {
                                   mViewLife.onViewError(R.string.local_fail, e);
                                   e.printStackTrace();
                               }

                               @Override
                               public void onNext(ApiResult apiResult) {
                                   if (apiResult.getStatus() == HttpKey.SUCCESS) {
                                       mViewLife.onViewSuccess((T) apiResult.getData());
                                   } else {
                                       mViewLife.onViewFail(R.string.local_fail, apiResult.getMessage());
                                   }
                               }
                           }
                );
    }

    public void getOfferDetail(String id, final ViewSuccess<PartTime> viewSuccess) {
        mModeLife.getOfferDetail(UrlConstant.UserInfo.mFiledMap, id).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                mViewBase.onViewStart(R.string.start);
            }
        })
                .subscribe(new Subscriber<ApiResult<PartTime>>() {
                               @Override
                               public void onCompleted() {
                                   mViewBase.onViewComplete();
                               }

                               @Override
                               public void onError(Throwable e) {
                                   mViewBase.onViewError(R.string.local_fail, e);
                                   e.printStackTrace();
                               }

                               @Override
                               public void onNext(ApiResult<PartTime> apiResult) {
                                   if (apiResult.getStatus() == HttpKey.SUCCESS) {
                                       viewSuccess.onSuccess(apiResult.getData());
                                   } else {
                                       mViewBase.onViewFail(R.string.local_fail, apiResult.getMessage());
                                   }
                               }
                           }
                );
    }

    public void getTaoyuList() {
        mModeLife.getTaoyuList(UrlConstant.UserInfo.mFiledMap).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                mViewLife.onViewStart(R.string.start);
            }
        })
                .subscribe(new Subscriber<ApiResult>() {
                               @Override
                               public void onCompleted() {
                                   mViewLife.onViewComplete();
                               }

                               @Override
                               public void onError(Throwable e) {
                                   mViewLife.onViewError(R.string.local_fail, e);
                                   e.printStackTrace();
                               }

                               @Override
                               public void onNext(ApiResult apiResult) {
                                   if (apiResult.getStatus() == HttpKey.SUCCESS) {
                                       mViewLife.onViewSuccess((T) apiResult.getData());
                                   } else {
                                       mViewLife.onViewFail(R.string.local_fail, apiResult.getMessage());
                                   }
                               }
                           }
                );
    }

    public void getTaoyuCategory(final ViewSuccess<List<TaoyuCategory>> viewSuccess) {
        mModeLife.getTaoyuCategory(UrlConstant.UserInfo.mFiledMap).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                mViewBase.onViewStart(R.string.start);
            }
        })
                .subscribe(new Subscriber<ApiResult<List<TaoyuCategory>>>() {
                               @Override
                               public void onCompleted() {
                                   mViewBase.onViewComplete();
                               }

                               @Override
                               public void onError(Throwable e) {
                                   mViewBase.onViewError(R.string.local_fail, e);
                                   e.printStackTrace();
                               }

                               @Override
                               public void onNext(ApiResult<List<TaoyuCategory>> apiResult) {
                                   if (apiResult.getStatus() == HttpKey.SUCCESS) {
                                       viewSuccess.onSuccess(apiResult.getData());
                                   } else {
                                       mViewBase.onViewFail(R.string.local_fail, apiResult.getMessage());
                                   }
                               }
                           }
                );
    }

    public void getMyTreeholeList(final ViewSuccess<List<TreeHole>> viewSuccess) {
        mModeLife.getMyTreeHoleList(UrlConstant.UserInfo.mFiledMap).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                mViewBase.onViewStart(R.string.start);
            }
        })
                .subscribe(new Subscriber<ApiResult<List<TreeHole>>>() {
                               @Override
                               public void onCompleted() {
                                   mViewBase.onViewComplete();
                               }

                               @Override
                               public void onError(Throwable e) {
                                   mViewBase.onViewError(R.string.local_fail, e);
                                   e.printStackTrace();
                               }

                               @Override
                               public void onNext(ApiResult<List<TreeHole>> apiResult) {
                                   if (apiResult.getStatus() == HttpKey.SUCCESS) {
                                       viewSuccess.onSuccess(apiResult.getData());
                                   } else {
                                       mViewBase.onViewFail(R.string.local_fail, apiResult.getMessage());
                                   }
                               }
                           }
                );
    }

    public void getComTreeholeList(final ViewSuccess<List<TreeHole>> viewSuccess) {
        mModeLife.getComTreeHoleList(UrlConstant.UserInfo.mFiledMap).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                mViewBase.onViewStart(R.string.start);
            }
        })
                .subscribe(new Subscriber<ApiResult<List<TreeHole>>>() {
                               @Override
                               public void onCompleted() {
                                   mViewBase.onViewComplete();
                               }

                               @Override
                               public void onError(Throwable e) {
                                   mViewBase.onViewError(R.string.local_fail, e);
                                   e.printStackTrace();
                               }

                               @Override
                               public void onNext(ApiResult<List<TreeHole>> apiResult) {
                                   if (apiResult.getStatus() == HttpKey.SUCCESS) {
                                       viewSuccess.onSuccess(apiResult.getData());
                                   } else {
                                       mViewBase.onViewFail(R.string.local_fail, apiResult.getMessage());
                                   }
                               }
                           }
                );
    }

    public void pubTaoyu(String name, String[] imgs, String cateid, String price, String intro, final ViewSuccess viewSuccess) {
        mModeLife.pubTaoyu(UrlConstant.UserInfo.mFiledMap, name, imgs, cateid, price, intro).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                mViewBase.onViewStart(R.string.start);
            }
        })
                .subscribe(new Subscriber<ApiResult>() {
                               @Override
                               public void onCompleted() {
                                   mViewBase.onViewComplete();
                               }

                               @Override
                               public void onError(Throwable e) {
                                   mViewBase.onViewError(R.string.local_fail, e);
                                   e.printStackTrace();
                               }

                               @Override
                               public void onNext(ApiResult apiResult) {
                                   if (apiResult.getStatus() == HttpKey.SUCCESS) {
                                       viewSuccess.onSuccess(apiResult.getData());
                                   } else {
                                       mViewBase.onViewFail(R.string.local_fail, apiResult.getMessage());
                                   }
                               }
                           }
                );
    }

    public void getTaoyuDetail(String id) {
        mModeLife.getTaoyuDetail(UrlConstant.UserInfo.mFiledMap, id).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                mViewLife.onViewStart(R.string.start);
            }
        })
                .subscribe(new Subscriber<ApiResult>() {
                               @Override
                               public void onCompleted() {
                                   mViewLife.onViewComplete();
                                   this.unsubscribe();
                               }

                               @Override
                               public void onError(Throwable e) {
                                   mViewLife.onViewError(R.string.local_fail, e);
                                   e.printStackTrace();
                                   this.unsubscribe();
                               }

                               @Override
                               public void onNext(ApiResult apiResult) {
                                   if (apiResult.getStatus() == HttpKey.SUCCESS) {
                                       mViewLife.onViewSuccess((T) apiResult.getData());
                                   } else {
                                       mViewLife.onViewFail(R.string.local_fail, apiResult.getMessage());
                                   }
                               }
                           }
                );
    }

    @Override
    public void releaseAll() {
        mModeLife.releaseAll();
    }
}
