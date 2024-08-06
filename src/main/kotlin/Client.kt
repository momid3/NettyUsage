package com.momid

import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory

fun startClient() {
    val group: EventLoopGroup = NioEventLoopGroup()
    try {
        // Create SSL context
        val sslContext: SslContext = SslContextBuilder.forClient()
            .trustManager(InsecureTrustManagerFactory.INSTANCE) // For self-signed certificates (Not recommended for production)
            .build()

        val bootstrap = Bootstrap()
        bootstrap.group(group)
            .channel(NioSocketChannel::class.java)
            .option(ChannelOption.TCP_NODELAY, true)
            .handler(object : ChannelInitializer<SocketChannel>() {
                @Throws(Exception::class)
                override fun initChannel(ch: SocketChannel) {
                    val pipeline = ch.pipeline()
                    // Add SSL handler first to encrypt and decrypt everything
                    pipeline.addLast(sslContext.newHandler(ch.alloc()))
                    // Add frame decoder and prepender
                    pipeline.addLast(LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4))
                    pipeline.addLast(LengthFieldPrepender(4))
                    pipeline.addLast(StringDecoder())
                    pipeline.addLast(StringEncoder())
                    // Add the main handler
                    pipeline.addLast(ClientHandler())
                }
            })

        // Connect to the server
        val channelFuture = bootstrap.connect("localhost", 8443).sync()
        println("client connected to server")

        // Wait until the connection is closed
        channelFuture.channel().closeFuture().sync()
    } finally {
        group.shutdownGracefully()
    }
}
