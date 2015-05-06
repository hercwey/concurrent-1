package transfer.deserializer;

import org.objectweb.asm.MethodVisitor;

import transfer.Inputable;
import transfer.compile.AsmDeserializerContext;
import transfer.core.ClassInfo;
import transfer.core.FieldInfo;
import transfer.def.PersistConfig;
import transfer.def.Types;
import transfer.exceptions.IllegalClassTypeException;
import transfer.exceptions.IllegalTypeException;
import transfer.exceptions.UnsupportDeserializerTypeException;
import transfer.utils.BitUtils;
import transfer.utils.IntegerMap;
import transfer.utils.TypeUtils;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 * 带标签的对象解析器
 * Created by Jake on 2015/2/23.
 */
public class TagObjectDeSerializer implements Deserializer {

    /**
     * 属性名解析器
     */
    private static final ShortStringDeserializer STRING_DESERIALIZER = ShortStringDeserializer.getInstance();


    @Override
    public <T> T deserialze(Inputable inputable, Type type, byte flag, IntegerMap referenceMap) {

        byte typeFlag = PersistConfig.getType(flag);
        if (typeFlag != Types.OBJECT) {
            throw new IllegalTypeException(typeFlag, Types.OBJECT, type);
        }

        // 读取对象类型
        int classId = BitUtils.getInt(inputable);
        Class<?> rawClass = TypeUtils.getRawClass(type);
        if (type == null 
        		|| type == Object.class
        		|| rawClass.isInterface()
				|| Modifier.isAbstract(rawClass.getModifiers()) && !rawClass.isArray()) {

            rawClass = PersistConfig.getClass(classId);
        }

        if (rawClass == null) {
            throw new UnsupportDeserializerTypeException(type);
        }

        ClassInfo classInfo = PersistConfig.getOrCreateClassInfo(rawClass);

        if (classInfo == null) {
            throw new UnsupportDeserializerTypeException(rawClass);
        }

        if (classId != classInfo.getClassId()) {
            throw new IllegalClassTypeException(classId, type);
        }

        Object object;
        try {
            object = rawClass.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("create instane error, class " + rawClass.getName());
        }



        Type fieldType;
        String fieldName;
        Object fieldValue;
        FieldInfo fieldInfo;
        Deserializer fieldDeserializer;

        // 读取属性数量
        int fieldNum = BitUtils.getInt(inputable);

        for (int i = 0;i < fieldNum;i++) {

            fieldName = STRING_DESERIALIZER.deserialze(inputable, String.class, inputable.getByte(), referenceMap);
            fieldInfo = classInfo.getFieldInfo(fieldName);

            byte fieldFlag = inputable.getByte();
            fieldType = fieldInfo != null ? fieldInfo.getType() : Object.class;

            fieldDeserializer = PersistConfig.getDeserializer(fieldType, fieldFlag);
            fieldValue = fieldDeserializer.deserialze(inputable, fieldType, fieldFlag, referenceMap);

            if (fieldInfo == null) {// 略过不存在的属性
                continue;
            }
            
            fieldInfo.setField(object, fieldValue);

        }

        return (T) object;
    }

    
    @Override
	public void compile(Type type, MethodVisitor mw,
			AsmDeserializerContext context) {
    	
	}

    private static TagObjectDeSerializer instance = new TagObjectDeSerializer();

    public static TagObjectDeSerializer getInstance() {
        return instance;
    }

}
