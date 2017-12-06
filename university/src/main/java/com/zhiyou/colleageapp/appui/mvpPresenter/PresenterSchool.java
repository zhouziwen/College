package com.zhiyou.colleageapp.appui.mvpPresenter;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.mvpMode.ModeSchool;
import com.zhiyou.colleageapp.appui.mvpView.ViewSchool;
import com.zhiyou.colleageapp.constants.HttpKey;
import com.zhiyou.colleageapp.httpservice.ApiResult;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * Create by chuyh on 2016/5/30.
 */
public class PresenterSchool<T> {

    private ModeSchool mModeSchool;
    private ViewSchool<T> mViewSchool;

    public PresenterSchool(ViewSchool<T> viewLife) {
        mViewSchool = viewLife;
        mModeSchool = new ModeSchool();
    }

    public void getSchoolInfo() {
        mModeSchool.getSchoolInfo(HttpKey.APPSECRET)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mViewSchool.onViewStart(R.string.start);
                    }
                })
                .subscribe(new Subscriber<ApiResult>() {
                               @Override
                               public void onCompleted() {
                                   mViewSchool.onViewComplete();
                                   this.unsubscribe();
                               }

                               @Override
                               public void onError(Throwable e) {
                                   mViewSchool.onViewError(R.string.local_fail, e);
                                   e.printStackTrace();
                                   this.unsubscribe();
                               }

                               @Override
                               public void onNext(ApiResult apiResult) {
                                   if (apiResult.getStatus() == HttpKey.SUCCESS) {
                                       mViewSchool.onViewSuccess((T) apiResult.getData());
                                   } else {
                                       mViewSchool.onViewFail(R.string.local_fail,apiResult.getMessage());
                                   }
                               }
                           }
                );
    }

    public void getSchoolSlider() {
        mModeSchool.getSchoolSlider(HttpKey.APPSECRET)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mViewSchool.onViewStart(R.string.start);
                    }
                })
                .subscribe(new Subscriber<ApiResult>() {
                               @Override
                               public void onCompleted() {
                                   mViewSchool.onViewComplete();
                                   this.unsubscribe();
                               }

                               @Override
                               public void onError(Throwable e) {
                                   mViewSchool.onViewError(R.string.local_fail, e);
                                   e.printStackTrace();
                                   this.unsubscribe();
                               }

                               @Override
                               public void onNext(ApiResult apiResult) {
                                   if (apiResult.getStatus() == HttpKey.SUCCESS) {
                                       mViewSchool.onViewSuccess((T) apiResult.getData());
                                   } else {
                                       mViewSchool.onViewFail(R.string.local_fail,apiResult.getMessage());
                                   }
                               }
                           }
                );
    }

    public void getSchoolNewsDetail(String id) {
        mModeSchool.getSchoolNewsDetail(HttpKey.APPSECRET, id)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mViewSchool.onViewStart(R.string.start);
                    }
                })
                .subscribe(new Subscriber<ApiResult>() {
                               @Override
                               public void onCompleted() {
                                   mViewSchool.onViewComplete();
                                   this.unsubscribe();
                               }

                               @Override
                               public void onError(Throwable e) {
                                   mViewSchool.onViewError(R.string.local_fail, e);
                                   e.printStackTrace();
                                   this.unsubscribe();
                               }

                               @Override
                               public void onNext(ApiResult apiResult) {
                                   if (apiResult.getStatus() == HttpKey.SUCCESS) {
                                       mViewSchool.onViewSuccess((T) apiResult.getData());
                                   } else {
                                       mViewSchool.onViewFail(R.string.local_fail,apiResult.getMessage());
                                   }
                               }
                           }
                );
    }

    public void getSocietyNewsList() {
        mModeSchool.getSocietyNewsList(HttpKey.APPSECRET)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mViewSchool.onViewStart(R.string.start);
                    }
                })
                .subscribe(new Subscriber<ApiResult>() {
                               @Override
                               public void onCompleted() {
                                   mViewSchool.onViewComplete();
                                   this.unsubscribe();
                               }

                               @Override
                               public void onError(Throwable e) {
                                   mViewSchool.onViewError(R.string.local_fail, e);
                                   e.printStackTrace();
                                   this.unsubscribe();
                               }

                               @Override
                               public void onNext(ApiResult apiResult) {
                                   if (apiResult.getStatus() == HttpKey.SUCCESS) {
                                       mViewSchool.onViewSuccess((T) apiResult.getData());
                                   } else {
                                       mViewSchool.onViewFail(R.string.local_fail,apiResult.getMessage());
                                   }
                               }
                           }
                );
    }

    public void getSchoolNewsList() {
        mModeSchool.getSchoolNewsList(HttpKey.APPSECRET)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mViewSchool.onViewStart(R.string.start);
                    }
                })
                .subscribe(new Subscriber<ApiResult>() {
                               @Override
                               public void onCompleted() {
                                   mViewSchool.onViewComplete();
                                   this.unsubscribe();
                               }

                               @Override
                               public void onError(Throwable e) {
                                   mViewSchool.onViewError(R.string.local_fail, e);
                                   e.printStackTrace();
                                   this.unsubscribe();
                               }

                               @Override
                               public void onNext(ApiResult apiResult) {
                                   if (apiResult.getStatus() == HttpKey.SUCCESS) {
                                       mViewSchool.onViewSuccess((T) apiResult.getData());
                                   } else {
                                       mViewSchool.onViewFail(R.string.local_fail,apiResult.getMessage());
                                   }
                               }
                           }
                );
    }

    public void getSchoolMajorList() {
        mModeSchool.getSchoolMajorList(HttpKey.APPSECRET)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mViewSchool.onViewStart(R.string.start);
                    }
                })
                .subscribe(new Subscriber<ApiResult>() {
                               @Override
                               public void onCompleted() {
                                   mViewSchool.onViewComplete();
                                   this.unsubscribe();
                               }

                               @Override
                               public void onError(Throwable e) {
                                   mViewSchool.onViewError(R.string.local_fail, e);
                                   e.printStackTrace();
                                   this.unsubscribe();
                               }

                               @Override
                               public void onNext(ApiResult apiResult) {
                                   if (apiResult.getStatus() == HttpKey.SUCCESS) {
                                       mViewSchool.onViewSuccess((T) apiResult.getData());
                                   } else {
                                       mViewSchool.onViewFail(R.string.local_fail,apiResult.getMessage());
                                   }
                               }
                           }
                );
    }

    public void getSchoolSimgnewsList() {
        mModeSchool.getSchoolSimgnewsList(HttpKey.APPSECRET)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mViewSchool.onViewStart(R.string.start);
                    }
                })
                .subscribe(new Subscriber<ApiResult>() {
                               @Override
                               public void onCompleted() {
                                   mViewSchool.onViewComplete();
                                   this.unsubscribe();
                               }

                               @Override
                               public void onError(Throwable e) {
                                   mViewSchool.onViewError(R.string.local_fail, e);
                                   e.printStackTrace();
                                   this.unsubscribe();
                               }

                               @Override
                               public void onNext(ApiResult apiResult) {
                                   if (apiResult.getStatus() == HttpKey.SUCCESS) {
                                       mViewSchool.onViewSuccess((T) apiResult.getData());
                                   } else {
                                       mViewSchool.onViewFail(R.string.local_fail,apiResult.getMessage());
                                   }
                               }
                           }
                );
    }
}
