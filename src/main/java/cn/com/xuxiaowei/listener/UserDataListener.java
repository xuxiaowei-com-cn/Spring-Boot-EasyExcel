package cn.com.xuxiaowei.listener;

import cn.com.xuxiaowei.entity.User;
import cn.com.xuxiaowei.service.IUserService;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import com.alibaba.excel.read.metadata.holder.ReadSheetHolder;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义 接收解析后的每条数据的返回
 * <p>
 * {@link UserDataListener} 不能被 spring 管理，要每次读取 excel 都要 new ,然后里面用到 spring 可以构造方法传进去
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
public class UserDataListener extends AnalysisEventListener<User> {

    private User user;

    private IUserService iUserService;

    List<User> list = new ArrayList<>();

    /**
     * 每隔 5 条存储数据库，实际使用中可以 3000 条，然后清理 list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;

    /**
     * 未使用 spring ,如果使用了 spring ,请使用下面的构造器
     */
    public UserDataListener() {
        this.user = new User();
    }

    /**
     * 如果使用了 spring ,请使用这个构造方法。每次创建 Listener 的时候需要把 spring 管理的类传进来
     */
    public UserDataListener(User user) {
        this.user = user;
    }

    /**
     * 使用 Spring 时，使用构造器将 Spring 管理的 Bean 传入
     */
    public UserDataListener(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    /**
     * 每一条数据解析都会来调用
     */
    @Override
    public void invoke(User user, AnalysisContext analysisContext) {

        ReadSheetHolder readSheetHolder = analysisContext.readSheetHolder();
        ReadRowHolder readRowHolder = analysisContext.readRowHolder();

        log.debug("Sheet Name：{}", readSheetHolder.getSheetName());
        log.debug("Sheet No：{}", readSheetHolder.getSheetNo());
        log.debug("大约总行数：{}", readSheetHolder.getApproximateTotalRowNumber());
        log.debug("读取到第 {} 行", readRowHolder.getRowIndex());
        log.debug("解析到一条数据:{}", JSON.toJSONString(user));

        list.add(user);

        // 达到 BATCH_COUNT 了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {

            saveUserData();

            // 存储完成清理 list
            list.clear();
        }
    }

    /**
     * 所有数据解析完成了，就会来调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.debug("{}条数据，开始存储数据库！", list.size());

        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        list.parallelStream().forEach(u -> log.debug(String.valueOf(u)));

        saveUserList();

        log.info("所有数据解析完成！");
    }

    private void saveUserData() {
        log.debug("{}条数据，开始存储数据库！", list.size());

        list.parallelStream().forEach(u -> log.debug(String.valueOf(u)));

        saveUserList();

        log.info("存储数据库成功！");
    }

    private void saveUserList() {
        if (iUserService != null) {
            list.parallelStream().forEach(u -> iUserService.save(u));
        }
    }

}
