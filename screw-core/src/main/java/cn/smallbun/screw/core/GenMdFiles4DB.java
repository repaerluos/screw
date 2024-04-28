/*
 * screw-core - 简洁好用的数据库表结构文档生成工具
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
package cn.smallbun.screw.core;

import java.util.ArrayList;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;

public class GenMdFiles4DB {
    public static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    public static final String JDBC_URL          = "jdbc:mysql://127.0.0.1:3306/zm-pms?useUnicode=true&characterEncoding=UTF-8";
    public static final String USER_NAME         = "root";
    public static final String PWD               = "root123";

    public static final String FILE_OUTPUT_DIR   = "/home/repaerluos/workspace/pms/PMS/doc/database/";
    public static final String FILE_NAME         = "pms-dbinfo";
    public static final String FILE_DESC         = "数据库设计文档生成。(UK等索引信息不包含，需要参照 <a href='./pms-ddl.sql'>pms-ddl.sql</a>)";

    public static void main(String[] args) {
        documentGeneration();
    }

    /**
     * 文档生成
     */
    public static void documentGeneration() {
        // 数据源
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(GenMdFiles4DB.DRIVER_CLASS_NAME);
        hikariConfig.setJdbcUrl(GenMdFiles4DB.JDBC_URL);
        hikariConfig.setUsername(GenMdFiles4DB.USER_NAME);
        hikariConfig.setPassword(GenMdFiles4DB.PWD);
        // 设置可以获取tables remarks信息
        hikariConfig.addDataSourceProperty("useInformationSchema", "true");
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setMaximumPoolSize(5);
        DataSource dataSource = new HikariDataSource(hikariConfig);
        // 生成配置
        EngineConfig engineConfig = EngineConfig.builder()
            // 生成文件路径
            .fileOutputDir(GenMdFiles4DB.FILE_OUTPUT_DIR)
            // 打开目录
            .openOutputDir(true)
            // 文件类型
            .fileType(EngineFileType.MD)
            // 生成模板实现
            .produceType(EngineTemplateType.freemarker)
            // 自定义文件名称
            .fileName(GenMdFiles4DB.FILE_NAME).build();

        // 忽略表
        ArrayList<String> ignoreTableName = new ArrayList<>();
        ignoreTableName.add("test_user");
        ignoreTableName.add("test_group");
        // 忽略表前缀
        ArrayList<String> ignorePrefix = new ArrayList<>();
        ignorePrefix.add("test_");
        // 忽略表后缀
        ArrayList<String> ignoreSuffix = new ArrayList<>();
        ignoreSuffix.add("_test");
        ProcessConfig processConfig = ProcessConfig.builder()
            // 指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置
            // 根据名称指定表生成
            .designatedTableName(new ArrayList<>())
            // 根据表前缀生成
            .designatedTablePrefix(new ArrayList<>())
            // 根据表后缀生成
            .designatedTableSuffix(new ArrayList<>())
            // 忽略表名
            .ignoreTableName(ignoreTableName)
            // 忽略表前缀
            .ignoreTablePrefix(ignorePrefix)
            // 忽略表后缀
            .ignoreTableSuffix(ignoreSuffix).build();
        // 配置
        Configuration config = Configuration.builder()
            // 版本
            .version("1.0.0")
            // 描述
            .description(GenMdFiles4DB.FILE_DESC)
            // 数据源
            .dataSource(dataSource)
            // 生成配置
            .engineConfig(engineConfig)
            // 生成配置
            .produceConfig(processConfig).build();
        // 执行生成
        new DocumentationExecute(config).execute();
    }

}
