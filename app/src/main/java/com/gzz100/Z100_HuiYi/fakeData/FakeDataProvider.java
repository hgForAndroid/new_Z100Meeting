package com.gzz100.Z100_HuiYi.fakeData;

import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.data.DelegateBean;
import com.gzz100.Z100_HuiYi.data.Document;
import com.gzz100.Z100_HuiYi.data.MeetingBean;
import com.gzz100.Z100_HuiYi.data.MeetingInfo;
import com.gzz100.Z100_HuiYi.data.UserBean;
import com.gzz100.Z100_HuiYi.data.Vote;
import com.gzz100.Z100_HuiYi.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieQXiong on 2016/8/29.
 */
public class FakeDataProvider {
    private  static int currRoleNum=0;

    public static List<Agenda> getAgendas() {
        List<Agenda> agendas = new ArrayList<>();
        Agenda agenda1 = new Agenda();
        agenda1.setAgendaIndex(1);
        agenda1.setAgendaName("这是关于议程1的一切标题");
        agenda1.setAgendaDuration("30:00");
        agenda1.setAgendaSpeaker("老大");
        Agenda agenda2 = new Agenda();
        agenda2.setAgendaIndex(2);
        agenda2.setAgendaName("关于这个议程2的事有什么");
        agenda2.setAgendaDuration("29:00");
        agenda2.setAgendaSpeaker("老二");
        Agenda agenda3 = new Agenda();
        agenda3.setAgendaIndex(3);
        agenda3.setAgendaName("马上就到议程3，还有什么事要处理");
        agenda3.setAgendaDuration("33:00");
        agenda3.setAgendaSpeaker("老三");
        agendas.add(agenda1);
        agendas.add(agenda2);
        agendas.add(agenda3);

        return agendas;
    }

    public static List<Document> getFileListByindex(int index) {
        List<Document> documents = new ArrayList<>();
        switch (index) {
            case 1:
                for (int i = 1; i < 4; i++) {
                    Document document = new Document();
                    document.setDocumentAgendaIndex(1);
                    document.setDocumentIndex(i);
                    document.setDocumentName("agenda_one_fileName.txt" + i);
                    document.setDocumentSize(12 + i);
                    document.setDocumentSpeaker("张三");
                    documents.add(document);
                }

                break;
            case 2:
                for (int i = 1; i < 5; i++) {
                    Document document = new Document();
                    document.setDocumentAgendaIndex(2);
                    document.setDocumentIndex(i);
                    document.setDocumentName("agenda_one_fileName.txt" + i);
                    document.setDocumentSize(16 + i);
                    document.setDocumentSpeaker("李四");
                    documents.add(document);
                }
                break;

            case 3:
                for (int i = 1; i < 5; i++) {
                    Document document = new Document();
                    document.setDocumentAgendaIndex(3);
                    document.setDocumentIndex(i);
                    document.setDocumentName("agenda_one_fileName.txt" + i);
                    document.setDocumentSize(4 + i);
                    document.setDocumentSpeaker("王五");
                    documents.add(document);
                }
                break;
        }
        return documents;
    }

    public static List<DelegateBean> getDelegateBeanByRolePos(int rolePos){
        List<DelegateBean> delegateBeans=new ArrayList<>();

        ArrayList<String> speakerNames=new ArrayList<>();
        speakerNames.add("张三");
        speakerNames.add("李四");
        speakerNames.add("王五");

        ArrayList<String> departmentNames=new ArrayList<>();
        departmentNames.add("技术部");
        departmentNames.add("销售部");
        departmentNames.add("运营部");
        departmentNames.add("后勤部");

        ArrayList<String> hostNames=new ArrayList<>();
        hostNames.add("王五");

        ArrayList<String> otherDelegateNames=new ArrayList<>();
        otherDelegateNames.add("张一");
        otherDelegateNames.add("李二");
        otherDelegateNames.add("王三");
        otherDelegateNames.add("张四");
        otherDelegateNames.add("李五");
        otherDelegateNames.add("王六");



        switch (rolePos)
        {
            case 0:
                for(int a=0;a<speakerNames.size();a++) {
                    DelegateBean delegateBean = new DelegateBean();
                    delegateBean.setDelegateName(speakerNames.get(a));
                    delegateBean.setDelegateDepartment(departmentNames.get(a));
                    delegateBean.setRole(Constant.KEYNOTE_SPEAKER);
                    ArrayList agendaIndexList = new ArrayList();
                    agendaIndexList.add(a+1);
                    delegateBean.setDelegateAgendaList(agendaIndexList);
                    delegateBean.setDelegateDetailInfo("我是好人");
                    delegateBeans.add(delegateBean);
                    currRoleNum=0;
                }
                break;
            case 1:
                for(int a=0;a<hostNames.size();a++) {
                    DelegateBean delegateBean = new DelegateBean();
                    delegateBean.setDelegateName(hostNames.get(a));
                    delegateBean.setDelegateDepartment(departmentNames.get(a));
                    delegateBean.setRole(Constant.HOST);
                    ArrayList agendaIndexList = new ArrayList();
                    delegateBean.setDelegateAgendaList(agendaIndexList);
                    delegateBean.setDelegateDetailInfo("他是好人");
                    delegateBeans.add(delegateBean);
                    currRoleNum=1;
                }
                break;
            case 2:
                for(int a=0;a<otherDelegateNames.size();a++) {
                    DelegateBean delegateBean = new DelegateBean();
                    delegateBean.setDelegateName(otherDelegateNames.get(a));
                    delegateBean.setDelegateDepartment(departmentNames.get(a%4));
                    delegateBean.setRole(Constant.NORMAL_DELEGATE);
                    delegateBean.setDelegateDetailInfo("他不是好人");
                    delegateBeans.add(delegateBean);
                    currRoleNum=2;
                }
                break;


        }
        return delegateBeans;
    }
    public static DelegateBean getDelegateDetailByNamePos(int delegateNamePos){
        List<DelegateBean> delegateBeans=new ArrayList<>();

        ArrayList<String> speakerNames=new ArrayList<>();
        speakerNames.add("张三");
        speakerNames.add("李四");
        speakerNames.add("王五");

        ArrayList<String> departmentNames=new ArrayList<>();
        departmentNames.add("技术部");
        departmentNames.add("销售部");
        departmentNames.add("运营部");
        departmentNames.add("后勤部");

        ArrayList<String> jobName=new ArrayList<>();
        jobName.add("技术人员");
        jobName.add("业务人员");
        jobName.add("后勤人员");

        ArrayList<String> hostNames=new ArrayList<>();
        hostNames.add("王五");

        ArrayList<String> otherDelegateNames=new ArrayList<>();
        otherDelegateNames.add("张一");
        otherDelegateNames.add("李二");
        otherDelegateNames.add("王三");
        otherDelegateNames.add("张四");
        otherDelegateNames.add("李五");
        otherDelegateNames.add("王六");

        DelegateBean delegateBean=new DelegateBean();



        switch (currRoleNum)
        {
            case 0:
                for(int a=0;a<speakerNames.size();a++) {
                    DelegateBean delegateBean1 = new DelegateBean();
                    delegateBean1.setDelegateName(speakerNames.get(a));
                    delegateBean1.setDelegateDepartment(departmentNames.get(a));
                    delegateBean1.setDelegateJob(jobName.get(a%3));
                    delegateBean1.setRole(Constant.KEYNOTE_SPEAKER);
                    ArrayList agendaIndex1 = new ArrayList();
                    agendaIndex1.add(a+1);
                    delegateBean1.setDelegateAgendaList(agendaIndex1);
                    delegateBean1.setDelegateDetailInfo("我是好人");
                    delegateBeans.add(delegateBean1);
                }
                break;
            case 1:
                for(int a=0;a<hostNames.size();a++) {
                    DelegateBean delegateBean1 = new DelegateBean();
                    delegateBean1.setDelegateName(hostNames.get(a));
                    delegateBean1.setDelegateDepartment(departmentNames.get(a));
                    delegateBean1.setDelegateJob(jobName.get(a%3));
                    delegateBean1.setRole(Constant.HOST);
                    ArrayList agendaIndex1 = new ArrayList();
                    delegateBean1.setDelegateAgendaList(agendaIndex1);
                    delegateBean1.setDelegateDetailInfo("他是好人");
                    delegateBeans.add(delegateBean1);

                }
                break;
            case 2:
                for(int a=0;a<otherDelegateNames.size();a++) {
                    DelegateBean delegateBean1 = new DelegateBean();
                    delegateBean1.setDelegateName(otherDelegateNames.get(a));
                    delegateBean1.setDelegateDepartment(departmentNames.get(a%4));
                    delegateBean1.setDelegateJob(jobName.get(a%3));
                    delegateBean1.setRole(Constant.NORMAL_DELEGATE);
                    delegateBean1.setDelegateDetailInfo("他不是好人");
                    delegateBeans.add(delegateBean1);

                }
                break;


        }
        delegateBean=delegateBeans.get(delegateNamePos);
        return delegateBean;
    }
    public static Vote getVoteDetailByIndex(int index){
        Vote vote = new Vote();
        switch (index){
            case 1:
                vote.setVoteID("1");
                vote.setVoteTitle("议程1投票标题");
                vote.setVoteNumberNeeded(1);
                vote.setVoteQuestion("议程1投票问题");
                List<String> options1 = new ArrayList<String>();
                options1.add("议程1:选项1");
                options1.add("议程1:选项2");
                options1.add("议程1:选项3");
                vote.setVoteOptionsList(options1);
                break;

            case 2:
                vote.setVoteID("2");
                vote.setVoteTitle("议程2投票标题");
                vote.setVoteNumberNeeded(2);
                vote.setVoteQuestion("议程2投票问题");
                List<String> options2 = new ArrayList<String>();
                options2.add("议程2:选项1");
                options2.add("议程2:选项2");
                options2.add("议程2:选项3");
                options2.add("议程2:选项4");
                vote.setVoteOptionsList(options2);
                break;

            case 3:
                vote.setVoteID("3");
                vote.setVoteTitle("议程3投票标题");
                vote.setVoteNumberNeeded(1);
                vote.setVoteQuestion("议程3投票问题");
                List<String> options3 = new ArrayList<String>();
                options3.add("议程3:选项1");
                options3.add("议程3:选项2");
                options3.add("议程3:选项3");
                options3.add("议程3:选项4");
                vote.setVoteOptionsList(options3);
                break;
        }

        return vote;
    }

    public static List<DelegateBean> getDelegates(){
        List<DelegateBean> mUsers = new ArrayList<>();
        DelegateBean d1 = new DelegateBean();
        d1.setDelegateId(1);
        d1.setDelegateName("张三张三");
        d1.setDelegateDetailInfo("我是张三张三啊");
        d1.setDelegateDepartment("技术部");
        d1.setDelegateJob("android开发");
        d1.setRole(2);
        ArrayList<Integer> arrayList1 = new ArrayList<>();
        arrayList1.add(1);
        arrayList1.add(2);
        d1.setDelegateAgendaList(arrayList1);

        DelegateBean d2 = new DelegateBean();
        d2.setDelegateId(2);
        d2.setDelegateName("王麻子");
        d2.setDelegateDetailInfo("我叫王麻子，但是我脸上木有麻子！");
        d2.setDelegateDepartment("技术部");
        d2.setDelegateJob("Java开发");
        d2.setRole(2);
        ArrayList<Integer> arrayList2 = new ArrayList<>();
        arrayList2.add(1);
        arrayList2.add(3);
        d2.setDelegateAgendaList(arrayList2);

        DelegateBean d3 = new DelegateBean();
        d3.setDelegateId(3);
        d3.setDelegateName("临到");
        d3.setDelegateDetailInfo("我叫临到，欢迎光临您的来到。");
        d3.setDelegateDepartment("技术部");
        d3.setDelegateJob("iOS开发");
        d3.setRole(2);
        ArrayList<Integer> arrayList3 = new ArrayList<>();
        arrayList3.add(2);
        arrayList3.add(3);
        d3.setDelegateAgendaList(arrayList3);

        DelegateBean d4 = new DelegateBean();
        d4.setDelegateId(4);
        d4.setDelegateName("李四");
        d4.setDelegateDepartment("技术部");
        d4.setDelegateDetailInfo("我叫李四，就是传说中的李四。");
        d4.setDelegateJob("c++开发");
        d4.setRole(2);
        ArrayList<Integer> arrayList4 = new ArrayList<>();
        arrayList4.add(1);
        d4.setDelegateAgendaList(arrayList4);

        DelegateBean d5 = new DelegateBean();
        d5.setDelegateId(5);
        d5.setDelegateName("王五");
        d5.setDelegateDetailInfo("我叫王五，就是传说中的王五。");
        d5.setDelegateDepartment("技术部");
        d5.setDelegateJob("c开发");
        d5.setRole(2);
        ArrayList<Integer> arrayList5 = new ArrayList<>();
        arrayList5.add(1);
        arrayList5.add(2);
        d5.setDelegateAgendaList(arrayList5);

        DelegateBean d6 = new DelegateBean();
        d6.setDelegateId(6);
        d6.setDelegateName("王小二");
        d6.setDelegateDetailInfo("我叫王小二，不是王二小。");
        d6.setDelegateDepartment("技术部");
        d6.setDelegateJob("c#开发");
        d6.setRole(2);
        ArrayList<Integer> arrayList6 = new ArrayList<>();
        arrayList6.add(2);
        d6.setDelegateAgendaList(arrayList6);

        DelegateBean d7 = new DelegateBean();
        d7.setDelegateId(7);
        d7.setDelegateName("赵烈");
        d7.setDelegateDetailInfo("我叫赵烈，火气不烈。");
        d7.setDelegateDepartment("后勤部");
        d7.setDelegateJob("技术支持");
        d7.setRole(2);
        ArrayList<Integer> arrayList7 = new ArrayList<>();
        arrayList7.add(1);
        d7.setDelegateAgendaList(arrayList7);

        DelegateBean d8 = new DelegateBean();
        d8.setDelegateId(8);
        d8.setDelegateName("饼子");
        d8.setDelegateDetailInfo("我叫饼子，但是不能吃。");
        d8.setDelegateDepartment("服务部");
        d8.setDelegateJob("客服");
        d8.setRole(2);

        DelegateBean d9 = new DelegateBean();
        d9.setDelegateId(9);
        d9.setDelegateName("胡子");
        d9.setDelegateDetailInfo("我叫胡子，就是脸上的那个。");
        d9.setDelegateDepartment("管理部");
        d9.setDelegateJob("经理");
        d9.setRole(1);

        DelegateBean d10 = new DelegateBean();
        d10.setDelegateId(10);
        d10.setDelegateName("阿毛");
        d10.setDelegateDetailInfo("我叫阿毛，头发不多。");
        d10.setDelegateDepartment("管理部");
        d10.setDelegateJob("助理");
        d10.setRole(3);

        mUsers.add(d1);
        mUsers.add(d2);
        mUsers.add(d3);
        mUsers.add(d4);
        mUsers.add(d5);
        mUsers.add(d6);
        mUsers.add(d7);
        mUsers.add(d8);
        mUsers.add(d9);
        mUsers.add(d10);
        return mUsers;
    }

    public static MeetingInfo getMeetingInfo(){
        MeetingInfo meetingInfo = new MeetingInfo("这是会议名称","2016.09.08   10:00","120",15,10,5);
        return meetingInfo;
    }

    public static List<MeetingBean> getMeetings(){
        List<MeetingBean> meetings = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            MeetingBean meetingBean = new MeetingBean(i,"会议"+i,"1"+i+":00",0);
            meetings.add(meetingBean);
        }
        return meetings;
    }
}