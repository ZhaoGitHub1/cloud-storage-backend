package cn.com.yizhu.utils;

import cn.com.yizhu.common.exception.BizException;
import cn.com.yizhu.dto.FileUploadResultDTO;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 文件上传工具类
 */
public class FileUploadUtil {

    /**
     * 文件存储绝对路径
     */
    private static final String uploadFolder = "F:/code/git/cloud-storage-backend/files/";
    /**
     * 文件统一后缀
     */
    private static final String FILE_SUFFIX = ".file";

    /**
     * 上传文件到本地
     * @param multipartFile
     * @return FileUploadResultDTO
     */
    public static FileUploadResultDTO uploadFile(MultipartFile multipartFile){
        FileUploadResultDTO fileUploadResultDTO = new FileUploadResultDTO();
        String filename = multipartFile.getOriginalFilename();
        String uuName = UUID.randomUUID().toString();
        fileUploadResultDTO.setFileName(filename);
        fileUploadResultDTO.setKey(uuName);
        fileUploadResultDTO.setSize(multipartFile.getSize());
        fileUploadResultDTO.setSuffix(filename.substring(filename.lastIndexOf(".") + 1));
        try {
            String md5DigestAsHex = DigestUtils.md5DigestAsHex(multipartFile.getInputStream());
            fileUploadResultDTO.setMd5(md5DigestAsHex);

            multipartFile.transferTo(new File(uploadFolder + uuName + FILE_SUFFIX));
        } catch (IOException e) {
            e.printStackTrace();
            new BizException("上传文件失败！");
        }
        return fileUploadResultDTO;
    }

    /**
     * 获取输入流的md5值
     * @param is
     * @return
     */
    public static String getFileMd5(InputStream is){
        try {
            return DigestUtils.md5DigestAsHex(is);
        } catch (IOException e) {
            e.printStackTrace();
            new BizException("获取md5值失败！");
        }
        return null;
    }
}
