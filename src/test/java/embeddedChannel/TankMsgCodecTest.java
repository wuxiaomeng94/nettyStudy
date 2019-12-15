package embeddedChannel;

import com.demo.netty.t3.TankMsg;
import com.demo.netty.t3.TankMsgDecoder;
import com.demo.netty.t3.TankMsgEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

public class TankMsgCodecTest {

    @Test
    public void testTankMsgEncoder() {
        TankMsg tankMsg = new TankMsg(8, 10);
        EmbeddedChannel channel = new EmbeddedChannel(new TankMsgEncoder());
        channel.writeOutbound(tankMsg);

        ByteBuf buf = (ByteBuf) channel.readOutbound();
        int x = buf.readInt();
        int y = buf.readInt();

        Assert.assertTrue(x == 8 && y == 10);
        buf.release();
    }

    @Test
    public void testTankMsgEncoder2() {
        ByteBuf buf = Unpooled.buffer();
        TankMsg msg = new TankMsg(8, 10);
        buf.writeInt(msg.x);
        buf.writeInt(msg.y);
        EmbeddedChannel channel = new EmbeddedChannel(new TankMsgEncoder(), new TankMsgDecoder());
        channel.writeInbound(buf.duplicate());

        TankMsg tankMsg = (TankMsg) channel.readInbound();

        Assert.assertTrue(tankMsg.x == 8 && tankMsg.y == 10);
    }

}
