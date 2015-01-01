package dbcache.utils;

import org.objectweb.asm.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 类操作的工具集
 *
 * @author Jake
 * @date 2014年9月6日上午12:10:37
 */
public class AsmUtils implements Opcodes {


	/**
	 * 获取代理对象
	 * @param proxyClass 代理类
	 * @param entity 被代理实体
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getProxyEntity(Class<?> proxyClass, T entity) {
		Class<?>[] paramTypes = { entity.getClass() };
		Object[] params = { entity };
		Constructor<?> con;
		try {
			con = proxyClass.getConstructor(paramTypes);
			return (T) con.newInstance(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 获取代理对象
	 * @param proxyClass 代理类
	 * @param entity 被代理实体
	 * @param constructParams 构造方法的参数
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getProxyEntity(Class<?> proxyClass, T entity, Object... constructParams) {

		Class<?>[] paramTypes = new Class<?>[constructParams.length + 1];
		paramTypes[0] = entity.getClass();
		for(int i = 1; i < constructParams.length + 1;i ++) {
			paramTypes[i] = constructParams[i - 1].getClass().getInterfaces()[0];
		}

		Object[] params = new Object[constructParams.length + 1];
		params[0] = entity;
		for(int i = 1; i < constructParams.length + 1;i ++) {
			params[i] = constructParams[i - 1];
		}

		Constructor<?> con;
		try {
			con = proxyClass.getConstructor(paramTypes);
			return (T) con.newInstance(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 把类名中的"."替换为"/"
	 *
	 * @param className
	 * @return
	 */
	public static String toAsmCls(String className) {
		return className.replace('.', '/');
	}


	/**
	 * 判断是否需要重写方法
	 * <p>
	 * object类本身的方法不做重写
	 * </p>
	 * <p>
	 * "main" 方法不做重写
	 * </p>
	 *
	 * @param m
	 *            目标方法
	 * @return
	 */
	public static boolean needOverride(Method m) {
		// object类本身的方法不做重写
		if (m.getDeclaringClass().getName().equals(Object.class.getName())) {
			return false;
		}
		// "main" 方法不做重写
		if (Modifier.isPublic(m.getModifiers())
				&& Modifier.isStatic(m.getModifiers())
				&& m.getReturnType().getName().equals("void")
				&& m.getName().equals("main")) {
			return false;
		}
		return true;
	}


	public static String getPrimitiveLetter(Class<?> type) {
        if (Integer.TYPE.equals(type)) {
            return "I";
        } else if (Void.TYPE.equals(type)) {
            return "V";
        } else if (Boolean.TYPE.equals(type)) {
            return "Z";
        } else if (Character.TYPE.equals(type)) {
            return "C";
        } else if (Byte.TYPE.equals(type)) {
            return "B";
        } else if (Short.TYPE.equals(type)) {
            return "S";
        } else if (Float.TYPE.equals(type)) {
            return "F";
        } else if (Long.TYPE.equals(type)) {
            return "J";
        } else if (Double.TYPE.equals(type)) {
            return "D";
        }

        throw new IllegalStateException("Type: " + type.getCanonicalName() + " is not a primitive type");
    }


    public static java.lang.reflect.Type getMethodType(Class<?> clazz, String methodName) {
        try {
            Method method = clazz.getMethod(methodName);

            return method.getGenericReturnType();
        } catch (Exception ex) {
            return null;
        }
    }


    public static java.lang.reflect.Type getFieldType(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getField(fieldName);

            return field.getGenericType();
        } catch (Exception ex) {
            return null;
        }
    }



	/**
	 *
	 * <p>
	 * get StoreCode(Opcodes#xStore)
	 * </p>
	 *
	 *
	 * @param type
	 * @return
	 */
	public static int storeCode(Type type) {

		int sort = type.getSort();
		switch (sort) {
		case Type.ARRAY:
			sort = ASTORE;
			break;
		case Type.BOOLEAN:
			sort = ISTORE;
			break;
		case Type.BYTE:
			sort = ISTORE;
			break;
		case Type.CHAR:
			sort = ISTORE;
			break;
		case Type.DOUBLE:
			sort = DSTORE;
			break;
		case Type.FLOAT:
			sort = FSTORE;
			break;
		case Type.INT:
			sort = ISTORE;
			break;
		case Type.LONG:
			sort = LSTORE;
			break;
		case Type.OBJECT:
			sort = ASTORE;
			break;
		case Type.SHORT:
			sort = ISTORE;
			break;
		default:
			break;
		}
		return sort;
	}


	/**
	 *
	 * <p>
	 * get StoreCode(Opcodes#xLOAD)
	 * </p>
	 *
	 * @param type
	 * @return
	 */
	public static int loadCode(Type type) {
		int sort = type.getSort();
		switch (sort) {
		case Type.ARRAY:
			sort = ALOAD;
			break;
		case Type.BOOLEAN:
			sort = ILOAD;
			break;
		case Type.BYTE:
			sort = ILOAD;
			break;
		case Type.CHAR:
			sort = ILOAD;
			break;
		case Type.DOUBLE:
			sort = DLOAD;
			break;
		case Type.FLOAT:
			sort = FLOAD;
			break;
		case Type.INT:
			sort = ILOAD;
			break;
		case Type.LONG:
			sort = LLOAD;
			break;
		case Type.OBJECT:
			sort = ALOAD;
			break;
		case Type.SHORT:
			sort = ILOAD;
			break;
		default:
			break;
		}
		return sort;
	}


	/**
	 *
	 * <p>
	 * get StoreCode(Opcodes#xRETURN)
	 * </p>
	 *
	 * @param type
	 * @return
	 */
	public static int rtCode(Type type) {
		int sort = type.getSort();
		switch (sort) {
		case Type.ARRAY:
			sort = ARETURN;
			break;
		case Type.BOOLEAN:
			sort = IRETURN;
			break;
		case Type.BYTE:
			sort = IRETURN;
			break;
		case Type.CHAR:
			sort = IRETURN;
			break;
		case Type.DOUBLE:
			sort = DRETURN;
			break;
		case Type.FLOAT:
			sort = FRETURN;
			break;
		case Type.INT:
			sort = IRETURN;
			break;
		case Type.LONG:
			sort = LRETURN;
			break;
		case Type.OBJECT:
			sort = ARETURN;
			break;
		case Type.SHORT:
			sort = IRETURN;
			break;
		default:
			break;
		}
		return sort;
	}


	/**
	 * 封装成包装类型
	 */
	public static void withBoxingType(MethodVisitor mWriter, Type fieldType) {
		switch (fieldType.getSort()) {
			case Type.VOID:
				break;
			case Type.BOOLEAN:
				mWriter.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
				break;
			case Type.BYTE:
				mWriter.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;");
				break;
			case Type.CHAR:
				mWriter.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;");
				break;
			case Type.SHORT:
				mWriter.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;");
				break;
			case Type.INT:
				mWriter.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
				break;
			case Type.FLOAT:
				mWriter.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;");
				break;
			case Type.LONG:
				mWriter.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;");
				break;
			case Type.DOUBLE:
				mWriter.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
				break;
		}
	}


	/**
	 * 转换成拆箱类型
	 * @param mWriter
	 * @param fieldType
	 */
	public static void withUnBoxingType(MethodVisitor mWriter, Type fieldType) {
		if(fieldType == Type.VOID_TYPE) {
			;
		} else if(fieldType == Type.INT_TYPE) {
			mWriter.visitTypeInsn(CHECKCAST, "java/lang/Integer");
			mWriter.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I");
		}
	}

	/**
	 *
	 * <p>
	 * 比较参数类型是否一致
	 * </p>
	 *
	 * @param types
	 *            asm的类型({@link Type})
	 * @param clazzes
	 *            java 类型({@link Class})
	 * @return
	 */
	private static boolean sameType(Type[] types, Class<?>[] clazzes) {
		// 个数不同
		if (types.length != clazzes.length) {
			return false;
		}

		for (int i = 0; i < types.length; i++) {
			if (!Type.getType(clazzes[i]).equals(types[i])) {
				return false;
			}
		}

		return true;
	}


	/**
	 *
	 * <p>
	 * 获取方法的参数名
	 * </p>
	 * 兼容JDK1.6
	 * @param m
	 * @return
	 */
	public static Map<String, Class<?>> getMethodParams(final Method m) {
		//获取参数类型列表
		final Class<?>[] parameterTypes = m.getParameterTypes();

		final Map<String, Class<?>> paramMap = new LinkedHashMap<String, Class<?>>(parameterTypes.length);

		final String n = m.getDeclaringClass().getName();
		ClassReader cr = null;

		try {
			cr = new ClassReader(n);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		cr.accept(new ClassVisitor(Opcodes.ASM4) {

			@Override
			public MethodVisitor visitMethod(final int access,
					final String name, final String desc,
					final String signature, final String[] exceptions) {
				final Type[] args = Type.getArgumentTypes(desc);
				// 方法名相同并且参数个数相同
				if (!name.equals(m.getName())
						|| !sameType(args, m.getParameterTypes())) {
					return super.visitMethod(access, name, desc, signature,
							exceptions);
				}
				MethodVisitor v = super.visitMethod(access, name, desc,
						signature, exceptions);

				return new MethodVisitor(Opcodes.ASM4, v) {
					@Override
					public void visitLocalVariable(String name, String desc,
							String signature, Label start, Label end, int index) {
						int i = index - 1;
						// 如果是静态方法，则第一就是参数
						// 如果不是静态方法，则第一个是"this"，然后才是方法的参数
						if (Modifier.isStatic(m.getModifiers())) {
							i = index;
						}
						if (i >= 0 && i < parameterTypes.length) {
							paramMap.put(name, parameterTypes[i]);
						}
						super.visitLocalVariable(name, desc, signature, start,
								end, index);
					}

				};

			}
		}, 0);

		return paramMap;
	}



	/**
	 *
	 * <p>
	 * 把java字节码写入class文件
	 * </p>
	 *
	 * @param <T>
	 * @param name
	 * @param data
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static <T> void writeClazz(String name, byte[] data) {
		try {
			File file = new File("C:\\" + name + ".class");
			FileOutputStream fout = new FileOutputStream(file);

			fout.write(data);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
