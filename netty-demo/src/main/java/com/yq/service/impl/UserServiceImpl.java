package com.yq.service.impl;

import com.yq.mapper.TbUserMapper;
import com.yq.pojo.TbUser;
import com.yq.pojo.TbUserExample;
import com.yq.pojo.vo.User;
import com.yq.service.UserService;
import com.yq.utils.FastDFSClient;
import com.yq.utils.IdWorker;
import com.yq.utils.QRCodeUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper userMapper;
    @Autowired
    private IdWorker idWorker;

    @Autowired
    private FastDFSClient fastDFSClient;

    @Autowired
    private Environment env;

    @Autowired
    private QRCodeUtils qrCodeUtils;

    @Override
    public List<TbUser> findAll() {
        return userMapper.selectByExample(null);
    }

    @Override
    public User findById(String id) {
        try {
            TbUser tbUser = userMapper.selectByPrimaryKey(id);
            if (tbUser != null) {
                User user = new User();
                BeanUtils.copyProperties(user, tbUser);
                return user;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 修改昵称
     */
    @Override
    public void updateNickname(TbUser user) {
        try {
            userMapper.updateByPrimaryKeySelective(user);

        } catch (Exception e) {
            throw new RuntimeException("修改昵称失败.");
        }
    }

    /**
     * 上传头像
     *
     * @param file
     * @param userId
     * @return
     */
    @Override
    public User upload(MultipartFile file, String userId) {
        try {
            //fastdfs会返回图片路径
            String url = fastDFSClient.uploadFile(file);
            //小图文件名:_150*150.后缀
            String[] fileNames = url.split("\\.");
            String fileName = fileNames[0];
            String ext = fileNames[1];
            //设置缩略图名字
            String suffix = "_150x150.";
            String picSmallUrl = fileName + suffix + ext;
            TbUser tbUser = userMapper.selectByPrimaryKey(userId);
            String prefix = env.getProperty("fdfs.httpurl");
            //设置头像大图片
            tbUser.setPicNormal(prefix + url);
            //设置小图片
            tbUser.setPicSmall(prefix + picSmallUrl);
            userMapper.updateByPrimaryKey(tbUser);
            //将用户信息返回
            User user = new User();
            BeanUtils.copyProperties(user, tbUser);
            return user;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 注册
     *
     * @param user
     */
    @Override
    public void register(TbUser user) {
        try {
            //设置查询条件,判断是否存在该用户
            TbUserExample example = new TbUserExample();
            TbUserExample.Criteria criteria = example.createCriteria();
            criteria.andUsernameEqualTo(user.getUsername());
            //进行查询
            List<TbUser> users = userMapper.selectByExample(example);
            //用户存在
            if (!CollectionUtils.isEmpty(users)) {
                throw new RuntimeException("用户已存在.");
            }
            //不存在该用户,则进行注册
            //对密码进行加密
            String encodingPwd = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
            user.setPassword(encodingPwd);
            user.setPicNormal(null);
            user.setPicSmall(null);
            user.setNickname(user.getUsername());
            String tempDir = env.getProperty("hcat.tmpdir") + user.getUsername() + ".png";
            String content = "hichat://" + user.getUsername();
            qrCodeUtils.createQRCode(tempDir, content);
            String url = fastDFSClient.uploadFile(new File(tempDir));
            String prefix = env.getProperty("fdfs.httpurl");
            user.setQrcode(prefix + url);
            user.setCreatetime(new Date());
            user.setId(idWorker.nextId());
            userMapper.insert(user);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public User login(String username, String password) {
        try {
            //判空
            if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
                //设置查询条件
                TbUserExample example = new TbUserExample();
                TbUserExample.Criteria criteria = example.createCriteria();
                criteria.andUsernameEqualTo(username);
                //进行查询
                List<TbUser> users = userMapper.selectByExample(example);
                //判断查询结果是否只有一条
                if (users != null && users.size() == 1) {
                    //对密码进行校验
                    String encodingPwd = DigestUtils.md5DigestAsHex(password.getBytes());
                    if (encodingPwd.equals(users.get(0).getPassword())) {
                        //封装user,并返回
                        User user = new User();
                        BeanUtils.copyProperties(user, users.get(0));
                        return user;
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据用户名搜索用户
     *
     * @param userid
     * @param friendUsername
     * @return
     */
    @Override
    public User findByUsername(String userid, String friendUsername) {
        try {
            TbUserExample example = new TbUserExample();
            TbUserExample.Criteria criteria = example.createCriteria();
            criteria.andUsernameEqualTo(friendUsername);
            List<TbUser> tbUsers = userMapper.selectByExample(example);
            if (tbUsers != null && tbUsers.size() == 1) {
                User friendUser = new User();
                BeanUtils.copyProperties(friendUser, tbUsers.get(0));
                return friendUser;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
