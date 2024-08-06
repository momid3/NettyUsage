package com.momid

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.util.CharsetUtil

class ServerHandler : SimpleChannelInboundHandler<ByteBuf>() {

    override fun channelRead0(ctx: ChannelHandlerContext, msg: ByteBuf) {
        val received = msg.toString(CharsetUtil.UTF_8)
        println("received from client: $received")
        ctx.writeAndFlush(ctx.alloc().buffer().writeBytes("hello client\n".toByteArray(CharsetUtil.UTF_8)))
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }
}
