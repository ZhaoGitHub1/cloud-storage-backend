package cn.com.yizhu.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * 文件仓储实现类
 */
@Repository
public class FileRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

//    public FileMeta queryByFileMeta(FileMeta fileMeta){
//        if (fileMeta == null) return null;
//
//        Query query = new Query(Criteria
//                .where("fileMeta.md5").is(fileMeta.getMd5())
//                .and("fileMeta.size").is(fileMeta.getSize())
//                .and("fileMeta.suffix").is(fileMeta.getSuffix())
//                .and("status").is(FileStatusEnum.NORMAL.getValue())
//        );
//        return mongoTemplate.findOne(query, FileMeta.class);
//    }
}
