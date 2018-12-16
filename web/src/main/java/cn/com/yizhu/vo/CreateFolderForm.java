package cn.com.yizhu.vo;

import cn.com.yizhu.contants.FileTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 创建文件夹请求实体
 */
@Api(description = "创建文件夹请求VO")
public class CreateFolderForm {

    @ApiModelProperty(value = "父级文件夹id", required = true)
    @NotNull(message = "父级文件夹id不能为空！")
    private Long parentId;

    @ApiModelProperty(value = "文件夹名称")
    private String name;

    @ApiModelProperty(value = "标签、备注")
    private String tag;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
