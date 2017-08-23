package com.ericwyn.seanote.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ericwyn.seanote.R;
import com.ericwyn.seanote.entity.Note;
import com.ericwyn.seanote.server.DataServer;

import java.util.List;

/**
 * 主页面的RecyclerView
 * 使用了BRVAH 框架
 * 参考：http://www.jianshu.com/p/b343fcff51b0
 *
 * Created by ericwyn on 17-8-20.
 */

public class MainRvAdapter extends BaseQuickAdapter<Note, BaseViewHolder> {
    public MainRvAdapter(List<Note> data){
        //绑定数据源和视图
        super(R.layout.note_rv_item_main,data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Note note) {
        //绑定具体数据的单个属性与视图里面的具体项
        baseViewHolder.setText(R.id.tv_title_mainAct_rv_item,note.getTitle())
                .setText(R.id.tv_word_mainAct_rv_item,DataServer.getWordShowInCard(note.getWords()))
                .setText(R.id.tv_createTime_mainAct_rv_item,note.getCreateTime())
                .setText(R.id.tv_notedir_mainAct_rv_item,"TestDir")
                .linkify(R.id.tv_title_mainAct_rv_item);
    }

}
