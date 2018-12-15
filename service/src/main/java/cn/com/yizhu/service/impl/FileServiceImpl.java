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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 文件业务逻辑处理类
 */
@Service
public class FileServiceImpl implements FileService {

    static final Logger LOG = LoggerFactory.getLogger(FileServiceImpl.class);
    static final Integer EXPIRE_TIME = 10;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional
    @Override
    public FileDTO createFile(MultipartFile multipartFile, String path, String fileName, FileTypeEnum fileType, String tag) throws BizException {
        String requestValue = "cs:guest";
        File saved;

        if(! checkExistFolder(path)){
            throw new BizException("文件夹不存在！");
        }

        if (checkExistFile(path, fileName, fileType.getValue())){
            throw new BizException("该文件夹下已存在名称为"+fileName+"的文件");
        }
        try {
            if ( ! RedisLockUtils.getFileLock(redisTemplate, path,requestValue, EXPIRE_TIME)){
                new BizException("系统繁忙，请稍后重试！");
            }
            File file = new File(fileName, fileType.getValue(), path, FileStatusEnum.NORMAL.getValue(), 0L, DateUtils.getTime(),null);

            if (FileTypeEnum.FILE == fileType){
                Optional.ofNullable(multipartFile).orElseThrow(()->new BizException("请上传文件"));

                String originalFilename = multipartFile.getOriginalFilename();
                if (StringUtils.isEmpty(fileName)){
                    file.setName(originalFilename.substring(0, originalFilename.lastIndexOf("."))); // 用户未填写fileName，则设置文件名为上传文件的名字
                }

                long size = multipartFile.getSize();
                String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
                String fileMd5 = FileUploadUtil.getFileMd5(multipartFile.getInputStream()); // 获取上传文件的md5值
                FileMeta fileMeta = new FileMeta(0L, System.currentTimeMillis(), size, tag, null, suffix, fileMd5);

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
            }
            saved = fileRepository.save(file);
        } catch (Exception e){
            LOG.error("创建文件失败，异常：",e);
            throw new BizException("创建文件失败！");
        } finally {
            RedisLockUtils.releaseFileLock(redisTemplate, path, requestValue);
        }
        return copyFileToFileDTO(saved);
    }
    private Boolean checkExistFolder(String path){
        List<File> files = fileRepository.findIdByPathAndTypeAndStatus(path, FileTypeEnum.DIR.getValue(), FileStatusEnum.NORMAL.getValue());
        return (files== null || files.size()<=0) ? false : true;
    }

    private Boolean checkExistFile(String path, String fileName, Integer type){
        List<File> files = fileRepository.findIdByPathAndNameAndTypeAndStatus(path, fileName, type, FileStatusEnum.NORMAL.getValue());
        return (files== null || files.size()<=0) ? false : true;
    }

    @Override
    public List<FileDTO> getFileList(String path, Integer status, Integer type) {
        List<FileDTO> fileDTOS = new ArrayList<>();
        if (StringUtils.isEmpty(type)){
            List<File> files = fileRepository.findByPathAndStatus(path, status);
            files.forEach(file -> fileDTOS.add(copyFileToFileDTO(file)));
            return fileDTOS;
        }
        List<File> files = fileRepository.findAllByPathAndTypeAndStatus(path, status, type);
        files.forEach(file -> fileDTOS.add(copyFileToFileDTO(file)));
        return fileDTOS;
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

}
