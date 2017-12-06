package com.zhiyou.colleageapp.appui.listener;

/**
 * Author ： LongWeiHu on 2016/5/19.
 */
public interface EaseVoiceRecorderCallback {
    /**
     * 录音完毕
     *
     * @param voiceFilePath
     *            录音完毕后的文件路径
     * @param voiceTimeLength
     *            录音时长
     */
    void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength);

}
