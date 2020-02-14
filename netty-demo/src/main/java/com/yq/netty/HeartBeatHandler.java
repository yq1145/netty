package com.yq.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                System.out.println("读空闲事件触发....");
            } else if (idleStateEvent.state() == IdleState.WRITER_IDLE) {

                System.out.println("写空闲事件触发...");
            } else if (idleStateEvent.state() == IdleState.ALL_IDLE) {
                System.out.println("========================");
                System.out.println("读写事件触发....");
                System.out.println("关闭通道...");
                ctx.channel().close();
            }
        }
    }
}
