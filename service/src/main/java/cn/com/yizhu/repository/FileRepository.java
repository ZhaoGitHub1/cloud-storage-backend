package cn.com.yizhu.repository;

import cn.com.yizhu.entity.File;
import cn.com.yizhu.entity.FileMeta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FileRepository extends MongoRepository<File, String> {

    /**
     * 根据文件元信息查询
     * @param fileMeta
     * @return
     */
    File queryByFileMeta(FileMeta fileMeta);

    /**
     * 根据md5值和文件大小查找源文件
     * @param md5
     * @param size
     * @return
     */
    @Query(value = "{'fileMeta.md5':?0, 'fileMeta.size':?1, 'fileMeta.refId':0}", fields = "{'fileMeta.key':1, 'fileMeta.md5':1}")
    List<File> queryFile(String md5, long size);
}
