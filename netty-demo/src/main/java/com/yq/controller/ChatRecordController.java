package com.yq.controller;

import com.yq.pojo.TbChatRecord;
import com.yq.service.ChatRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("chatrecord")
public class ChatRecordController {

    @Autowired
    private ChatRecordService chatRecordService;

    /**
     * 根据用户id和朋友id查询聊天记录
     *
     * @param userid
     * @param friendid
     * @return
     */
    @RequestMapping("findByUserIdAndFriendId")
    public List<TbChatRecord> findByUserIdAndFriendId(String userid, String friendid) {
        List<TbChatRecord> records = chatRecordService.findByUserIdAndFriendId(userid, friendid);
        return records;
    }

    /**
     * 根据用户id查询未读的消息
     *
     * @param userid
     * @return
     */
    @RequestMapping("findUnreadByUserid")
    public List<TbChatRecord> findUnreadByUserid(String userid) {
        List<TbChatRecord> records = chatRecordService.findUnreadByUserid(userid);
        return records;
    }


}
