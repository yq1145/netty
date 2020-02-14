package com.yq.service;

import com.yq.pojo.TbChatRecord;

import java.util.List;

public interface ChatRecordService {

    /**
     * 添加消息
     *
     * @param chatRecord
     */
    void insert(TbChatRecord chatRecord);

    /**
     * 根据用户id和朋友id查询聊天记录
     *
     * @param userid
     * @param friendid
     * @return
     */
    List<TbChatRecord> findByUserIdAndFriendId(String userid, String friendid);

    /**
     * 根据用户id查询未读的记录
     *
     * @param userid
     * @return
     */
    List<TbChatRecord> findUnreadByUserid(String userid);

    /**
     * 设置消息为已读
     *
     * @param id
     */
    void updateStatusHasRead(String id);
}
