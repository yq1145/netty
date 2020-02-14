package com.yq.netty;

import com.alibaba.fastjson.JSON;
import com.yq.pojo.TbChatRecord;
import com.yq.service.ChatRecordService;
import com.yq.utils.SpringUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 处理消息的handler
 * TextWebSocketFrame: 在netty中，是用于为websocket专门处理文本的对象，frame是消息的载体
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    // 用于记录和管理所有客户端的 Channel
    private static ChannelGroup clients = new
            DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame
            msg) throws Exception {
        // 获取从客户端传输过来的消息
        String text = msg.text();
        System.out.println(" 接收到的数据:" + text);
        Message message = JSON.parseObject(text, Message.class);
        //通过springutils工具类获取spring上下文容器
        ChatRecordService chatRecordService = SpringUtil.getBean(ChatRecordService.class);
        switch (message.getType()) {
            //处理客户端连接的消息
            case 0:
                //建立用户与导通的关联
                String userid = message.getChatRecord().getUserid();
                UserChannelMap.put(userid, ctx.channel());
                System.out.println("建立用户: " + userid + " 与通道" + ctx.channel().id() + "的联系");
                break;
            //处理客户端发送好友消息
            case 1:
                System.out.println("接受到用户消息...");
                //将链条消息报讯到数据库中
                TbChatRecord chatRecord = message.getChatRecord();
                chatRecordService.insert(chatRecord);
                //1.如果发送消息好友在线,可以直接将消息发送给好友
                Channel channel = UserChannelMap.get(chatRecord.getFriendid());
                if (channel != null) {
                    channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
                } else {
                    //2.如果不在线,暂时不发送
                    //为空则,用户不在线
                    System.out.println("用户不在线: 暂时不发送信息...");
                }
                break;
            case 2:
                System.out.println("用户签收消息....");
                //将消息hasred属性设置为已读
                chatRecordService.updateStatusHasRead(message.getChatRecord().getId());
                break;
            case 3:
                //接受心跳消息
                System.out.println("接受心跳消息..." + JSON.toJSONString(message));
                break;
        }

    }

    /**
     * 当客户端连接服务端之后（打开连接）
     * 获取客户端的 channel，并且放入到 ChannelGroup 中去进行管理
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 将 channel 添加到客户端
        clients.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 当触发 handlerRemoved，ChannelGroup 会自动移除对应客户端的 channel
        //clients.remove(ctx.channel());
        // asLongText()——唯一的 ID
        // asShortText()——短 ID（有可能会重复)
//        System.out.println(" 客户端断开, channel 对应的长 id 为:" +
//                ctx.channel().id().asLongText());
        //        System.out.println(" 客户端断开, channel 对应的短 id 为:" +
        //                ctx.channel().id().asShortText());
        UserChannelMap.removeByChannelId(ctx.channel().id().asLongText());
        ctx.channel().close();
        UserChannelMap.print();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        UserChannelMap.removeByChannelId(ctx.channel().id().asLongText());
        ctx.channel().close();
    }
}
