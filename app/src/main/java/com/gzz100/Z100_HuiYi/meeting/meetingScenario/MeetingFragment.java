package com.gzz100.Z100_HuiYi.meeting.meetingScenario;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.UserBean;

import java.util.ArrayList;
import java.util.List;

/**
* 文件详情
* @author XieQXiong
* create at 2016/8/23 17:01
*/

public class MeetingFragment extends Fragment {

    private MeetingRoomView mMeetingRoomView;

    private List<UserBean> users = new ArrayList<>();

    public static MeetingFragment newInstance(){return new MeetingFragment();}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
//        Log.e("MeetingFragment -->","onCreate");
        super.onCreate(savedInstanceState);
        UserBean user1 = new UserBean(1,"张三","android开发",2);
        UserBean user2 = new UserBean(2,"王麻子","Java开发",2);
        UserBean user3 = new UserBean(3,"临到","iOS开发",2);
        UserBean user4 = new UserBean(4,"李四","c++开发",2);
        UserBean user5 = new UserBean(5,"王五","c开发",2);
        UserBean user6 = new UserBean(6,"王小二","c#开发",2);
        UserBean user7 = new UserBean(7,"赵烈","技术支持",2);
        UserBean user8 = new UserBean(8,"饼子","客服",2);
        UserBean user9 = new UserBean(9,"胡子","经理",1);
        UserBean user10 = new UserBean(10,"阿毛","助理",3);
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);
        users.add(user6);
        users.add(user7);
        users.add(user8);
        users.add(user9);
        users.add(user10);

    }


    @Override
    public void onStart() {
        super.onStart();
        mMeetingRoomView.addUsers(users, new OnUserClickListener() {
            @Override
            public void onUserClick(View v, int position) {
                Toast.makeText(getActivity(),users.get(position).getUserName(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPause() {
//        Log.e("MeetingFragment -->","onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
//        Log.e("MeetingFragment -->","onStop");
        super.onStop();
    }

    @Override
    public void onDetach() {
//        Log.e("MeetingFragment -->","onDetach");
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
//        Log.e("MeetingFragment -->","onAttach");
        super.onAttach(context);
    }

    @Override
    public void onResume() {
//        Log.e("MeetingFragment -->","onResume");
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting, null);
        mMeetingRoomView = (MeetingRoomView) view.findViewById(R.id.id_meeting_view);
        return view;
    }

    @Override
    public void onDestroyView() {
//        Log.e("MeetingFragment -->","onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
//        Log.e("MeetingFragment -->","onDestroy");
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        Log.e("MeetingFragment -->","onSaveInstanceState");
        outState.putString("key","saveBundle");

    }


}
