package cn.com.yizhu.service;

import cn.com.yizhu.common.exception.BizException;
import cn.com.yizhu.dto.FileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    FileDTO createRootDir(String name, String tag) throws BizException;

    /**
     * 上传文件
     * @param file
     * @param parentId
     * @param name
     * @param tag
     * @return
     * @throws BizException
     */
    FileDTO createFile(MultipartFile file, Long parentId, String name, String tag) throws BizException;

    /**
     * 创建文件夹
     * @param parentId
     * @param name
     * @param tag
     * @return
     * @throws BizException
     */
    FileDTO createFolder(Long parentId, String name, String tag) throws BizException;

    /**
     * 获取文件列表
     * @param path
     * @param status
     * @param type
     * @return
     */
    List<FileDTO> getFileList(String path, Integer status, Integer type);

    /**
     * 复制文件
     * @param sourceIds
     * @param targetId
     * @return
     * @throws BizException
     */
    List<FileDTO> copyFile(List<Long> sourceIds, Long targetId) throws BizException;

    /**
     * 移动或复制时获取除自身外的文件列表
     * @param id
     * @param path
     * @return
     */
    List<FileDTO> getCanCopyToFileList(Long id, String path);

    /**
     * 移动文件
     * @param sourceIds
     * @param targetId
     * @return
     * @throws BizException
     */
    List<FileDTO> moveFile(List<Long> sourceIds, Long targetId) throws BizException;

    /**
     * 删除文件
     * @param sourceIds
     * @param targetId
     * @throws BizException
     * @return
     */
    List<FileDTO> deleteFile(List<Long> sourceIds, Long targetId) throws BizException;

    /**
     * 彻底删除文件
     * @param sourceIds
     * @param targetId
     * @return
     * @throws BizException
     */
    List<FileDTO> cleanFile(List<Long> sourceIds, Long targetId) throws BizException;
}
