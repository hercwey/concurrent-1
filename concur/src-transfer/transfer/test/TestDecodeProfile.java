package transfer.test;

import transfer.ByteArray;
import transfer.Transfer;
import transfer.def.TransferConfig;

/**
 * Created by Administrator on 2015/2/26.
 */
public class TestDecodeProfile {

    public static void main(String[] args) {

        TransferConfig.registerClass(Entity.class, 1);

        Entity entity = new Entity();
        entity.setUid(101);
        entity.getFriends().add(1l);
        entity.getFriends().add(2l);
        entity.getFriends().add(3l);

        ByteArray byteArray = Transfer.encode(entity);

        byte[] bytes = byteArray.toBytes();

        long t1 = System.currentTimeMillis();

        for (int i = 0; i < 5000000;i++) {
            Entity entity1 = Transfer.decode(bytes, Entity.class);
        }

        System.out.println(System.currentTimeMillis() - t1);

    }

}