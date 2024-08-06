package com.momid

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

class ClientHandler : SimpleChannelInboundHandler<String>() {

    override fun channelActive(ctx: ChannelHandlerContext) {
        ctx.writeAndFlush("hello server")
    }

    override fun channelRead0(ctx: ChannelHandlerContext, msg: String) {
        println("received from server: $msg")
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }
}
