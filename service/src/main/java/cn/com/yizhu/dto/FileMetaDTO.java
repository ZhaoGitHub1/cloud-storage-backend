package cn.com.yizhu.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

/**
 * 文件元信息
 */
@Api(description = "文件元信息")
public class FileMetaDTO {

    @ApiModelProperty(value = "来源id")
    private Long refId;

    @ApiModelProperty(value = "版本号")
    private Long version;

    @ApiModelProperty(value = "文件大小，单位byte")
    private Long size;

    @ApiModelProperty(value = "标签/备注")
    private String tag;

    @ApiModelProperty(value = "文件key")
    private String key;

    @ApiModelProperty(value = "文件后缀")
    private String suffix;

    @ApiModelProperty(value = "文件的md5")
    private String md5;

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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
