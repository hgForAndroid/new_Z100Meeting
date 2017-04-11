package com.gzz100.Z100_HuiYi.meeting.about;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.R;

/**
* 关于
* @author XieQXiong
* create at 2016/8/23 17:01
*/

public class AboutFragment extends Fragment {

    private TextView mTvFunctionIntroduce;
    private ScrollView mScrollView;
    private boolean expand;//功能列表是否展开

    public static AboutFragment newInstance(){return new AboutFragment();}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, null);
//        ButterKnife.bind(getActivity(),view);
        mTvFunctionIntroduce = (TextView) view.findViewById(R.id.id_fragment_about_function_introduce);
        mScrollView = (ScrollView) view.findViewById(R.id.id_scrollView_of_function);
//        mTvFunctionIntroduce.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (expand){
//                    mScrollView.setVisibility(View.GONE);
//                    Drawable drawable = getResources().getDrawable(R.drawable.ic_arrows_down);
//                    drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
//                    mTvFunctionIntroduce.setCompoundDrawables(null,null,drawable,null);
//                }else {
//                    mScrollView.setVisibility(View.VISIBLE);
//                    Drawable drawable = getResources().getDrawable(R.drawable.ic_arrows_up);
//                    drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
//                    mTvFunctionIntroduce.setCompoundDrawables(null,null,drawable,null);
//                }
//                expand = !expand;
//            }
//        });
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
