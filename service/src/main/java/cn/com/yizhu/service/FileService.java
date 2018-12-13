package cn.com.yizhu.service;

import cn.com.yizhu.common.exception.BizException;
import cn.com.yizhu.contants.FileTypeEnum;
import cn.com.yizhu.dto.FileDTO;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    /**
     * 上传文件或创建文件夹
     * @param file 待上传文件
     * @param path 文件全路径
     * @param fileName 文件夹名称或文件tag
     * @param fileType 文件类型
     * @return
     * @throws Exception
     */
    FileDTO createFile(MultipartFile file, String path, String fileName, FileTypeEnum fileType) throws BizException;
}
