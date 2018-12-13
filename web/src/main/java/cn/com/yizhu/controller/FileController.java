package cn.com.yizhu.controller;

import cn.com.yizhu.common.vo.ResponseVO;
import cn.com.yizhu.contants.Contants;
import cn.com.yizhu.dto.FileDTO;
import cn.com.yizhu.service.FileService;
import cn.com.yizhu.vo.CreateFileForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 文件控制器
 */
@Api(description = "文件管理", tags = "File.Manage")
@RestController(Contants.WEB_ROUTE + "/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/createFile")
    @ApiOperation(value = "上传文件或创建文件夹")
    @Valid
    public ResponseVO<FileDTO> createFile(@ModelAttribute CreateFileForm createFileForm) throws Exception {
        return ResponseVO.successResponseVO(
                fileService.createFile(
                        createFileForm.getFile(),
                        createFileForm.getPath(),
                        createFileForm.getFileName(),
                        createFileForm.getFileTypeEnum()
                )
        );
    }


}
