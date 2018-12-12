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
import cn.com.yizhu.utils.FileUploadUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 文件业务逻辑处理类
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository fileRepository;

    @Transactional
    @Override
    public FileDTO createFile(MultipartFile multipartFile, String path, String fileName, FileTypeEnum fileType) throws Exception{
        File file = new File();
        file.setCreateId(0L);
        file.setCreateTime(new Date());
        file.setName(fileName);
        file.setStatus(FileStatusEnum.NORMAL.getValue());
        file.setPath(path);

        if (FileTypeEnum.FILE == fileType){

            Optional.ofNullable(multipartFile).orElseThrow(()->new BizException("请上传文件"));

            String originalFilename = multipartFile.getOriginalFilename();
            file.setType(FileTypeEnum.FILE.getValue());
            if (StringUtils.isEmpty(fileName)){
                file.setName(originalFilename.substring(0, originalFilename.lastIndexOf("."))); // 用户喂填写fileName，则设置文件名为上传文件的名字
            }

            FileMeta fileMeta = new FileMeta();
            fileMeta.setSize(multipartFile.getSize());
            fileMeta.setSuffix(originalFilename.substring(originalFilename.lastIndexOf(".")+1));
            fileMeta.setMd5(FileUploadUtil.getFileMd5(multipartFile.getInputStream())); // 获取上传文件的md5值

            List<File> existedFile = fileRepository.queryFile(FileUploadUtil.getFileMd5(multipartFile.getInputStream()), multipartFile.getSize());

            if (existedFile != null && existedFile.size() > 0){
                fileMeta.setKey(existedFile.get(0).getFileMeta().getKey());
            }else {
                FileUploadResultDTO fileUploadResultDTO = FileUploadUtil.uploadFile(multipartFile);// 上传文件

                fileMeta.setKey(fileUploadResultDTO.getKey());
                fileMeta.setMd5(fileUploadResultDTO.getMd5()); // 覆盖输入流的md5
            }

            fileMeta.setRefId(0L);
            fileMeta.setTag(fileName);
            fileMeta.setVersion(System.currentTimeMillis());
            file.setFileMeta(fileMeta);
        }else if (FileTypeEnum.DIR == fileType){
            file.setType(FileTypeEnum.DIR.getValue());
        }

        return copyFileToFileDTO(fileRepository.save(file));
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