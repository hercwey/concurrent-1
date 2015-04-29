package dbcache.index;

import java.io.Serializable;
import java.util.concurrent.ConcurrentMap;

import utils.collections.concurrent.ConcurrentLinkedHashMap8;

/**
 * 索引缓存对象
 * @author Jake
 * @date 2014年9月21日下午5:56:10
 */
public class IndexObject<PK extends Comparable<PK> & Serializable> {


	/**
	 * 索引键
	 */
	private IndexKey indexKey;

	/**
	 * 索引区域缓存
	 */
	private ConcurrentMap<PK, Boolean> indexValues = new ConcurrentLinkedHashMap8<PK, Boolean>();


	/**
	 * 获取实例
	 * @param indexKey 索引键
	 * @return
	 */
	public static <PK extends Comparable<PK> & Serializable> IndexObject<PK> valueOf(IndexKey indexKey) {
		IndexObject<PK> indexObject = new IndexObject<PK>();
		indexObject.indexKey = indexKey;
		return indexObject;
	}


	public IndexKey getIndexKey() {
		return indexKey;
	}

	public void setIndexKey(IndexKey indexKey) {
		this.indexKey = indexKey;
	}

	public ConcurrentMap<PK, Boolean> getIndexValues() {
		return indexValues;
	}

	public void setIndexValues(ConcurrentMap<PK, Boolean> indexValues) {
		this.indexValues = indexValues;
	}

}
