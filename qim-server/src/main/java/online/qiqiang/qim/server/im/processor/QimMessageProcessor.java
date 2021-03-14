package online.qiqiang.qim.server.im.processor;

import online.qiqiang.qim.protocol.msg.QimMsg;

/**
 * @author qiqiang
 */
public interface QimMessageProcessor<M extends QimMsg> {
    /**
     * 是否继续传播
     *
     * @param context
     * @return
     */
    boolean process(ProcessorContext<M> context);

    boolean active(ProcessorContext<M> context);
}