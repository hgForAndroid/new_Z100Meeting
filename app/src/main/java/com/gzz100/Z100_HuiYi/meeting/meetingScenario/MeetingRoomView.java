package com.gzz100.Z100_HuiYi.meeting.meetingScenario;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
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
    //屏幕的宽高
    private int mScreenWidth;
    private int mScreenHeight;
    //外矩形的各个方位的数值
    //默认为  mScreenWidth * 2 / 10，
    // mScreenHeight  / 4     ，    mScreenWidth * 8 / 10   ， mScreenHeight *3 / 5
    private float mLeft;
    private float mTop;
    private float mRight;
    private float mBottom;
    //内外矩形相离的距离
    private float mAlignInner;
    //外矩形，内矩形画笔
    private Paint mOutPaint;
    private Paint mInPaint;
    //外矩形，内矩形
    private RectF mOutRectF;
    private RectF mInRectF;
    //是否是圆角矩形，圆角的值，默认是50
    private boolean mIsRoundRect;
    private float mRadius;
    //内外矩形的背景色
    private int mBgcolor;
    private int mInnerBgcolor;

    //作为用户位置占会议桌长度的 ？/？  的标识，每次自增 2
    int up = 1;
    int down = 1;

    int temp1 = 0;
    int temp2 = 0;

    public MeetingRoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initScreenSize();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MeetingRoomView);

        mLeft = typedArray.getFloat(R.styleable.MeetingRoomView_left, mScreenWidth * 2 / 10);
        mTop = typedArray.getFloat(R.styleable.MeetingRoomView_top, mScreenHeight / 4);
        mRight = typedArray.getFloat(R.styleable.MeetingRoomView_right, mScreenWidth * 8 / 10);
        mBottom = typedArray.getFloat(R.styleable.MeetingRoomView_bottom, mScreenHeight * 3 / 5);
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
        if (!changed)
            return;
        int childCount = getChildCount();
        float widthAll = mRight - mLeft;
        float heightAll = mBottom - mTop;
        for (int i = 0; i < childCount; i++) {
            int measuredWidth = getChildAt(i).getMeasuredWidth();
            int measuredHeight = getChildAt(i).getMeasuredHeight();
            int cl;
            int ct;
            int cb;
            int cr;
            if (i == 0) {
                cl = (int) (mLeft - measuredWidth - 20);
                ct = (int) (mBottom - heightAll / 2 - measuredHeight / 2);
                cr = (int) (mLeft - 20);
                cb = (int) (mBottom - heightAll / 2 + measuredHeight / 2);
                getChildAt(i).layout(cl, ct, cr, cb);

            } else if (i == 9) {
                cl = (int) mRight + 20;
                ct = (int) (mBottom - heightAll / 2 - measuredHeight / 2);
                cr = (int) mRight + 20 + measuredWidth;
                cb = (int) (mBottom - heightAll / 2 + measuredHeight / 2);
                getChildAt(i).layout(cl, ct, cr, cb);
            } else {
                if (i % 2 == 0) {

                    cl = (int) (widthAll * down / 8 - measuredWidth / 2);
                    ct = (int) (mBottom + 20);
                    cr = (int) (widthAll * down / 8 + measuredWidth / 2);
                    cb = (int) (mBottom + measuredHeight + 20);

                    down += 2;
                } else {

                    cl = (int) (widthAll * up / 8 - measuredWidth / 2);
                    ct = (int) (mTop - measuredHeight - 20);
                    cr = (int) (widthAll * up / 8 + measuredWidth / 2);
                    cb = (int) (mTop - 20);
                    up += 2;

                }
                getChildAt(i).layout(cl+ (int) mLeft , ct, cr+ (int) mLeft, cb);
            }
        }

    }

    public void resetUpAndDownValue() {
        up = 1;
        down = 1;
    }

    public void addUsers(List<UserBean> users, String currentName,final OnUserClickListener onUserClickListener) {
        this.removeAllViews();
        for (int i = 0; i < users.size(); i++) {
            View view = View.inflate(this.getContext(), R.layout.user_for_meeting_room, null);
            TextView name = (TextView) view.findViewById(R.id.id_tv_user_name_meeting_room);
            users.get(i).setPicForUser(mContext,name);
            String userName = users.get(i).getUserName();
            name.setText(userName);
            final int finalI = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onUserClickListener.onUserClick(v, finalI);
                }
            });
            if (userName.equals(currentName)) {
                users.get(i).setCurrentDelegate(mContext,name);
            }
            this.addView(view);
        }
        postInvalidate();
//        requestLayout();
    }

    public MeetingRoomView(Context context) {
        super(context);
        this.mContext = context;
        initPaint();
    }

    private void initPaint() {
        mOutPaint = new Paint();
        mOutPaint.setColor(mBgcolor);
        mOutPaint.setAntiAlias(true);
//        mOutPaint.setStrokeWidth(20);//画笔宽度
        mOutPaint.setStyle(Paint.Style.FILL);//画笔空心  FILL 为实心

        mInPaint = new Paint();
        mInPaint.setColor(mInnerBgcolor);
        mInPaint.setAntiAlias(true);
//        mInPaint.setStrokeWidth(20);//画笔宽度
        mInPaint.setStyle(Paint.Style.FILL);//画笔空心  FILL 为实心

        mOutRectF = new RectF(mLeft, mTop, mRight, mBottom);
        mInRectF = new RectF(mLeft + mAlignInner, mTop + mAlignInner, mRight - mAlignInner, mBottom - mAlignInner);
        //ViewGroup  默认不调用onDraw函数，设置下面的方法让它调用
        this.setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mIsRoundRect) {
            canvas.drawRoundRect(mOutRectF, mRadius, mRadius, mOutPaint);
            canvas.drawRoundRect(mInRectF, mRadius, mRadius, mInPaint);
        } else {
            canvas.drawRect(mOutRectF, mOutPaint);
            canvas.drawRect(mInRectF, mInPaint);
        }

    }
    int i = 1 ;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
