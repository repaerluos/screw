/*
 * screw-extension - 简洁好用的数据库表结构文档生成工具
 * Copyright © 2020 SanLi (qinggang.zuo@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cn.smallbun.screw.extension;

import java.util.ArrayList;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import cn.smallbun.screw.core.process.ProcessConfig;
import cn.smallbun.screw.extension.pojo.PojoConfiguration;
import cn.smallbun.screw.extension.pojo.execute.PojoExecute;
import cn.smallbun.screw.extension.pojo.strategy.HumpNameStrategy;

public class GenPojoFiles4DB {
    public static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    public static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/zm-pms?useUnicode=true&characterEncoding=UTF-8";
    public static final String USER_NAME = "root";
    public static final String PWD = "root123";

    public static final String FILE_OUTPUT_DIR = "/home/repaerluos/workspace/pms/PMS/doc/database/pojo/";
    public static final String PKG_NAME = "cn.smallbun.screw";

    public static void main(String[] args) {
        pojoGeneration();
    }

    /**
     * pojo生成
     */
    public static void pojoGeneration() {
        // 数据源
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(GenPojoFiles4DB.DRIVER_CLASS_NAME);
        hikariConfig.setJdbcUrl(GenPojoFiles4DB.JDBC_URL);
        hikariConfig.setUsername(GenPojoFiles4DB.USER_NAME);
        hikariConfig.setPassword(GenPojoFiles4DB.PWD);
        // 设置可以获取tables remarks信息
        hikariConfig.addDataSourceProperty("useInformationSchema", "true");
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setMaximumPoolSize(5);
        DataSource dataSource = new HikariDataSource(hikariConfig);

        ProcessConfig processConfig = ProcessConfig.builder()
                // 指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置
                // 根据名称指定表生成
                .designatedTableName(new ArrayList<>())
                // 根据表前缀生成
                .designatedTablePrefix(new ArrayList<>())
                // 根据表后缀生成
                .designatedTableSuffix(new ArrayList<>()).build();

        // 设置生成pojo相关配置
        PojoConfiguration config = new PojoConfiguration();
        // 设置文件存放路径
        config.setPath(GenPojoFiles4DB.FILE_OUTPUT_DIR);
        // 设置包名
        config.setPackageName(GenPojoFiles4DB.PKG_NAME);
        // 设置是否使用lombok
        config.setUseLombok(true);
        // 设置数据源
        config.setDataSource(dataSource);
        // 设置命名策略
        config.setNameStrategy(new HumpNameStrategy());
        // 设置表过滤逻辑
        config.setProcessConfig(processConfig);
        // 执行生成
        new PojoExecute(config).execute();
    }
}
