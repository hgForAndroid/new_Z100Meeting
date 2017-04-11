package com.gzz100.Z100_HuiYi.meetingPrepare;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.gzz100.Z100_HuiYi.R;


/**
 * @author XieQingXiong
 * @description EditText右边点击可显示下拉内容。
 * @packageName com.gzz100.Z100_HuiYi.meetingPrepare
 * @className
 * @time 2017/3/28   17:01
 */

public class ExpandableEditText extends LinearLayout {

    private EditText mEditText;
    private Button mButton;
    private Context mContext;

    private RecyclerView mRecyclerView;
    private PopupWindow mPopupWindow;

    private boolean layoutManagerAlreadySet;

    private int defaultWidth = ViewGroup.LayoutParams.MATCH_PARENT;
    private int defaultHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

    public ExpandableEditText(Context context) {
        this(context,null);
    }

    public ExpandableEditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ExpandableEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initLayout(context);
    }

    private void initLayout(Context context) {
        View.inflate(context,R.layout.expandable_edit_text,this);
        mEditText = (EditText) findViewById(R.id.id_edit_text);
        mButton = (Button) findViewById(R.id.id_expand);
        mRecyclerView = (RecyclerView) View.inflate(mContext, R.layout.recycler_view,null);
    }

    /**
     * 设置可以点击显示下拉内容。
     * @param dropWidth      下拉框的宽度，传入的值如果 <= 0 ,则使用默认  MATCH_PARENT。
     * @param dropHeight     下拉框的高度，传入的值如果 <= 0 ,则使用默认 WRAP_CONTENT。
     */
    public void setDropDownEnable(final int dropWidth, final int dropHeight){
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow != null && mPopupWindow.isShowing()){
                    mPopupWindow.dismiss();
                }else {
                    if (dropWidth > 0){
                        defaultWidth = dropWidth;
                    }
                    if (dropHeight > 0){
                        defaultHeight = dropHeight;
                    }
                    mPopupWindow = new PopupWindow(mRecyclerView, defaultWidth,defaultHeight);
                    mPopupWindow.showAsDropDown(mEditText);
                }
            }
        });
    }

    /**
     * 给下拉展示设置一个适配器，该适配器默认使用竖向的 LinearLayoutManager，
     * 也可以自定义 LayoutManager，在该方法调用之前，调用{@link #setLayoutManager(RecyclerView.LayoutManager)}方法即可。
     * @param adapter
     */
    public void setAdapterForDropDown(RecyclerView.Adapter adapter){
        if (mRecyclerView == null){
            return;
        }
        //没有设置LayoutManager，给个默认的 LayoutManager
        if (!layoutManagerAlreadySet){
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL,false));
        }
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * 给下拉显示的RecyclerView设置一个LayoutManager，如果没有设置，则默认使用 竖向的 LinearLayoutManager。
     * 必须在{@link #setAdapterForDropDown(RecyclerView.Adapter)}方法前调用该方法。
     * @param layoutManager
     */
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager){
        if (mRecyclerView == null){
            return;
        }
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManagerAlreadySet = true;
    }

    /**
     * 给RecyclerView的item设置间距,需要在{@link #setAdapterForDropDown(RecyclerView.Adapter)}之前调用。
     * @param itemSpaceDecoration
     */
    public void addItemDecoration(RecyclerView.ItemDecoration itemSpaceDecoration){
        if (mRecyclerView == null){
            return;
        }
        if (itemSpaceDecoration == null){
            return;
        }
        mRecyclerView.addItemDecoration(itemSpaceDecoration);
    }

    /**
     * 给EditText设置值。
     * @param content
     */
    public void setContentForEditText(String content){
        if (TextUtils.isEmpty(content)){
            return;
        }
        mEditText.setText(content);
    }

    public String getContent(){
        String content = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(content)){
            return "";
        }
        return content;

    }
    /**
     * 隐藏下拉显示内容
     */
    public void dismiss(){
        if (mPopupWindow != null && mPopupWindow.isShowing()){
            mPopupWindow.dismiss();
        }
    }
}
