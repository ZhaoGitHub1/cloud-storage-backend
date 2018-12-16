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
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/createFolder")
    @ApiOperation(value = "创建文件夹")
    @Valid
    public ResponseVO<FileDTO> createFolder(@ModelAttribute CreateFileForm createFileForm) throws Exception {
        return ResponseVO.successResponseVO(
                fileService.createFolder(
                        createFileForm.getParentId(),
                        createFileForm.getName(),
                        createFileForm.getTag()
                )
        );
    }
    @PostMapping("/createFile")
    @ApiOperation(value = "上传文件")
    @Valid
    public ResponseVO<FileDTO> createFile(@ApiParam(value = "文件", name = "file", required = true) MultipartFile file,
                                          @ModelAttribute CreateFileForm createFileForm) throws Exception {
        return ResponseVO.successResponseVO(
                fileService.createFile(
                        file,
                        createFileForm.getParentId(),
                        createFileForm.getName(),
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

    @ApiOperation(value = "移动或复制时获取除自身和子文件夹外的文件列表")
    @GetMapping("/getCanCopyToFileList")
    @Valid
    public ResponseVO<List<FileDTO>> getCanCopyToFileList(@RequestParam(name = "id") Long id,
                                                       @RequestParam(name = "path") String path){
        return ResponseVO.successResponseVO(
                fileService.getCanCopyToFileList(id, path)
        );
    }

    @ApiOperation("复制文件")
    @GetMapping("/copyFile")
    public ResponseVO<List<FileDTO>> copyFile(@RequestParam(name = "sourceIds") List<Long> sourceIds,
                                        @RequestParam(name = "targetId") Long targetId) throws Exception {
        return ResponseVO.successResponseVO(
                fileService.copyFile(sourceIds, targetId)
        );
    }

    @ApiOperation("移动文件")
    @GetMapping("/moveFile")
    public ResponseVO<List<FileDTO>> moveFile(@RequestParam(name = "sourceIds") List<Long> sourceIds,
                                              @RequestParam(name = "targetId") Long targetId) throws Exception {
        return ResponseVO.successResponseVO(
                fileService.moveFile(sourceIds, targetId)
        );
    }

    @ApiOperation("删除文件")
    @GetMapping("/deleteFile")
    public ResponseVO<List<FileDTO>> deleteFile(@RequestParam(name = "sourceIds") List<Long> sourceIds,
                                              @RequestParam(name = "targetId") Long targetId) throws Exception {
        return ResponseVO.successResponseVO(
                fileService.deleteFile(sourceIds, targetId)
        );
    }

    @ApiOperation("彻底删除文件")
    @GetMapping("/cleanFile")
    public ResponseVO<List<FileDTO>> cleanFile(@RequestParam(name = "sourceIds") List<Long> sourceIds,
                                              @RequestParam(name = "targetId") Long targetId) throws Exception {
        return ResponseVO.successResponseVO(
                fileService.cleanFile(sourceIds, targetId)
        );
    }
}
