package com.yq.service.impl;

import com.yq.mapper.TbChatRecordMapper;
import com.yq.pojo.TbChatRecord;
import com.yq.pojo.TbChatRecordExample;
import com.yq.service.ChatRecordService;
import com.yq.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ChatRecordServiceImpl implements ChatRecordService {
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private TbChatRecordMapper chatRecordMapper;

    @Override
    public List<TbChatRecord> findByUserIdAndFriendId(String userid, String friendid) {
        //userid -->friendId 的聊天记录
        TbChatRecordExample example = new TbChatRecordExample();
        TbChatRecordExample.Criteria criteria1 = example.createCriteria();
        TbChatRecordExample.Criteria criteria2 = example.createCriteria();
        criteria1.andUseridEqualTo(userid);
        criteria1.andFriendidEqualTo(friendid);
        criteria1.andHasDeleteEqualTo(0);
        //friendId -->userid 的聊天记录
        criteria2.andUseridEqualTo(friendid);
        criteria2.andFriendidEqualTo(userid);
        criteria2.andHasDeleteEqualTo(0);
        example.or(criteria1);
        example.or(criteria2);

        //将发给userid的所有消息设置为已读
        TbChatRecordExample exampleQuerySendToMe = new TbChatRecordExample();
        TbChatRecordExample.Criteria criteria = exampleQuerySendToMe.createCriteria();
        //设置查询条件
        criteria.andFriendidEqualTo(userid);
        criteria.andHasReadEqualTo(0);
        List<TbChatRecord> records = chatRecordMapper.selectByExample(exampleQuerySendToMe);
        for (TbChatRecord record : records) {
            record.setHasRead(1);
            chatRecordMapper.updateByPrimaryKey(record);
        }
        return chatRecordMapper.selectByExample(example);
    }

    @Override
    public List<TbChatRecord> findUnreadByUserid(String userid) {
        TbChatRecordExample example = new TbChatRecordExample();
        TbChatRecordExample.Criteria criteria = example.createCriteria();
        criteria.andFriendidEqualTo(userid);
        criteria.andHasReadEqualTo(0);
        List<TbChatRecord> records = chatRecordMapper.selectByExample(example);
        return records;
    }

    @Override
    public void insert(TbChatRecord chatRecord) {
        chatRecord.setId(idWorker.nextId());
        chatRecord.setHasRead(0);
        chatRecord.setCreatetime(new Date());
        chatRecord.setHasDelete(0);
        chatRecordMapper.insert(chatRecord);

    }

    @Override
    public void updateStatusHasRead(String id) {
        TbChatRecord tbChatRecord = chatRecordMapper.selectByPrimaryKey(id);
        tbChatRecord.setHasRead(1);
        chatRecordMapper.updateByPrimaryKey(tbChatRecord);
    }
}
