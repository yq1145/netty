package com.yq.controller;

import com.yq.pojo.TbFriendReq;
import com.yq.pojo.vo.Result;
import com.yq.pojo.vo.User;
import com.yq.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    /**
     * 发送好友请求
     *
     * @param friendReq
     * @return
     */
    @RequestMapping("sendRequest")
    public Result sendRequest(@RequestBody TbFriendReq friendReq) {
        try {
            friendService.sendRequest(friendReq.getFromUserid(), friendReq.getToUserid());
            return new Result(true, "发送请求成功");
        } catch (RuntimeException r) {
            return new Result(false, r.getMessage());
        } catch (Exception e) {
            return new Result(false, "发送请求失败");
        }
    }

    /**
     * 根据用户id查询好友请求
     *
     * @param userid
     * @return
     */
    @RequestMapping("findFriendReqByUserid")
    public List<User> findFriendReqByUserid(String userid) {
        List<User> list = friendService.findFriendReqByUserid(userid);
        return list;
    }

    /**
     * 接受好友请求
     *
     * @param reqid
     * @return
     */
    @RequestMapping("acceptFriendReq")
    public Result acceptFriendReq(String reqid) {
        friendService.acceptFriendReq(reqid);
        return new Result(true, "通过好友请求");
    }

    /**
     * 忽略好友请求
     *
     * @param reqid
     * @return
     */
    @RequestMapping("ignoreFriendReq")
    public Result ignoreFriendReq(String reqid) {
        friendService.ignoreFriendReq(reqid);
        return new Result(true, "忽略好友请求");
    }

    /**
     * 根据用户id查询好友请求列表
     *
     * @param userid
     * @return
     */
    @RequestMapping("findFriendByUserid")
    public List<User> findFriendByUserid(String userid) {
        List<User> list = friendService.findFriendByUserid(userid);
        return list;

    }

}
