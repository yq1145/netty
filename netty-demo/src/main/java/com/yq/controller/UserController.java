package com.yq.controller;


import com.yq.pojo.TbUser;
import com.yq.pojo.vo.Result;
import com.yq.pojo.vo.User;
import com.yq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 测试环境是否正常运行
     *
     * @return
     */
    @RequestMapping("findAll")
    public List<TbUser> findAll() {
        List<TbUser> list = userService.findAll();
        return list;
    }

    /**
     * 登录
     *
     * @param user
     * @return
     */
    @RequestMapping("login")
    public Result login(@RequestBody TbUser user) {
        User _user = null;
        try {
            _user = userService.login(user.getUsername(), user.getPassword());
            if (_user == null) {
                return new Result(false, "登录失败,请检查用户名或密码是否正确.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "登录内部错误");

        }
        return new Result(true, "登录成功", _user);

    }

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @RequestMapping("register")
    public Result register(@RequestBody TbUser user) {
        //如果注册成功,不抛出异常,如果失败则抛出
        try {
            userService.register(user);
            return new Result(true, "注册成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "注册失败,请检查输入信息.");
        }
    }

    /**
     * 上传文件
     *
     * @param file
     * @param userid
     * @return
     */
    @RequestMapping("upload")
    public Result upload(MultipartFile file, String userid) {
        User user = userService.upload(file, userid);
        if (user != null) {
            System.out.println(user);
            return new Result(true, "上传成功", user);
        }
        return new Result(true, "上传失败");
    }

    /**
     * 修改昵称
     *
     * @param user
     * @return
     */
    @RequestMapping("updateNickname")
    public Result updateNickname(@RequestBody TbUser user) {
        try {
            userService.updateNickname(user);
            return new Result(true, "上传成功.");
        } catch (RuntimeException e) {
            return new Result(false, e.getMessage());
        }
    }

    /**
     * 根据id查询用户信息
     *
     * @param id
     * @return
     */
    @RequestMapping("findById")
    public User findById(@RequestParam(name = "userid") String id) {
        return userService.findById(id);
    }

    /**
     * 根据用户名进行搜索
     *
     * @param userid
     * @param friendUsername
     * @return
     */
    @RequestMapping("findByUsername")
    public Result findByUsername(String userid, String friendUsername) {
        User friendUser = userService.findByUsername(userid, friendUsername);
        if (friendUser != null) {
            return new Result(true, "搜索成功", friendUser);
        }
        return new Result(false, "搜索失败");

    }

}
