    package com.zhiyou.colleageapp.appui.widget.chatrow;

    import android.content.Context;
    import android.widget.BaseAdapter;
    import android.widget.ImageView;
    import android.widget.TextView;

    import com.bumptech.glide.Glide;
    import com.hyphenate.chat.EMMessage;
    import com.zhiyou.colleageapp.R;
    import com.zhiyou.colleageapp.appui.model.EaseUI;
    import com.zhiyou.colleageapp.constants.EaseConstant;
    import com.zhiyou.colleageapp.domain.EaseEmoIcon;

    /**
 * 大表情(动态表情)
 *
 */
public class EaseEaseChatRowBigExpression extends EaseEaseChatRowText {

    private ImageView imageView;


    public EaseEaseChatRowBigExpression(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }
    
    @Override
    protected void onInflatView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_bigexpression : R.layout.ease_row_sent_bigexpression, this);
    }

    @Override
    protected void onFindViewById() {
        percentageView = (TextView) findViewById(R.id.percentage);
        imageView = (ImageView) findViewById(R.id.image);
    }


    @Override
    public void onSetUpView() {
        String emojiconId = message.getStringAttribute(EaseConstant.MESSAGE_ATTR_EXPRESSION_ID, null);
        EaseEmoIcon emojicon = null;
        if(EaseUI.getInstance().getEmoIconInfoProvider() != null){
            emojicon =  EaseUI.getInstance().getEmoIconInfoProvider().getEmoIconInfo(emojiconId);
        }
        if(emojicon != null){
            if(emojicon.getBigIcon() != 0){
                Glide.with(activity).load(emojicon.getBigIcon()).placeholder(R.drawable.ease_default_expression).into(imageView);
            }else if(emojicon.getBigIconPath() != null){
                Glide.with(activity).load(emojicon.getBigIconPath()).placeholder(R.drawable.ease_default_expression).into(imageView);
            }else{
                imageView.setImageResource(R.drawable.ease_default_expression);
            }
        }
        
        handleTextMessage();
    }
}
