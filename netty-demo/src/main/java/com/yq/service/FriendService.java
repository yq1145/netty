package com.yq.service;

import com.yq.pojo.vo.User;

import java.util.List;

public interface FriendService {
    /**
     * 发送好友请求
     *
     * @param fromUserid
     * @param toUserid
     */
    void sendRequest(String fromUserid, String toUserid);

    /**
     * 根据用户id查询好友请求
     *
     * @param userid
     * @return
     */
    List<User> findFriendReqByUserid(String userid);

    /**
     * 接受好友请求
     *
     * @param reqid
     * @return
     */
    void acceptFriendReq(String reqid);

    /**
     * 忽略好友请求
     *
     * @param reqid
     * @return
     */
    void ignoreFriendReq(String reqid);

    /**
     * 根据用户id查询好友请求列表
     *
     * @param userid
     * @return
     */
    List<User> findFriendByUserid(String userid);
}
