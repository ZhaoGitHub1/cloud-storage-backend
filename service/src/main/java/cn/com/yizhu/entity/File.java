package cn.com.yizhu.entity;

import cn.com.yizhu.common.annotation.AutoIncKey;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "file")
public class File {

    @Id
    @AutoIncKey
    private long id;

    @Field
    private String name;

    @Field
    private Integer type;

    @Field
    private String path;

    @Field
    private Integer status;

    @Field
    private Long refId;

    @Field
    private String tag;

    @Field
    private Long createId;

    @Field
    private Date createTime;

    @Field
    private Long updateId;

    @Field
    private Date updateTime;

    @Field
    private FileMeta fileMeta;

    public File(){}

    public File(String name, Integer type, String path, Integer status, Long refId, String tag, Long createId, Date createTime) {
        this.name = name;
        this.type = type;
        this.path = path;
        this.status = status;
        this.refId = refId;
        this.tag = tag;
        this.createId = createId;
        this.createTime = createTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getCreateId() {
        return createId;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Long updateId) {
        this.updateId = updateId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public FileMeta getFileMeta() {
        return fileMeta;
    }

    public void setFileMeta(FileMeta fileMeta) {
        this.fileMeta = fileMeta;
    }
}
