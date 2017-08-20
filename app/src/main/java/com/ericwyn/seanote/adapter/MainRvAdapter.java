package com.ericwyn.seanote.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ericwyn.seanote.entity.Note;
import com.ericwyn.seanote.server.DataServer;

/**
 * 主页面的RecyclerView
 * 使用了BRVAH 框架
 * 参考：http://www.jianshu.com/p/b343fcff51b0
 *
 * Created by ericwyn on 17-8-20.
 */

public class MainRvAdapter extends BaseQuickAdapter<Note, BaseViewHolder> {
    public MainRvAdapter(){
        //绑定数据源和视图
        super(android.R.layout.activity_list_item, DataServer.loadData());
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Note note) {
        //绑定具体数据的单个属性与视图里面的具体项
    }
}
