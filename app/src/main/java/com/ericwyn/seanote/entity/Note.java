package com.ericwyn.seanote.entity;

import com.ericwyn.seanote.Util.FileUtil;

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
    private String filePath;
//    private

    public Note(){

    }

    public Note(String fileName,String words){
        this.fileName=fileName;
        this.words=words;
        this.title= FileUtil.getNoteName(fileName);
        this.createTime=FileUtil.getNoteCreateTime(fileName);
        this.id=FileUtil.getNoteFileId(fileName);
    }


    public String getFileName() {
        return fileName;
    }

    public String getTitle() {
        return title;
    }

    public String getWords() {
        return words;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getId() {
        return id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
