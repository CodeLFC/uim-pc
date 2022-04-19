package gaozhi.online.uim.im.conversation.message;

import gaozhi.online.ubtb.core.util.ByteUtil;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO
 * @date 2022/4/18 16:43
 */
public class IMString {

    /**
     * @author LiFucheng
     * @version 1.0
     * @description: TODO  文本消息类型
     * @date 2022/3/23 15:38
     */
    public static class DataCoder implements IMMessage.DataCoder<String> {
        @Override
        public IMMessage.IMMsgType getMsgType() {
            return IMMessage.IMMsgType.TEXT;
        }

        @Override
        public byte[] parse2Binary(String data) {
            return ByteUtil.stringToByte(data);
        }

        @Override
        public String parse2T(byte[] data) {
            return ByteUtil.bytesToString(data);
        }

        @Override
        public String parse2Tip(byte[] data) {
            return ByteUtil.bytesToString(data);
        }
    }
}
