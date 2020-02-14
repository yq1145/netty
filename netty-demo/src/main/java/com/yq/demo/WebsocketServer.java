package com.yq.demo;

public class WebsocketServer {
    public static void main(String[] args) throws InterruptedException {
       /* // 初始化主线程池（ boss 线程池）
        NioEventLoopGroup mainGroup = new NioEventLoopGroup();
        // 初始化从线程池 (worker 线程池 )
        NioEventLoopGroup subGroup = new NioEventLoopGroup();
        try {
            // 创建服务器启动器
            ServerBootstrap b = new ServerBootstrap();
            // 指定使用主线程池和从线程池
            b.group(mainGroup, subGroup)
                    // 指定使用 Nio 通道类型
                    .channel(NioServerSocketChannel.class)
                    // 指定通道初始化器加载通道处理器
                    .childHandler(new WsServerInitializer());
            // 绑定端口号启动服务器，并等待服务器启动
            // ChannelFuture 是 Netty 的回调消息
            ChannelFuture future = b.bind(9001).sync();
            // 等待服务器 socket 关闭
            future.channel().closeFuture().sync();
        } finally {
            // 优雅关闭 boos 线程池和 worker 线程池
            mainGroup.shutdownGracefully();
            subGroup.shutdownGracefully();
        }*/
    }
}