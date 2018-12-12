package cn.com.yizhu.vo;

import cn.com.yizhu.contants.FileTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

/**
 * 上传文件请求实体
 */
@Api(description = "上传文件请求VO")
public class CreateFileForm {

    @ApiModelProperty(value = "文件路径", required = true)
    private String path;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "文件类型", required = true)
    private FileTypeEnum fileTypeEnum;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FileTypeEnum getFileTypeEnum() {
        return fileTypeEnum;
    }

    public void setFileTypeEnum(FileTypeEnum fileTypeEnum) {
        this.fileTypeEnum = fileTypeEnum;
    }
}
