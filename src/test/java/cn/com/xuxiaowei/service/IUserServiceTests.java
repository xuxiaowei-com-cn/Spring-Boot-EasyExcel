package cn.com.xuxiaowei.service;

import cn.com.xuxiaowei.entity.User;
import cn.com.xuxiaowei.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 用户 User 测试类
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@SpringBootTest
class IUserServiceTests {

    @Autowired
    private IUserService iUserService;

    @Resource
    private UserMapper userMapper;

    /**
     * 查询 id 为 1 并且没有删除的用户信息
     */
    @Test
    void getById() {
        User byId = iUserService.getById(1);
        log.debug(String.valueOf(byId));
    }

    /**
     * 查询 用户名 为 xxw 并且没有删除的用户信息
     */
    @Test
    void queryWrapper() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(User.USERNAME, "xxw");
        User user = userMapper.selectOne(queryWrapper);
        log.debug(String.valueOf(user));
    }

}
