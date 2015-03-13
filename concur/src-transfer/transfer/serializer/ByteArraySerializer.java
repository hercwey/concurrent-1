package transfer.serializer;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import transfer.Outputable;
import transfer.compile.AsmSerializerContext;
import transfer.def.Types;
import transfer.utils.BitUtils;
import transfer.utils.IdentityHashMap;

import java.lang.reflect.Type;

/**
 * 字节数组编码器 Created by Jake on 2015/2/26.
 */
public class ByteArraySerializer implements Serializer, Opcodes {

	@Override
	public void serialze(Outputable outputable, Object object,
			IdentityHashMap referenceMap) {

		if (object == null) {
			NULL_SERIALIZER.serialze(outputable, object, referenceMap);
			return;
		}

		outputable.putByte(Types.BYTE_ARRAY);

		byte[] bytes = (byte[]) object;

		BitUtils.putInt(outputable, bytes.length);

		outputable.putBytes(bytes);

	}

	@Override
	public void compile(Type type, MethodVisitor mv,
			AsmSerializerContext context) {

		mv.visitCode();
		mv.visitVarInsn(ALOAD, 2);
		Label l1 = new Label();
		mv.visitJumpInsn(IFNONNULL, l1);

		mv.visitVarInsn(ALOAD, 1);
		mv.visitInsn(ICONST_1);
		mv.visitMethodInsn(INVOKEINTERFACE, "transfer/Outputable", "putByte",
				"(B)V", true);

		mv.visitInsn(RETURN);
		mv.visitLabel(l1);

		mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);

		mv.visitVarInsn(ALOAD, 1);
		mv.visitIntInsn(BIPUSH, (int) Types.BYTE_ARRAY);
		mv.visitMethodInsn(INVOKEINTERFACE, "transfer/Outputable", "putByte",
				"(B)V", true);

		mv.visitVarInsn(ALOAD, 2);
		mv.visitTypeInsn(CHECKCAST, "[B");
		mv.visitVarInsn(ASTORE, 4);

		mv.visitVarInsn(ALOAD, 1);
		mv.visitVarInsn(ALOAD, 4);
		mv.visitInsn(ARRAYLENGTH);
		mv.visitMethodInsn(INVOKESTATIC, "transfer/utils/BitUtils", "putInt",
				"(Ltransfer/Outputable;I)V", false);

		mv.visitVarInsn(ALOAD, 1);
		mv.visitVarInsn(ALOAD, 4);
		mv.visitMethodInsn(INVOKEINTERFACE, "transfer/Outputable", "putBytes",
				"([B)V", true);

		mv.visitInsn(RETURN);

		mv.visitMaxs(4, 5);
		mv.visitEnd();

	}

	private static ByteArraySerializer instance = new ByteArraySerializer();

	public static ByteArraySerializer getInstance() {
		return instance;
	}

}
