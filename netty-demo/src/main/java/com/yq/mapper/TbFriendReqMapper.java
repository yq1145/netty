package com.yq.mapper;

import com.yq.pojo.TbFriendReq;
import com.yq.pojo.TbFriendReqExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbFriendReqMapper {
    int countByExample(TbFriendReqExample example);

    int deleteByExample(TbFriendReqExample example);

    int deleteByPrimaryKey(String id);

    int insert(TbFriendReq record);

    int insertSelective(TbFriendReq record);

    List<TbFriendReq> selectByExample(TbFriendReqExample example);

    TbFriendReq selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TbFriendReq record, @Param("example") TbFriendReqExample example);

    int updateByExample(@Param("record") TbFriendReq record, @Param("example") TbFriendReqExample example);

    int updateByPrimaryKeySelective(TbFriendReq record);

    int updateByPrimaryKey(TbFriendReq record);
}