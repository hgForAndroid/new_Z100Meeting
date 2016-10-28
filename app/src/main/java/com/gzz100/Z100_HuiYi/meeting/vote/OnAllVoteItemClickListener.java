package com.gzz100.Z100_HuiYi.meeting.vote;

import android.view.View;

/**
 * Created by Lee on 2016/10/24.
 */

public interface OnAllVoteItemClickListener {
    void onVoteStartStopButtonClick(View view, int position);
    void onCheckResultButtonClick(View view, int position);
}
