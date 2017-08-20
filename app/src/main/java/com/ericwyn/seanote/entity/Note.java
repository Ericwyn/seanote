package com.ericwyn.seanote.entity;

/**
 * 笔记 .md 文件的映射类
 * 文件名统一以: title-createTime.md 来命名
 * 通过文件名获取title和创建时间,
 * 创建时间用currentTime来表示，然后这个也作为唯一的文件id映射文件的内容
 *
 * 而后获取文件内容 words，以及最近的修改时间
 *
 * 关于文件的id?应该是存放在id.xml里面吧。
 *
 * Created by ericwyn on 17-8-20.
 */

public class Note {
    private String fileName;
    private String title;
    private String words;
    private String createTime;
    private String id;

//    private

}
