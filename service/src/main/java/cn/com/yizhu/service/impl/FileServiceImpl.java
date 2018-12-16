package cn.com.yizhu.service.impl;

import cn.com.yizhu.common.exception.BizException;
import cn.com.yizhu.contants.FileStatusEnum;
import cn.com.yizhu.contants.FileTypeEnum;
import cn.com.yizhu.dto.FileDTO;
import cn.com.yizhu.dto.FileMetaDTO;
import cn.com.yizhu.dto.FileUploadResultDTO;
import cn.com.yizhu.entity.File;
import cn.com.yizhu.entity.FileMeta;
import cn.com.yizhu.repository.FileRepository;
import cn.com.yizhu.service.FileService;
import cn.com.yizhu.utils.DateUtils;
import cn.com.yizhu.utils.FileUploadUtil;
import cn.com.yizhu.utils.RedisLockUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 文件业务逻辑处理类
 */
@Service
public class FileServiceImpl implements FileService {

    static final Logger LOG = LoggerFactory.getLogger(FileServiceImpl.class);
    static final Integer EXPIRE_TIME = 10;
    static final String PATH_SPLIT_SYMBOL = "/";
    static final String ROOT_PATH = "/root";
    static final Long DEFUALT_REF_ID= 0L;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional
    @Override
    public FileDTO createFolder(Long parentId, String name, String tag) throws BizException {
        if (0L == parentId){    // 创建跟目录
            return createRootDir(name, tag);
        }
        if (StringUtils.isEmpty(name)){
            throw new BizException("请填写文件夹名称！");
        }

        String requestValue = "cs:guest";   // TODO 获取登录信息
        File saved;
        File parentFolder = fileRepository.findById(parentId).get();
        String parentFolderPath = parentFolder.getPath();
        if (parentFolder.getType() != FileTypeEnum.DIR.getValue()){
            throw new BizException("请选择文件夹！");
        }
        try {
            if ( ! RedisLockUtils.getFileLock(redisTemplate, parentFolderPath,requestValue, EXPIRE_TIME)){
                throw new BizException("系统繁忙，请稍后重试！");
            }
            String currentFolderPath = currentFolderAppendSubPath(parentFolder);
            checkExistFile(currentFolderPath, name, FileTypeEnum.DIR.getValue());
            File file = new File(
                    name, FileTypeEnum.DIR.getValue(), currentFolderPath,
                    FileStatusEnum.NORMAL.getValue(), DEFUALT_REF_ID, tag, 0L, DateUtils.getTime()
            );
            saved = fileRepository.save(file);
        }finally {
            RedisLockUtils.releaseFileLock(redisTemplate, parentFolderPath, requestValue);
        }
        return copyFileToFileDTO(saved);
    }

    @Override
    public FileDTO createRootDir(String name, String tag) throws BizException {
        String userId = "001";  // TODO 获取userid
        name = Optional.ofNullable(name).orElse("user"+userId);
        tag = Optional.ofNullable(tag).orElse("系统初始化");
        try {
            checkExistFile(ROOT_PATH, name, FileTypeEnum.DIR.getValue());
        }catch (BizException e){
            throw new BizException("该用户已存在根目录！");
        }
        File file = new File(name, FileTypeEnum.DIR.getValue(), ROOT_PATH, FileStatusEnum.NORMAL.getValue(), 0L, tag, 0L, DateUtils.getTime());
        File saved = fileRepository.save(file);
        return copyFileToFileDTO(saved);
    }

    @Transactional
    @Override
    public FileDTO createFile(MultipartFile multipartFile, Long parentId, String name, String tag) throws BizException {
        Optional.ofNullable(multipartFile).orElseThrow(()->new BizException("请上传文件"));

        String requestValue = "cs:guest";   // TODO 获取登录信息
        File saved;
        String parentFolderPath = "cs:"+parentId;

        File parentFolder = fileRepository.findById(parentId).get();
        if (parentFolder.getType() != FileTypeEnum.DIR.getValue()){
            throw new BizException("请选择文件夹！");
        }
        parentFolderPath = parentFolder.getPath();
        try {
            if ( ! RedisLockUtils.getFileLock(redisTemplate, parentFolderPath,requestValue, EXPIRE_TIME)){
                new BizException("系统繁忙，请稍后重试！");
            }
            String originalFilename = multipartFile.getOriginalFilename();
            name = StringUtils.isEmpty(name) // 用户未填写fileName，则设置文件名为上传文件的名字
                    ? originalFilename.substring(0, originalFilename.lastIndexOf("."))
                    : name;
            String currentFolderPath = currentFolderAppendSubPath(parentFolder);
            checkExistFile(currentFolderPath, name, FileTypeEnum.FILE.getValue());

            File file = new File(
                    name, FileTypeEnum.FILE.getValue(), currentFolderPath, FileStatusEnum.NORMAL.getValue(),
                    DEFUALT_REF_ID, tag, 0L, DateUtils.getTime());

            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String fileMd5 = FileUploadUtil.getFileMd5(multipartFile.getInputStream()); // 获取上传文件的md5值
            FileMeta fileMeta = new FileMeta(System.currentTimeMillis(), multipartFile.getSize(), null, suffix, fileMd5);

            // 查询是否存在md5值相同文件
            List<File> existedFile = fileRepository.queryFile(fileMd5);

            if (existedFile != null && existedFile.size() > 0){
                fileMeta.setKey(existedFile.get(0).getFileMeta().getKey());
            }else {
                FileUploadResultDTO fileUploadResultDTO = FileUploadUtil.uploadFile(multipartFile);// 上传文件

                fileMeta.setKey(fileUploadResultDTO.getKey());
                fileMeta.setMd5(fileUploadResultDTO.getMd5()); // 覆盖输入流的md5
            }
            file.setFileMeta(fileMeta);
            saved = fileRepository.save(file);
        } catch (IOException e){
            LOG.error("创建文件失败，异常：",e);
            throw new BizException("创建文件失败！");
        } finally {
            RedisLockUtils.releaseFileLock(redisTemplate, parentFolderPath, requestValue);
        }
        return copyFileToFileDTO(saved);
    }

    @Transactional
    @Override
    public List<FileDTO> copyFile(List<Long> sourceIds, Long targetId) throws BizException{
        File targetFile = fileRepository.findById(targetId).get();
        if (targetFile == null
                || targetFile.getType() != FileTypeEnum.DIR.getValue()
                || targetFile.getStatus() != FileStatusEnum.NORMAL.getValue()){
            throw new BizException("目标地址不合法");
        }

        String targetFilePath = currentFolderAppendSubPath(targetFile);
        String requestValue = "cs:guest";
        List<File> saved;
        List<File> sourceFiles = new ArrayList<>();
        try {
            if ( ! RedisLockUtils.getFileLock(redisTemplate, targetFilePath, requestValue, EXPIRE_TIME)) {
                new BizException("系统繁忙，请稍后重试！");
            }

            Iterable<File> fileIterable = fileRepository.findAllById(sourceIds);
            final Integer[] count = {0};
            fileIterable.forEach(f -> count[0]++);
            if (count[0] != sourceIds.size()){
                throw new BizException("部分文件丢失，请刷新后重试！");
            }
            for (File file : fileIterable) {
                if (file == null || file.getStatus() != FileStatusEnum.NORMAL.getValue()) {
                    throw new BizException("来源文件不存在");
                }
                if (sourceIds.contains(targetId) || targetFile.getPath().contains(currentFolderAppendSubPath(file))){
                    throw new BizException("复制失败，不可复制到自身或子文件夹下！");
                }
                checkExistFile(currentFolderAppendSubPath(targetFile), file.getName(), file.getType());
                if (file.getType() == FileTypeEnum.FILE.getValue()) {  // 复制文件
                    file.setPath(currentFolderAppendSubPath(targetFile));
                    file.setRefId(file.getId());
                    file.setCreateTime(DateUtils.getTime());
                    sourceFiles.add(file);
                } else if (file.getType() == FileTypeEnum.DIR.getValue()) { // 复制文件夹及子文件
                    String sourceFilePath = file.getPath();
                    sourceFiles.add(file);
                    sourceFiles.addAll(fileRepository.findAllByPathStartsWithAndStatus(currentFolderAppendSubPath(file), FileStatusEnum.NORMAL.getValue()));
                    Optional.ofNullable(sourceFiles).ifPresent(files ->
                            files.forEach(f -> {
                                String newPath = f.getPath().replaceFirst(sourceFilePath, targetFilePath);
                                f.setPath(newPath);
                                f.setRefId(f.getId());
                                f.setCreateTime(DateUtils.getTime());
                            })
                    );
                }
            }
            saved = fileRepository.saveAll(sourceFiles);
        } finally {
            RedisLockUtils.releaseFileLock(redisTemplate, targetFilePath, requestValue);
        }
        return copyFilesToFileDTOs(saved);
    }

    @Override
    public List<FileDTO> getFileList(String path, Integer status, Integer type) {
        if (StringUtils.isEmpty(type)){
            List<File> files = fileRepository.findByPathAndStatus(path, status);
            return copyFilesToFileDTOs(files);
        }
        List<File> files = fileRepository.findAllByPathAndTypeAndStatus(path, type, status);
        return copyFilesToFileDTOs(files);
    }

    @Override
    public List<FileDTO> getCanCopyToFileList(Long id, String path) {
        File file = fileRepository.findById(id).get();
        List<FileDTO> fileList = getFileList(path, FileStatusEnum.NORMAL.getValue(), FileTypeEnum.DIR.getValue());
        return fileList
                .stream()
                .filter(f -> !f.getPath().contains(currentFolderAppendSubPath(file)))
                .filter(f -> f.getId() != file.getId())
                .collect(Collectors.toList());
    }

    @Override
    public List<FileDTO> moveFile(List<Long> sourceIds, Long targetId) throws BizException {
        return null;
    }

    @Override
    public List<FileDTO> deleteFile(List<Long> sourceIds, Long targetId) throws BizException {
        return null;
    }

    @Override
    public List<FileDTO> cleanFile(List<Long> sourceIds, Long targetId) throws BizException {
        return null;
    }

    /*========================== 工具方法============================*/
    /**
     * 检查文件夹是否存在
     */
    private void checkExistFolder(String path) throws BizException {
        File files = fileRepository.queryFirstByPathAndTypeAndStatus(path, FileTypeEnum.DIR.getValue(), FileStatusEnum.NORMAL.getValue());
        if(files == null){
            throw new BizException("文件夹不存在！");
        }
    }

    /**
     * 检查路径下是否存在同名文件
     */
    private void checkExistFile(String path, String fileName, Integer type) throws BizException {
        File files = fileRepository.queryFirstByPathAndNameAndTypeAndStatus(path, fileName, type, FileStatusEnum.NORMAL.getValue());
        if(files != null){
            throw new BizException("该文件夹下已存在同名文件！");
        }
    }
    /**
     * copy文件属性
     */
    private FileDTO copyFileToFileDTO(File file){
        FileDTO fileDTO = new FileDTO();
        BeanUtils.copyProperties(file, fileDTO);
        Optional.ofNullable(file.getFileMeta()).ifPresent(fileMeta -> {
            FileMetaDTO fileMetaDTO = new FileMetaDTO();
            BeanUtils.copyProperties(fileMeta, fileMetaDTO);
            fileDTO.setFileMetaDTO(fileMetaDTO);
        });
        return fileDTO;
    }
    /**
     * copy文件属性
     */
    private List<FileDTO> copyFilesToFileDTOs(List<File> files){
        List<FileDTO> fileDTOList = new ArrayList<>();
        Optional.ofNullable(files).ifPresent(fs ->
                fs.forEach(file ->
                        fileDTOList.add(copyFileToFileDTO(file))));
        return fileDTOList;
    }
    /**
     * 获取文件夹的路径，请确保file类型为文件夹
     */
    private String currentFolderAppendSubPath(File file) {
        StringBuilder sb =new StringBuilder(file.getPath());
        Optional.ofNullable(file).ifPresent(f->
                sb.append(PATH_SPLIT_SYMBOL).append(f.getName()));
        return sb.toString();
    }
}