package online.qiqiang.qim.protocol;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author qiqiang
 */
public class HessianUtils {
    public static <T> T read(byte[] data, Class<T> clazz) {
        ByteArrayInputStream os = new ByteArrayInputStream(data);
        Hessian2Input input = new Hessian2Input(os);
        try {
            return (T) input.readObject(clazz);
        } catch (IOException e) {
            throw new ImProtocolException(e);
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] write(Object obj) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(os);
        try {
            output.writeObject(obj);
            output.close();
            return os.toByteArray();
        } catch (IOException e) {
            throw new ImProtocolException(e);
        }
    }
}