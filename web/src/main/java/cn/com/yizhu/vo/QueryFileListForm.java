package cn.com.yizhu.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 查询文件列表请求实体
 */
@Api(description = "查询文件列表请求VO")
public class QueryFileListForm {

    @ApiModelProperty(value = "文件路径", required = true)
    @NotNull(message = "文件路径不能为空！")
    private String path;

    @ApiModelProperty(value = "文件状态", required = true, example = "0")
    @NotNull(message = "文件状态不能为空")
    private Integer status;

    @ApiModelProperty(value = "文件类型", example = "0")
    private Integer fileType;

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

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }
}
