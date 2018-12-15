package cn.com.yizhu.controller;

import cn.com.yizhu.common.vo.ResponseVO;
import cn.com.yizhu.contants.Contants;
import cn.com.yizhu.dto.FileDTO;
import cn.com.yizhu.service.FileService;
import cn.com.yizhu.vo.CreateFileForm;
import cn.com.yizhu.vo.QueryFileListForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

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
    public ResponseVO<FileDTO> createFile(@ApiParam(value = "文件", name = "file") MultipartFile file,
                                          @ModelAttribute CreateFileForm createFileForm) throws Exception {
        return ResponseVO.successResponseVO(
                fileService.createFile(
                        file,
                        createFileForm.getPath(),
                        createFileForm.getFileName(),
                        createFileForm.getFileTypeEnum(),
                        createFileForm.getTag()
                )
        );
    }

    @ApiOperation(value = "获取文件列表")
    @GetMapping("/getFileList")
    @Valid
    public ResponseVO<List<FileDTO>> getFileList(@ModelAttribute QueryFileListForm queryFileListForm){
        return ResponseVO.successResponseVO(
                fileService.getFileList(
                        queryFileListForm.getPath(),
                        queryFileListForm.getStatus(),
                        queryFileListForm.getFileType()
                )
        );
    }


}
