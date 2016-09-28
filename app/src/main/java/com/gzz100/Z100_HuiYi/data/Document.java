package com.gzz100.Z100_HuiYi.data;

import android.content.Context;
import android.widget.ImageView;

import com.gzz100.Z100_HuiYi.R;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

/**
 * Created by XieQXiong on 2016/8/24.
 */
public class Document  extends BaseBean {
    private int documentID;
    private int documentAgendaIndex;
    private int documentIndex;
    private String documentName;
    private int documentSize;
    private String documentSpeaker;

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

    public String getDocumentSpeaker() {
        return documentSpeaker;
    }

    public void setDocumentSpeaker(String documentSpeaker) {
        this.documentSpeaker = documentSpeaker;
    }

    public void setImageForFile(Context context, ImageView imageView){
        if (!this.documentName.isEmpty() && this.documentName.contains(".txt")){
            //"http://p3.so.qhmsg.com/bdr/_240_/t0146723952b5d73a1d.png"
            Picasso.with(context).load(R.mipmap.txt).into(imageView);
        }else if (!this.documentName.isEmpty() &&
                (this.documentName.contains(".doc") || this.documentName.contains(".docx"))){
            //"http://p1.so.qhmsg.com/bdr/_240_/t01b16d40e4a51d6241.png"
            Picasso.with(context).load(R.mipmap.doc).into(imageView);
        }else if (!this.documentName.isEmpty() && this.documentName.contains(".xlsx")){
            //"http://p0.so.qhmsg.com/bdr/_240_/t01d509fc4c1cb60a9a.png"
            Picasso.with(context).load(R.mipmap.xlsx).into(imageView);
        }
    }


}
