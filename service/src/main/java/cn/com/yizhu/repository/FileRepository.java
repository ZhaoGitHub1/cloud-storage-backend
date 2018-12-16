package cn.com.yizhu.repository;

import cn.com.yizhu.entity.File;
import cn.com.yizhu.entity.FileMeta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FileRepository extends MongoRepository<File, Long> {

    /**
     * 根据文件元信息查询
     * @param fileMeta
     * @return
     */
    File queryByFileMeta(FileMeta fileMeta);

    /**
     * 根据md5值和文件大小查找源文件
     * @param md5
     * @return
     */
    @Query(value = "{'fileMeta.md5':?0, 'refId':0, 'status':0}", fields = "{'fileMeta.key':1, 'fileMeta.md5':1}")
    List<File> queryFile(String md5);

    /**
     * 查询文件列表
     * @param path
     * @param status
     * @param type
     * @return
     */
    List<File> findAllByPathAndTypeAndStatus(String path, Integer type, Integer status);

    /**
     * 查询文件列表
     * @param path
     * @param status
     * @return
     */
    List<File> findByPathAndStatus(String path, Integer status);

    /**
     * 查询文件是否存在
     * @param path
     * @param type
     * @return
     */
    File queryFirstByPathAndTypeAndStatus(String path, Integer type, Integer status);

    File queryFirstByPathAndNameAndTypeAndStatus(String path, String name, Integer type, Integer status);

    List<File> findAllByPathStartsWithAndStatus(String path, Integer status);
}
