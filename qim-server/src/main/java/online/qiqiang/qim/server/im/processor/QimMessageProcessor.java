package online.qiqiang.qim.server.im.processor;

import online.qiqiang.qim.protocol.msg.QimMsg;

/**
 * @author qiqiang
 */
public interface QimMessageProcessor<M extends QimMsg> {
    /**
     * 处理
     *
     * @param context 上下文
     * @return 是否继续传播
     */
    boolean process(ProcessorContext<M> context);

    /**
     * 是否活动
     *
     * @param context 上下文
     * @return 是否活动
     */
    boolean active(ProcessorContext<M> context);
}