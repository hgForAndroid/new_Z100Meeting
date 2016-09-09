package com.gzz100.Z100_HuiYi.fakeData;

import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.data.DelegateBean;
import com.gzz100.Z100_HuiYi.data.Document;
import com.gzz100.Z100_HuiYi.data.Vote;

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
        agenda1.setAgendaIndex(1 + "");
        agenda1.setAgendaName("这是关于议程1的一切标题");
        agenda1.setAgendaTime("30:00");
        agenda1.setAgendaSpeaker("老大");
        Agenda agenda2 = new Agenda();
        agenda2.setAgendaIndex(2 + "");
        agenda2.setAgendaName("关于这个议程2的事有什么");
        agenda2.setAgendaTime("32:00");
        agenda2.setAgendaSpeaker("老二");
        Agenda agenda3 = new Agenda();
        agenda3.setAgendaIndex(3 + "");
        agenda3.setAgendaName("马上就到议程3，还有什么事要处理");
        agenda3.setAgendaTime("33:00");
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
                    document.setAgendaIndex(1 + "");
                    document.setFileURL("agenda_one_address" + i);
                    document.setFileIndex(i + "");
                    document.setFileName("agenda_one_fileName.txt" + i);
                    document.setFileSize((12 + i) + "kb");
                    document.setFileSpeaker("张三");
                    documents.add(document);
                }

                break;
            case 2:
                for (int i = 1; i < 5; i++) {
                    Document document = new Document();
                    document.setAgendaIndex(2 + "");
                    document.setFileURL("agenda_two_address" + i);
                    document.setFileIndex(i + "");
                    document.setFileName("agenda_two_fileName.doc" + i);
                    document.setFileSize((16 + i) + "kb");
                    document.setFileSpeaker("李四");
                    documents.add(document);
                }
                break;

            case 3:
                for (int i = 1; i < 5; i++) {
                    Document document = new Document();
                    document.setAgendaIndex(3 + "");
                    document.setFileURL("agenda_three_address" + i);
                    document.setFileIndex(i + "");
                    document.setFileName("agenda_three_fileName.xlsx" + i);
                    document.setFileSize((14 + i) + "kb");
                    document.setFileSpeaker("王五");
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
        otherDelegateNames.add("张1");
        otherDelegateNames.add("李2");
        otherDelegateNames.add("王3");
        otherDelegateNames.add("张4");
        otherDelegateNames.add("李5");
        otherDelegateNames.add("王6");



        switch (rolePos)
        {
            case 0:
                for(int a=0;a<speakerNames.size();a++) {
                    DelegateBean delegateBean1 = new DelegateBean();
                    delegateBean1.setDelegateName(speakerNames.get(a));
                    delegateBean1.setDelegateDepartment(departmentNames.get(a));
                    delegateBean1.setRole("主讲人");
                    ArrayList agendaIndex1 = new ArrayList();
                    agendaIndex1.add(a+1);
                    delegateBean1.setDelegateAgendaList(agendaIndex1);
                    delegateBean1.setDelegateDetailInfo("我是好人");
                    delegateBeans.add(delegateBean1);
                    currRoleNum=0;
                }
                break;
            case 1:
                for(int a=0;a<hostNames.size();a++) {
                    DelegateBean delegateBean1 = new DelegateBean();
                    delegateBean1.setDelegateName(hostNames.get(a));
                    delegateBean1.setDelegateDepartment(departmentNames.get(a));
                    delegateBean1.setRole("主持人");
                    ArrayList agendaIndex1 = new ArrayList();
                    delegateBean1.setDelegateAgendaList(agendaIndex1);
                    delegateBean1.setDelegateDetailInfo("他是好人");
                    delegateBeans.add(delegateBean1);
                    currRoleNum=1;
                }
                break;
            case 2:
                for(int a=0;a<otherDelegateNames.size();a++) {
                    DelegateBean delegateBean1 = new DelegateBean();
                    delegateBean1.setDelegateName(otherDelegateNames.get(a));
                    delegateBean1.setDelegateDepartment(departmentNames.get(a%4));
                    delegateBean1.setRole("参会代表");
                    delegateBean1.setDelegateDetailInfo("他不是好人");
                    delegateBeans.add(delegateBean1);
                    currRoleNum=2;
                }
                break;


        }
        return delegateBeans;
    }
    public static DelegateBean getDelegateDetailByNamePos(int namePos){
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
        otherDelegateNames.add("张1");
        otherDelegateNames.add("李2");
        otherDelegateNames.add("王3");
        otherDelegateNames.add("张4");
        otherDelegateNames.add("李5");
        otherDelegateNames.add("王6");

        DelegateBean delegateBean=new DelegateBean();



        switch (currRoleNum)
        {
            case 0:
                for(int a=0;a<speakerNames.size();a++) {
                    DelegateBean delegateBean1 = new DelegateBean();
                    delegateBean1.setDelegateName(speakerNames.get(a));
                    delegateBean1.setDelegateDepartment(departmentNames.get(a));
                    delegateBean1.setDelegateJob(jobName.get(a));
                    delegateBean1.setRole("主讲人");
                    ArrayList agendaIndex1 = new ArrayList();
                    agendaIndex1.add(a+1);
                    delegateBean1.setDelegateAgendaList(agendaIndex1);
                    delegateBean1.setDelegateDetailInfo("我是好人");
                    delegateBeans.add(delegateBean1);
                    delegateBean=delegateBeans.get(namePos);
                }
                break;
            case 1:
                for(int a=0;a<hostNames.size();a++) {
                    DelegateBean delegateBean1 = new DelegateBean();
                    delegateBean1.setDelegateName(hostNames.get(a));
                    delegateBean1.setDelegateDepartment(departmentNames.get(a));
                    delegateBean1.setDelegateJob(jobName.get(a));
                    delegateBean1.setRole("主持人");
                    ArrayList agendaIndex1 = new ArrayList();
                    delegateBean1.setDelegateAgendaList(agendaIndex1);
                    delegateBean1.setDelegateDetailInfo("他是好人");
                    delegateBeans.add(delegateBean1);
                    delegateBean=delegateBeans.get(namePos);
                }
                break;
            case 2:
                for(int a=0;a<otherDelegateNames.size();a++) {
                    DelegateBean delegateBean1 = new DelegateBean();
                    delegateBean1.setDelegateName(otherDelegateNames.get(a));
                    delegateBean1.setDelegateDepartment(departmentNames.get(a%4));
                    delegateBean1.setDelegateJob(jobName.get(a));
                    delegateBean1.setRole("参会代表");
                    delegateBean1.setDelegateDetailInfo("他不是好人");
                    delegateBeans.add(delegateBean1);
                    delegateBean=delegateBeans.get(namePos);
                }
                break;


        }
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
}