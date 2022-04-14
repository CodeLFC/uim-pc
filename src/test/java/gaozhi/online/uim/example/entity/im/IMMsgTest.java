package gaozhi.online.uim.example.entity.im;

import gaozhi.online.ubtb.core.util.ByteUtil;
import gaozhi.online.uim.example.im.entity.IMMsg;
import org.junit.jupiter.api.Test;

import java.util.Date;


class IMMsgTest {

    @Test
    public void testCodec(){

        StringBuilder builder = new StringBuilder();
        for(int i =0;i<100000;i++){
            builder.append(i+"->");
        }

        String temp =new String(builder);

        IMMsg msg = new IMMsg();
        msg.setFromId(10000);
        msg.setMsgType(1);
        msg.setRouteCount(10);
        msg.setToId(100001);
        msg.setTime(System.currentTimeMillis());
        msg.setData(ByteUtil.stringToByte(temp));
        IMMsg imMsg = IMMsg.Codec.decode(IMMsg.Codec.encode(msg));
        System.out.println(new Date(msg.getTime())+":"+ByteUtil.bytesToString(imMsg.getData()));
    }

}