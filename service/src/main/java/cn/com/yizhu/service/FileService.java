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
     * @param targetIds
     * @throws BizException
     * @return
     */
    List<FileDTO> deleteFile(List<Long> targetIds) throws BizException;

    /**
     * 彻底删除文件
     * @param targetIds
     * @return
     * @throws BizException
     */
    List<FileDTO> cleanFile(List<Long> targetIds) throws BizException;

    /**
     * 重命名文件
     * @param targetId
     * @return
     * @throws BizException
     */
    FileDTO renameFile(Long targetId) throws BizException;

    /**
     * 清空回收站
     * @return
     */
    Boolean cleanAll();

    /**
     * 下载
     * @param targetId
     */
    void download(Long targetId);

    /**
     * 批量打包下载
     * @param targetIds
     */
    void batchDownload(List<Long> targetIds);

    /**
     * 图片预览
     * @param key
     * @param suffix
     */
    void downPicuture(String key, String suffix);

    /**
     * 分类查看文件
     * @param category
     * @return
     */
    List<FileDTO> findByCategory(Integer category);

    /**
     * 文件名模糊查询文件
     * @param fileName
     * @return
     */
    List<FileDTO> findFilesLikeName(String fileName);
}
