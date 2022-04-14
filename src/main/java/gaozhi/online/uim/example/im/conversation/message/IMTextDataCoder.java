package gaozhi.online.uim.example.im.conversation.message;

import gaozhi.online.ubtb.core.util.ByteUtil;
import gaozhi.online.uim.example.im.conversation.IMMessage;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO  文本消息类型
 * @date 2022/3/23 15:38
 */
public class IMTextDataCoder implements IMMessage.DataCoder<String> {
    @Override
    public IMMsgType getMsgType() {
        return IMMsgType.TEXT;
    }

    @Override
    public byte[] parse2Binary(String data) {
        return ByteUtil.stringToByte(data);
    }

    @Override
    public String parse2T(byte[] data) {
        return ByteUtil.bytesToString(data);
    }
}
