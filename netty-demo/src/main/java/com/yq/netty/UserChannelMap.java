package com.yq.netty;

import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 建立用户id与通道的关联
 */
public class UserChannelMap {
    //用户保存用户id与通道的map对象
    private static Map<String, Channel> userChannelMap;

    static {
        userChannelMap = new HashMap<String, Channel>();
    }

    /**
     * 添加通道
     *
     * @param userid
     * @param channel
     */
    public static void put(String userid, Channel channel) {
        userChannelMap.put(userid, channel);
    }

    /**
     * 根据好友id获取通道
     *
     * @param friendId
     * @return
     */
    public static Channel get(String friendId) {
        return userChannelMap.get(friendId);
    }

    /**
     * 根据通道id关闭通道
     *
     * @param channelId
     */
    public static void removeByChannelId(String channelId) {
        if (StringUtils.isNotBlank(channelId)) {
            return;
        }
        for (String s : userChannelMap.keySet()) {
            Channel channel = userChannelMap.get(s);
            if (channelId.equals(channel.id().asLongText())) {
                System.out.println(" 客户端断开;用户id为: " + s + "通道为: " + channelId);
                userChannelMap.remove(s);
            }
        }
    }

    /**
     * 移除通道
     *
     * @param userid
     */
    public static void remove(String userid) {
        userChannelMap.remove(userid);
    }

    /**
     * 打印所有的map信息
     */
    public static void print() {
        for (String s : userChannelMap.keySet()) {
            System.out.println("用户id: " + s + " 通道: " + userChannelMap.get(s));
        }
    }
}
