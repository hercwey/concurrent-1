package transfer.test;

import dbcache.test.Entity;
import dbcache.utils.JsonUtils;
import transfer.ByteArray;
import transfer.Transfer;
import transfer.def.TransferConfig;

/**
 * Created by Administrator on 2015/2/26.
 */
public class TestEncodePerform {

    public static void main(String[] args) {

        TransferConfig.registerClass(Entity.class, 1);

        Entity entity = new Entity();
        entity.setUid(101);
        entity.getFriends().add(1l);
        entity.getFriends().add(2l);
        entity.getFriends().add(3l);

        long t1 = 0l;


        t1 = System.currentTimeMillis();
        for (int i = 0; i < 10000000;i++) {
            JsonUtils.object2Bytes(entity);
        }
        System.out.println(System.currentTimeMillis() - t1);



        t1 = System.currentTimeMillis();
        for (int i = 0; i < 10000000;i++) {
            JsonUtils.object2JsonString(entity).getBytes();
        }
        System.out.println(System.currentTimeMillis() - t1);



        t1 = System.currentTimeMillis();
        ByteArray byteArray = Transfer.encode(entity);
        System.out.println(byteArray.toBytes().length);
        for (int i = 0; i < 10000000;i++) {
            byteArray = Transfer.encode(entity);
        }
        System.out.println(System.currentTimeMillis() - t1);
        
        
        
        t1 = System.currentTimeMillis();
        for (int i = 0; i < 10000000;i++) {
        	JacksonUtils.object2JsonString(entity);
        }
        System.out.println(System.currentTimeMillis() - t1);
        
        
        
        

        System.out.println(JsonUtils.object2JsonString(entity).getBytes().length);
        System.out.println(JsonUtils.object2Bytes(entity).length);




    }

}