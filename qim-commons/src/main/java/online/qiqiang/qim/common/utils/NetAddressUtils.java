package online.qiqiang.qim.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author qiqiang
 */
public class NetAddressUtils {
    public static String local() {
        try {
            InetAddress[] all = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
            for (InetAddress inetAddress : all) {
                System.out.println(inetAddress.getHostAddress());
            }
            return "";
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println(local());
    }
}