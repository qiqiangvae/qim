package online.qiqiang.qim.common.utils;

import online.qiqiang.qim.common.vo.QimResponse;

/**
 * @author qiqiang
 */
public class QimResponseUtils {
    public static boolean isOk(QimResponse<?> qimResponse) {
        return qimResponse != null && "OK".equals(qimResponse.getOk());
    }
}