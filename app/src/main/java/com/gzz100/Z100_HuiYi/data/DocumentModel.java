package com.gzz100.Z100_HuiYi.data;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.file.FileOperate;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by XieQXiong on 2016/8/24.
 */
public class DocumentModel extends BaseBean {
    private int documentID;
    private int documentAgendaIndex;
    private int documentIndex;
    private String documentName;
    private int documentSize;
    private String documentPath;

    public int getDocumentID() {
        return documentID;
    }

    public void setDocumentID(int documentID) {
        this.documentID = documentID;
    }

    public int getDocumentAgendaIndex() {
        return documentAgendaIndex;
    }

    public void setDocumentAgendaIndex(int documentAgendaIndex) {
        this.documentAgendaIndex = documentAgendaIndex;
    }

    public int getDocumentIndex() {
        return documentIndex;
    }

    public void setDocumentIndex(int documentIndex) {
        this.documentIndex = documentIndex;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public int getDocumentSize() {
        return documentSize;
    }

    public void setDocumentSize(int documentSize) {
        this.documentSize = documentSize;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public void setImageForFile(Context context, ImageView imageView){
        if (!this.documentName.isEmpty() && this.documentName.contains(".txt")){
            Picasso.with(context).load(R.mipmap.txt).into(imageView);
        }else if (!this.documentName.isEmpty() &&
                (this.documentName.contains(".doc") || this.documentName.contains(".docx"))){
            Picasso.with(context).load(R.mipmap.doc).into(imageView);
        }else if (!this.documentName.isEmpty() && this.documentName.contains(".xlsx")){
            Picasso.with(context).load(R.mipmap.xlsx).into(imageView);
        }
    }

    public void setDocumentSpeaker(Context context, TextView textView){
        int documentAgendaIndex = this.documentAgendaIndex;
        List<AgendaModel> agendaModels = FileOperate.getInstance(context).queryAgendaList();
        for (AgendaModel agenda : agendaModels) {
            if (agenda.getAgendaIndex() == documentAgendaIndex){
                textView.setText(agenda.getAgendaSpeaker());
            }
        }
    }


}
