package dbcache.service;

import java.util.concurrent.ExecutorService;

import dbcache.model.CacheObject;
import dbcache.model.PersistAction;

/**
 * 实体入库服务接口
 * @author Jake
 * @date 2014年8月13日上午12:24:45
 */
public interface DbPersistService {

	/**
	 * 处理创建
	 * @param cacheObject 实体缓存对象
	 * @param dbAccessService 数据库存取服务
	 */
	public void handleSave(CacheObject<?> cacheObject, DbAccessService dbAccessService);

	/**
	 * 处理更新
	 * @param cacheObject 实体缓存对象
	 * @param dbAccessService 数据库存取服务
	 */
	public void handleUpdate(CacheObject<?> cacheObject, DbAccessService dbAccessService);

	/**
	 * 处理删除
	 * @param cacheObject 实体缓存对象
	 * @param dbAccessService 数据库存取服务
	 */
	public void handleDelete(CacheObject<?> cacheObject, DbAccessService dbAccessService);

	/**
	 * 等待处理完毕
	 */
	public void awaitTermination();

	/**
	 * 打印出未入库对象
	 */
	void logHadNotPersistEntity();

	/**
	 * 获取入库线程池
	 * @return ExecutorService
	 */
	ExecutorService getThreadPool();

}
