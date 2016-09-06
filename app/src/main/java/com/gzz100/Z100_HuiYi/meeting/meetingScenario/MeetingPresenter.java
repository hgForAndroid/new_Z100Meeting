package com.gzz100.Z100_HuiYi.meeting.meetingScenario;

import com.gzz100.Z100_HuiYi.data.UserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieQXiong on 2016/9/5.
 */
public class MeetingPresenter implements MeetingContract.Presenter {
    private MeetingContract.View mView;
    public MeetingPresenter(MeetingContract.View view) {
        this.mView = view;
        this.mView.setPresenter(this);
    }

    @Override
    public void getMeetingInfo() {

        mView.showMeetingInfo();

    }

    @Override
    public void showOthers() {
        mView.showOthers();
    }

    @Override
    public void fetchUserInfo(int userId) {

        mView.showUserInfo();

    }

    boolean firstLoad = true;
    private List<UserBean> mUsers = new ArrayList<>();
    @Override
    public void fetchMainUsers() {
        if (firstLoad){
            firstLoad = false;
            UserBean user1 = new UserBean(1,"张三张三","android开发",2);
            UserBean user2 = new UserBean(2,"王麻子","Java开发",2);
            UserBean user3 = new UserBean(3,"临到","iOS开发",2);
            UserBean user4 = new UserBean(4,"李四","c++开发",2);
            UserBean user5 = new UserBean(5,"王五","c开发",2);
            UserBean user6 = new UserBean(6,"王小二","c#开发",2);
            UserBean user7 = new UserBean(7,"赵烈","技术支持",2);
            UserBean user8 = new UserBean(8,"饼子","客服",2);
            UserBean user9 = new UserBean(9,"胡子","经理",1);
            UserBean user10 = new UserBean(10,"阿毛","助理",3);
            mUsers.add(user1);
            mUsers.add(user2);
            mUsers.add(user3);
            mUsers.add(user4);
            mUsers.add(user5);
        mUsers.add(user6);
        mUsers.add(user7);
        mUsers.add(user8);
        mUsers.add(user9);
        mUsers.add(user10);

            mView.showMeetingRoom(mUsers);
        }

    }

    @Override
    public void resetFirstLoad(){
        firstLoad = true;
    }

    @Override
    public void showDelegate() {
        mView.showDelegate(true);
    }

    @Override
    public void setOtherNum(int otherNum) {

    }

    @Override
    public void start() {
        fetchMainUsers();
    }
}
