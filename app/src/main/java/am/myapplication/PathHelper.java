package am.myapplication;

/**
 * 作者：林冠宏
 * <p>
 * My GitHub : https://github.com/af913337456/
 * <p>
 * My Blog   : http://www.cnblogs.com/linguanh/
 * <p>
 * on 2017/9/9.
 */

/**
 *  以最小的 o(x∈z) 获取锚点 ， o(n) <~ n^2 == width*height
 *
 * */

public class PathHelper {

    static {
        System.loadLibrary("LghBitmapHelper");
    }

    public interface CallbackBehaviour {
        public void callback(int index, int x, int y);
    }

    /** jni 调用 java 接口 */
    public native void FastGetData(
            CallbackBehaviour callbackBehaviour,
            Object bitmap,
            int xStep,
            int yStep
    );



}
