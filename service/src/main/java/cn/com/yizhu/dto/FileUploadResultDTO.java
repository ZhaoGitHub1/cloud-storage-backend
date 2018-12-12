package cn.com.yizhu.dto;

/**
 * 文件上传返回信息DTO
 */
public class FileUploadResultDTO {

    /**
     * 文件key
     */
    private String key;

    /**
     * 文件后缀
     */
    private String suffix;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * MD5的值，用于区分文件是否存在
     */
    private String md5;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
