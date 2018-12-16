package cn.com.yizhu.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "fillMeta")
public class FileMeta {

    @Field
    private Long version;

    @Field
    private Long size;

    @Field
    private String key;

    @Field
    private String suffix;

    @Field
    private String md5;

    public FileMeta(){}

    public FileMeta(Long version, Long size, String key, String suffix, String md5) {
        this.version = version;
        this.size = size;
        this.key = key;
        this.suffix = suffix;
        this.md5 = md5;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
