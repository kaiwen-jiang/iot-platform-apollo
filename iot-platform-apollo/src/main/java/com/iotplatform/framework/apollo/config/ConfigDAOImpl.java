package com.iotplatform.framework.apollo.config;

import com.iotplatform.framework.apollo.internals.ConfigFrameworkDAO;
import com.iotplatform.framework.apollo.internals.dto.ConfigRespDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

/**
 * ConfigDAOImpl 实现类
 * @author Kevin
 * @date 2022/8/02 21:20
 */
public class ConfigDAOImpl implements ConfigFrameworkDAO {

    private final JdbcTemplate jdbcTemplate;

    public ConfigDAOImpl(String jdbcUrl, String username, String password) {
        DataSource dataSource = new DriverManagerDataSource(jdbcUrl, username, password);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int selectCountByUpdateTimeGt(Date maxUpdateTime) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM infra_config WHERE update_time > ?",
                Integer.class, maxUpdateTime);
    }

    @Override
    public List<ConfigRespDTO> selectList() {
        return jdbcTemplate.query("SELECT config_key, value, update_time, deleted FROM infra_config",
                (rs, rowNum) -> new ConfigRespDTO().setKey(rs.getString("config_key"))
                        .setValue(rs.getString("value"))
                        .setUpdateTime(rs.getDate("update_time"))
                        .setDeleted(rs.getBoolean("deleted")));
    }

}
