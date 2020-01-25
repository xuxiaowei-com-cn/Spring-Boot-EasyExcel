package cn.com.xuxiaowei.generator;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * MyBatis Plus 代码生成器
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
public class MyBatisPlusGenerator {

    /**
     * 父包名
     */
    private String parent = "cn.com.xuxiaowei";

    /**
     * 作者
     */
    private String author = "徐晓伟";

    /**
     * 读取本地项目文件夹路径（Spring Boot）
     */
    private String projectPath = System.getProperty("user.dir");

    /**
     * 主文件夹
     */
    private String main = projectPath + "/src/main";

    /**
     * java 文件的路径
     */
    private String outputDir = main + "/java";

    /**
     * XML 文件夹
     */
    private String mapper = main + "/resources/mapper/";

    /**
     * 数据库 地址
     */
    private String url = "jdbc:mysql://127.0.0.1:3306/spring_boot_easyexcel?useSSL=false&serverTimezone=GMT%2B8";

    /**
     * 数据库 用户名
     */
    private String username = "root";

    /**
     * 数据库 密码
     */
    private String password = "root";

    /**
     * 数据库 驱动名称
     */
    private String driverName = com.mysql.cj.jdbc.Driver.class.getName();

    /**
     * 自定义继承的Entity类全称，带包名
     */
    private String superEntityClass = null;

    /**
     * 自定义继承的Controller类全称，带包名
     */
    private String superControllerClass = null;

    /**
     * Controller 名
     */
    private String controllerName = "%sRestController";

    /**
     * 是否生成 BaseResultMap
     */
    private boolean baseResultMap = true;

    /**
     * 逻辑删除属性名称
     */
    private String logicDeleteFieldName = "deleted";

    public static void main(String[] args) {

        MyBatisPlusGenerator myBatisPlusGenerator = new MyBatisPlusGenerator();

        myBatisPlusGenerator.getAutoGenerator();

    }

    /**
     * 代码生成器
     */
    private void getAutoGenerator() {

        // 创建 代码生成器
        AutoGenerator autoGenerator = new AutoGenerator();

        // 创建 全局配置
        GlobalConfig globalConfig = getGlobalConfig();

        // 代码生成器 设置 全局配置
        autoGenerator.setGlobalConfig(globalConfig);

        // 数据源配置
        DataSourceConfig dataSourceConfig = getDataSourceConfig();

        // 代码生成器 设置 数据源配置
        autoGenerator.setDataSource(dataSourceConfig);

        // 包配置
        PackageConfig packageConfig = getPackageConfig();

        // 设置 包配置
        autoGenerator.setPackageInfo(packageConfig);

        // 自定义配置
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 输出文件配置
        List<FileOutConfig> fileOutConfigLists = getFileOutConfigList(packageConfig);

        // 自定义配置 设置 自定义输出文件
        injectionConfig.setFileOutConfigList(fileOutConfigLists);

        // 代码生成器 设置 自定义配置
        autoGenerator.setCfg(injectionConfig);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
//        <code>templateConfig.setEntity("templates/entity2.java");</code>
//        <code>templateConfig.setService("/templates/service2.java");</code>
//        <code>templateConfig.setServiceImpl("/templates/serviceImpl2.java");</code>
//        <code>templateConfig.setController("/templates/controller2.java");</code>

        templateConfig.setXml(null);

        // 代码生成器 设置 模板
        autoGenerator.setTemplate(templateConfig);

        // 设置 策略配置项
        StrategyConfig strategyConfig = getStrategyConfig(packageConfig);

        // 数据库表配置
        autoGenerator.setStrategy(strategyConfig);

        // 模板引擎
        // FreemarkerTemplateEngine
        // VelocityTemplateEngine
        autoGenerator.setTemplateEngine(new VelocityTemplateEngine());

        // 生成代码
        autoGenerator.execute();

    }

    /**
     * 输出文件配置
     *
     * @param packageConfig 跟包相关的配置项
     * @return 输出文件配置
     */
    private List<FileOutConfig> getFileOutConfigList(PackageConfig packageConfig) {
        // 如果模板引擎是 freemarker
        // String xmlPath = ConstVal.TEMPLATE_XML + ".ftl";

        // 如果模板引擎是 velocity
        String xmlPath = ConstVal.TEMPLATE_XML + ".vm";

        // 自定义输出配置
        List<FileOutConfig> fileOutConfigs = new ArrayList<>();

        // 自定义配置会被优先输出
        fileOutConfigs.add(new FileOutConfig(xmlPath) {
            @Override
            public String outputFile(TableInfo tableInfo) {

                // 模块名
                String moduleName = packageConfig.getModuleName();

                // 模块名是否为空
                if (moduleName == null) {
                    moduleName = "";
                }

                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return mapper + moduleName + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        return fileOutConfigs;
    }


    /**
     * 获取 跟包相关的配置项
     *
     * @return 跟包相关的配置项
     */
    private PackageConfig getPackageConfig() {

        PackageConfig packageConfig = new PackageConfig();

        // 模块名
        String moduleName = scanner("是否生成模块名：\n输入“-”时，忽略模块名");

        // 忽略模块名的情况
        String noModuleName = "-";

        if (!noModuleName.equals(moduleName)) {
            // 模块名
            packageConfig.setModuleName(moduleName);
        }

        // 设置 父包名
        packageConfig.setParent(parent);

        return packageConfig;
    }

    /**
     * 设置 全局配置
     */
    private GlobalConfig getGlobalConfig() {

        GlobalConfig globalConfig = new GlobalConfig();

        // java8 新的时间类型
        globalConfig.setDateType(DateType.TIME_PACK);

        // 设置生成文件的输出目录
        globalConfig.setOutputDir(outputDir);

        // 设置 作者
        globalConfig.setAuthor(author);

        // 是否打开输出目录
        globalConfig.setOpen(false);

        String scanner = scanner("是否覆盖：\n输入“f”时，覆盖原文件");

        // 文件是否覆盖
        String fileOverride = "f";

        if (fileOverride.equals(scanner)) {
            globalConfig.setFileOverride(true);
        }

        // 实体属性 Swagger2 注解
        // <code>globalConfig.setSwagger2(true);</code>

        // 开启 BaseResultMap
        globalConfig.setBaseResultMap(baseResultMap);

        // 设置 Controller 名
        globalConfig.setControllerName(controllerName);

        return globalConfig;
    }

    /**
     * 设置 数据库配置
     */
    private DataSourceConfig getDataSourceConfig() {

        DataSourceConfig dataSourceConfig = new DataSourceConfig();

        // 设置 数据库 地址
        dataSourceConfig.setUrl(url);
        // 设置 驱动名称
        dataSourceConfig.setDriverName(driverName);
        // 设置 数据库 用户名
        dataSourceConfig.setUsername(username);
        // 设置 数据库 密码
        dataSourceConfig.setPassword(password);

        return dataSourceConfig;
    }

    /**
     * 设置 策略配置项
     *
     * @param packageConfig 跟包相关的配置项
     */
    private StrategyConfig getStrategyConfig(PackageConfig packageConfig) {

        StrategyConfig strategyConfig = new StrategyConfig();

        // 数据库表映射到实体的命名策略
        // underline_to_camel：下划线转驼峰命名
        // no_change：不做任何改变，原样输出
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);

        // 数据库表字段映射到实体的命名策略
        // underline_to_camel：下划线转驼峰命名
        // no_change：不做任何改变，原样输出
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);

        // 自定义继承的Entity类全称，带包名
        strategyConfig.setSuperEntityClass(superEntityClass);

        // 【实体】是否为lombok模型（默认 false）
        strategyConfig.setEntityLombokModel(true);

        // 【实体】是否生成字段常量（默认 false）
        strategyConfig.setEntityColumnConstant(true);

        // 生成 <code>@RestController</code> 控制器
        strategyConfig.setRestControllerStyle(true);

        // 是否生成实体时，生成字段注解
        strategyConfig.setEntityTableFieldAnnotationEnable(true);

        strategyConfig.setLogicDeleteFieldName(logicDeleteFieldName);

        // 自定义继承的Controller类全称，带包名
        strategyConfig.setSuperControllerClass(superControllerClass);

        // 需要包含的表名，允许正则表达式（与exclude二选一配置）
        strategyConfig.setInclude(scanner("表名，多个英文逗号分割").split(","));

        // 驼峰转连字符
        strategyConfig.setControllerMappingHyphenStyle(true);

        // 表前缀
        strategyConfig.setTablePrefix(packageConfig.getModuleName() + "_");

        return strategyConfig;
    }


    /**
     * 读取控制台内容
     */
    private String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(("请输入" + tip + "："));
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

}
