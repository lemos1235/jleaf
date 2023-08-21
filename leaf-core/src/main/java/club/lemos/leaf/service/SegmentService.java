package club.lemos.leaf.service;

import club.lemos.leaf.IDGen;
import club.lemos.leaf.common.Result;
import club.lemos.leaf.exception.InitException;
import club.lemos.leaf.segment.SegmentIDGenImpl;
import club.lemos.leaf.segment.dao.IDAllocDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SegmentService {
    private Logger logger = LoggerFactory.getLogger(SegmentService.class);

    private IDGen idGen;

    public SegmentService(IDAllocDao allocDao) throws InitException {
        // Config ID Gen
        idGen = new SegmentIDGenImpl();
        ((SegmentIDGenImpl) idGen).setDao(allocDao);
        if (idGen.init()) {
            logger.info("Segment Service Init Successfully");
        } else {
            throw new InitException("Segment Service Init Fail");
        }
    }

    public Result getId(String key) {
        return idGen.get(key);
    }

    public SegmentIDGenImpl getIdGen() {
        if (idGen instanceof SegmentIDGenImpl) {
            return (SegmentIDGenImpl) idGen;
        }
        return null;
    }
}
