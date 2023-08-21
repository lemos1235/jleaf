package club.lemos.leaf.plugin;

import club.lemos.leaf.plugin.util.LeafSpringContextUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.base.Preconditions;
import club.lemos.leaf.exception.InitException;
import club.lemos.leaf.segment.dao.IDAllocDao;
import club.lemos.leaf.segment.dao.impl.IDAllocDaoImpl;
import club.lemos.leaf.service.SegmentService;
import club.lemos.leaf.service.SnowflakeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.util.StringUtils;


@Configuration
@EnableConfigurationProperties(LeafSpringBootProperties.class)
public class LeafSpringBootStarterAutoConfigure {
    private final Logger logger = LoggerFactory.getLogger(LeafSpringBootStarterAutoConfigure.class);
    @Autowired
    private LeafSpringBootProperties properties;

    @Bean
    public LeafSpringContextUtil leafSpringContextUtil() {
        return new LeafSpringContextUtil();
    }

    @Bean
    @DependsOn(value = {"leafSpringContextUtil"})
    public SegmentService initLeafSegmentStarter() throws Exception {
        LeafSpringBootProperties.Segment segment = properties.getSegment();
        if (properties != null && segment != null && segment.isEnable()) {
            String allocStrategyDaoBeanName = segment.getAllocStrategyDaoBeanName();
            IDAllocDao allocDao = null;
            if (!StringUtils.isEmpty(allocStrategyDaoBeanName)) {
                allocDao = LeafSpringContextUtil.getBean(allocStrategyDaoBeanName, IDAllocDao.class);
            } else {
                String url = segment.getUrl();
                String username = segment.getUsername();
                String pwd = segment.getPassword();
                Preconditions.checkNotNull(url, "database url can not be null");
                Preconditions.checkNotNull(username, "username can not be null");
                Preconditions.checkNotNull(pwd, "password can not be null");
                // Config dataSource
                DruidDataSource dataSource = new DruidDataSource();
                dataSource.setUrl(url);
                dataSource.setUsername(username);
                dataSource.setPassword(pwd);
                dataSource.init();
                // Config Dao
                allocDao = new IDAllocDaoImpl(dataSource);
            }
            return new SegmentService(allocDao);
        }
        logger.warn("init leaf segment ignore properties is {}", properties);
        return null;
    }

    @Bean
    public SnowflakeService initLeafSnowflakeStarter() throws InitException {
        if (properties != null && properties.getSnowflake() != null && properties.getSnowflake().isEnable()) {
            return new SnowflakeService(properties.getSnowflake().getAddress(), properties.getSnowflake().getPort());
        }
        logger.warn("init leaf snowflake ignore properties is {}", properties);
        return null;
    }
}
