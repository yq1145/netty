package com.yq.service;

import com.yq.pojo.TbUser;
import com.yq.pojo.vo.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    List<TbUser> findAll();

    /**
     * 登录,根据用户名和密码判断
     *
     * @param username
     * @param password
     * @return
     */
    User login(String username, String password);

    /**
     * 注册,将用户信息保存到数据库中
     *
     * @param user
     */
    void register(TbUser user);
    /**
     * 上传文件
     *
     * @param file
     * @param userId
     * @return
     */
    User upload(MultipartFile file, String userId);
    /**
     * 修改昵称
     *
     * @param user
     * @return
     */
    void updateNickname(TbUser user);
    /**
     * 根据id查询用户信息
     *
     * @param id
     * @return
     */
    User findById(String id);
    /**
     * 根据用户名进行搜索
     *
     * @param userid
     * @param friendUsername
     * @return
     */
    User findByUsername(String userid, String friendUsername);
}
