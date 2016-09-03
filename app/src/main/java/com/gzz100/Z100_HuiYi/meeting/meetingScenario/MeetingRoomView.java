package com.gzz100.Z100_HuiYi.meeting.meetingScenario;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.UserBean;
import com.gzz100.Z100_HuiYi.utils.ScreenSize;

import java.util.List;

/**
 * Created by XieQXiong on 2016/9/2.
 */
public class MeetingRoomView extends ViewGroup {
    private Context mContext;
    //会议桌颜色
    private int mTableColor;
    private int mScreenWidth;
    private int mScreenHeight;
    //
    private float mLeft;
    private float mTop;
    private float mRight;
    private float mBottom;
    private float mAlignInner;
    private Paint mTablePaint;
    private Paint mInPaint;
    private RectF mOutRectF;
    private RectF mInRectF;
    private boolean mIsRoundRect;
    private float mRadius;
    private int mBgcolor;
    private int mInnerBgcolor;


    int up = 1;
    int down = 1;


    public MeetingRoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initScreenSize();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MeetingRoomView);

        mTableColor = typedArray.getColor(R.styleable.MeetingRoomView_table_color, 0xccc);
        mLeft = typedArray.getFloat(R.styleable.MeetingRoomView_left, mScreenWidth * 2 / 10);
        mTop = typedArray.getFloat(R.styleable.MeetingRoomView_top, mScreenHeight  / 4);
        mRight = typedArray.getFloat(R.styleable.MeetingRoomView_right, mScreenWidth * 8 / 10);
        mBottom = typedArray.getFloat(R.styleable.MeetingRoomView_bottom, mScreenHeight *3 / 5);
        mAlignInner = typedArray.getFloat(R.styleable.MeetingRoomView_align_inner_rect, 100);
        mIsRoundRect = typedArray.getBoolean(R.styleable.MeetingRoomView_round_rect, false);
        mRadius = typedArray.getFloat(R.styleable.MeetingRoomView_round_rect_radius, 50);
        mBgcolor = typedArray.getColor(R.styleable.MeetingRoomView_background_color,
                mContext.getResources().getColor(R.color.color_meeting_room_bg));
        mInnerBgcolor = typedArray.getColor(R.styleable.MeetingRoomView_inner_background_color,
                mContext.getResources().getColor(R.color.color_white));

        typedArray.recycle();
        initPaint();
    }

    private void initScreenSize() {
        mScreenWidth = ScreenSize.getScreenWidth(mContext);
        mScreenHeight = ScreenSize.getScreenHeight(mContext);
    }

    public MeetingRoomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initPaint();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        float widthAll = mRight - mLeft;
        float heightAll = mBottom - mTop;
//        Log.e("","mRight = "+mRight+"   mLeft = "+mLeft+"    widthAll = "+widthAll+"     heightAll = "+heightAll);
        for (int i = 0; i < childCount; i++) {
            int measuredWidth = getChildAt(i).getMeasuredWidth();
            int measuredHeight = getChildAt(i).getMeasuredHeight();
            int cl = 0;
            int ct = 0;
            int cb = 0;
            int cr = 0;
            if (i == 0){
                cl = (int) (mLeft - measuredWidth -20);
                ct = (int) (mBottom - heightAll / 2 - measuredHeight/2);
                cr = (int) (mLeft - 20);
                cb = (int) (mBottom - heightAll / 2 + measuredHeight/2);
//                getChildAt(i).layout(cl,ct,cr,cb);

            }else if (i == 9){
                cl = (int) mRight + 20 ;
                ct = (int) (mBottom - heightAll / 2 - measuredHeight/2);
                cr = (int) mRight + 20 + measuredWidth;
                cb = (int) (mBottom - heightAll / 2 + measuredHeight/2);
//                getChildAt(i).layout(cl,ct,cr,cb);
            }else {
                if (i % 2 == 0){

                    cl = (int) ( widthAll*down/ 8 - measuredWidth /2 - widthAll + mLeft);
                    ct = (int)(mBottom + 20);
                    cr = (int)(widthAll*down/ 8 + measuredWidth /2 - widthAll + mLeft);
                    cb = (int)(mBottom +measuredHeight + 20);

//                    getChildAt(i).layout(cl,ct,cr,cb);
                    down+=2;
                }else{

                    cl = (int) (widthAll*up/8 - measuredWidth /2 - widthAll + mLeft);
                    ct = (int)(mTop - measuredHeight - 20);
                    cr = (int)(widthAll*up/8 + measuredWidth /2 - widthAll + mLeft);
                    cb = (int)(mTop - 20);
//                    getChildAt(i).layout(cl,ct,cr,cb);
                    up+=2;

                }

            }
            getChildAt(i).layout(cl,ct,cr,cb);

        }
    }

    public void addUsers(List<UserBean> users, final OnUserClickListener onUserClickListener){
        this.removeAllViews();
        for (int i = 0; i < users.size(); i++) {
            View view = View.inflate(this.getContext(),R.layout.user_for_meeting_room,null);
            ImageView pic = (ImageView) view.findViewById(R.id.id_imv_user_pic_meeting_room);
            users.get(i).setPicForUser(mContext,pic);
            TextView name = (TextView) view.findViewById(R.id.id_tv_user_name_meeting_room);
            String userName = users.get(i).getUserName();
            name.setText(userName);
            final int finalI = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onUserClickListener.onUserClick(v, finalI);
                }
            });
            if ("王麻子".equals(userName)){
                view.setBackgroundColor(mContext.getResources().getColor(R.color.color_tab_selected));
            }
            this.addView(view);
        }
        postInvalidate();
    }

    public MeetingRoomView(Context context) {
        super(context);
        this.mContext = context;
        initPaint();
    }
    private void initPaint() {
        mTablePaint = new Paint();
        mTablePaint.setColor(mBgcolor);
        mTablePaint.setAntiAlias(true);
//        mTablePaint.setStrokeWidth(20);//画笔宽度
        mTablePaint.setStyle(Paint.Style.FILL);//画笔空心  FILL 为实心

        mInPaint = new Paint();
        mInPaint.setColor(mInnerBgcolor);
        mInPaint.setAntiAlias(true);
//        mInPaint.setStrokeWidth(20);//画笔宽度
        mInPaint.setStyle(Paint.Style.FILL);//画笔空心  FILL 为实心

        mOutRectF = new RectF(mLeft,mTop,mRight,mBottom);
        mInRectF = new RectF(mLeft+mAlignInner,mTop+mAlignInner,mRight-mAlignInner,mBottom-mAlignInner);
        //ViewGroup  默认不调用onDraw函数，设置下面的方法让它调用
        this.setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mIsRoundRect){
            canvas.drawRoundRect(mOutRectF,mRadius,mRadius,mTablePaint);
            canvas.drawRoundRect(mInRectF,mRadius,mRadius,mInPaint);
        }else {
            canvas.drawRect(mOutRectF,mTablePaint);
            canvas.drawRect(mInRectF,mInPaint);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
