package cn.com.yizhu.controller;

import cn.com.yizhu.common.vo.ResponseVO;
import cn.com.yizhu.contants.Contants;
import cn.com.yizhu.dto.FileDTO;
import cn.com.yizhu.service.FileService;
import cn.com.yizhu.vo.CreateFileForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseVO<FileDTO> createFile(
            @ApiParam(value = "文件") MultipartFile file,
            @ModelAttribute CreateFileForm createFileForm) throws Exception {
        return ResponseVO.successResponseVO(
                fileService.createFile(
                        file,
                        createFileForm.getPath(),
                        createFileForm.getFileName(),
                        createFileForm.getFileTypeEnum()
                )
        );
    }


}
