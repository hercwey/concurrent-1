package dbcache.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 动态更新字段注解
 * <br/>用于大字段动态update,无修改字段值则不更新入库.
 * @see ChangeFields
 * @date 2015年1月12日下午11:52:50
 * @author Jake
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicUpdate {
	
}
