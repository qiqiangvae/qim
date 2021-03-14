package online.qiqiang.qim.server.im;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import online.qiqiang.qim.protocol.ImProtocol;
import online.qiqiang.qim.protocol.msg.MsgType;
import online.qiqiang.qim.server.im.processor.Processor;
import online.qiqiang.qim.server.im.processor.ProcessorContainer;
import online.qiqiang.qim.server.im.processor.QimMessageProcessor;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qiqiang
 */
@ChannelHandler.Sharable
@Component
public class ServerQimProtocolHandler extends SimpleChannelInboundHandler<ImProtocol> implements ApplicationContextAware, ApplicationRunner {
    private Map<MsgType, ProcessorContainer> contextMap;
    private ApplicationContext applicationContext;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ImProtocol protocol) throws Exception {
        int msgType = protocol.getMsgType();
        ProcessorContainer handlerContainer = contextMap.get(MsgType.type(msgType));
        handlerContainer.process(protocol,ctx.executor());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        contextMap = new HashMap<>();
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, QimMessageProcessor> handlers = applicationContext.getBeansOfType(QimMessageProcessor.class);
        for (QimMessageProcessor messageProcessor : handlers.values()) {
            Processor processor = messageProcessor.getClass().getDeclaredAnnotation(Processor.class);
            ProcessorContainer context = contextMap.getOrDefault(processor.type(), new ProcessorContainer(processor.type()));
            context.getHandlers().add(messageProcessor);
            contextMap.put(processor.type(), context);
        }
        for (ProcessorContainer context : contextMap.values()) {
            context.getHandlers().sort(AnnotationAwareOrderComparator.INSTANCE);
        }
    }
}