package com.yq.service.impl;

import com.yq.mapper.TbFriendMapper;
import com.yq.mapper.TbFriendReqMapper;
import com.yq.mapper.TbUserMapper;
import com.yq.pojo.*;
import com.yq.pojo.vo.User;
import com.yq.service.FriendService;
import com.yq.utils.IdWorker;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class FriendServiceImpl implements FriendService {

    @Autowired
    private IdWorker idWorker;
    @Autowired
    private TbUserMapper userMapper;
    @Autowired
    private TbFriendMapper friendMapper;
    @Autowired
    private TbFriendReqMapper friendReqMapper;

    @Override
    public void sendRequest(String fromUserid, String toUserid) {
        //对添加操作进行校验
        //1.不能添加自己为好友
        if (fromUserid.equals(toUserid)) {
            throw new RuntimeException("添加好友失败");
        }
        //2.已经添加的好友不能再次添加
        TbFriendExample example = new TbFriendExample();
        TbFriendExample.Criteria criteria = example.createCriteria();
        criteria.andUseridEqualTo(fromUserid);
        criteria.andFriendsIdEqualTo(toUserid);
        List<TbFriend> tbFriends = friendMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(tbFriends)) {
            throw new RuntimeException("添加好友失败");
        }
        //3.已经进行好友申请的,不能再次申请
        TbFriendReqExample reqExample = new TbFriendReqExample();
        TbFriendReqExample.Criteria criteria2 = reqExample.createCriteria();
        criteria2.andFromUseridEqualTo(fromUserid);
        criteria2.andToUseridEqualTo(toUserid);
        List<TbFriendReq> tbFriendReqs = friendReqMapper.selectByExample(reqExample);
        if (!CollectionUtils.isEmpty(tbFriendReqs)) {
            throw new RuntimeException("添加好友失败");
        }
        //校验成功进行添加
        try {
            TbFriendReq friendReq = new TbFriendReq();
            friendReq.setCreatetime(new Date());
            friendReq.setFromUserid(fromUserid);
            friendReq.setToUserid(toUserid);
            friendReq.setStatus(0);
            friendReq.setId(idWorker.nextId());
            friendReqMapper.insert(friendReq);
        } catch (Exception e) {
            e.getStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<User> findFriendReqByUserid(String userid) {
        try {
            TbFriendReqExample example = new TbFriendReqExample();
            TbFriendReqExample.Criteria criteria = example.createCriteria();
            criteria.andToUseridEqualTo(userid);
            criteria.andStatusEqualTo(0);
            List<TbFriendReq> friendReqs = friendReqMapper.selectByExample(example);
            List<User> users = new ArrayList<>();
            for (TbFriendReq friendReq : friendReqs) {
                User user = new User();
                //查询谁发送的请求的
                TbUser tbUser = userMapper.selectByPrimaryKey(friendReq.getFromUserid());
                BeanUtils.copyProperties(user, tbUser);
                users.add(user);
            }
            return users;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void acceptFriendReq(String reqid) {
        try {
            //修改req表中的status为1
            TbFriendReqExample example = new TbFriendReqExample();
            TbFriendReqExample.Criteria criteria = example.createCriteria();
            //查询来自哪里的id
            criteria.andFromUseridEqualTo(reqid);
            List<TbFriendReq> tbFriendReqs = friendReqMapper.selectByExample(example);
            if (CollectionUtils.isEmpty(tbFriendReqs)) {
                throw new RuntimeException("操作失败");
            }
            TbFriendReq tbFriendReq = tbFriendReqs.get(0);
            tbFriendReq.setStatus(1);
            friendReqMapper.updateByPrimaryKeySelective(tbFriendReq);
            //互相添加好友,friend表中添加两条记录
            TbFriend tbFriend1 = new TbFriend();
            tbFriend1.setCreatetime(new Date());
            tbFriend1.setId(idWorker.nextId());
            tbFriend1.setUserid(tbFriendReq.getToUserid());
            tbFriend1.setFriendsId(tbFriendReq.getFromUserid());
            friendMapper.insertSelective(tbFriend1);
            //添加用户2
            TbFriend tbFriend2 = new TbFriend();
            tbFriend2.setCreatetime(new Date());
            tbFriend2.setId(idWorker.nextId());
            tbFriend2.setUserid(tbFriendReq.getFromUserid());
            tbFriend2.setFriendsId(tbFriendReq.getToUserid());
            friendMapper.insertSelective(tbFriend2);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("操作失败");
        }

    }

    @Override
    public void ignoreFriendReq(String reqid) {
        try {
            //修改req表中的status为1
            TbFriendReqExample example = new TbFriendReqExample();
            TbFriendReqExample.Criteria criteria = example.createCriteria();
            criteria.andFromUseridEqualTo(reqid);
            List<TbFriendReq> tbFriendReqs = friendReqMapper.selectByExample(example);
            TbFriendReq tbFriendReq = tbFriendReqs.get(0);
            tbFriendReq.setStatus(1);
            friendReqMapper.updateByPrimaryKeySelective(tbFriendReq);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("操作失败");
        }
    }

    @Override
    public List<User> findFriendByUserid(String userid) {
        try {
            TbFriendExample example = new TbFriendExample();
            TbFriendExample.Criteria criteria = example.createCriteria();
            criteria.andUseridEqualTo(userid);
            List<TbFriend> tbFriends = friendMapper.selectByExample(example);
            List<User> users = new ArrayList<>();
            for (TbFriend friend : tbFriends) {
                User user = new User();
                TbUser tbUser = userMapper.selectByPrimaryKey(friend.getFriendsId());
                BeanUtils.copyProperties(user, tbUser);
                users.add(user);
            }
            return users;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("操作失败");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("操作失败");
        }
    }
}
