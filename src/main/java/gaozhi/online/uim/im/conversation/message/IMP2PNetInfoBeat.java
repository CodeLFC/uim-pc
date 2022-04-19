package gaozhi.online.uim.im.conversation.message;

import com.google.gson.Gson;
import gaozhi.online.ubtb.core.util.ByteUtil;
import gaozhi.online.uim.im.entity.UClient;
import gaozhi.online.uim.utils.DateTimeUtil;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO
 * @date 2022/4/18 16:43
 */
public class IMP2PNetInfoBeat {

    /**
     * @author LiFucheng
     * @version 1.0
     * @description: TODO p2p心跳
     * @date 2022/4/18 16:07
     */
    public static class DataCoder implements IMMessage.DataCoder<UClient>{
        private final Gson gson = new Gson();
        @Override
        public IMMessage.IMMsgType getMsgType() {
            return IMMessage.IMMsgType.P2PNetInfoBeat;
        }

        @Override
        public byte[] parse2Binary(UClient data) {
            return ByteUtil.stringToByte(gson.toJson(data));
        }

        @Override
        public UClient parse2T(byte[] data) {
            return gson.fromJson(ByteUtil.bytesToString(data),UClient.class);
        }

        @Override
        public String parse2Tip(byte[] data) {
            UClient friend = gson.fromJson(ByteUtil.bytesToString(data),UClient.class);
            return DateTimeUtil.getHMSTime(friend.getUpdateTime());
        }
    }
}
