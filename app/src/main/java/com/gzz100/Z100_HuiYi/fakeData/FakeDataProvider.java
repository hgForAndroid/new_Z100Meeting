package com.gzz100.Z100_HuiYi.fakeData;

import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.data.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieQXiong on 2016/8/29.
 */
public class FakeDataProvider {
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
                    document.setFileName("agenda_one_fileName" + i);
                    document.setFileSize((12 + i) + "kb");
                    document.setFileSpeaker("张三");
                    documents.add(document);
                }

                break;
            case 2:
                for (int i = 1; i < 5; i++) {
                    Document document = new Document();
                    document.setAgendaIndex(1 + "");
                    document.setFileURL("agenda_two_address" + i);
                    document.setFileIndex(i + "");
                    document.setFileName("agenda_two_fileName" + i);
                    document.setFileSize((16 + i) + "kb");
                    document.setFileSpeaker("李四");
                    documents.add(document);
                }
                break;

            case 3:
                for (int i = 1; i < 5; i++) {
                    Document document = new Document();
                    document.setAgendaIndex(1 + "");
                    document.setFileURL("agenda_three_address" + i);
                    document.setFileIndex(i + "");
                    document.setFileName("agenda_three_fileName" + i);
                    document.setFileSize((14 + i) + "kb");
                    document.setFileSpeaker("王五");
                    documents.add(document);
                }
                break;
        }
        return documents;
    }
}