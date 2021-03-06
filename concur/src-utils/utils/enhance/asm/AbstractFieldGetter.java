package utils.enhance.asm;

/**
 * 属性获取器
 * @author Jake
 * @date 2014年11月2日下午2:11:00
 * @param <T> 实体类型
 */
public abstract class AbstractFieldGetter<T> implements ValueGetter<T>, Cloneable {

	// asm重写
	@Override
	public abstract Object get(T target);


	@Override
	public abstract String getName();


	@Override
	@SuppressWarnings("unchecked")
	public ValueGetter<T> doClone() {
		try {
			return (ValueGetter<T>) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

}
