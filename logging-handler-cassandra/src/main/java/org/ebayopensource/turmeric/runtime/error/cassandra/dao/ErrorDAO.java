package org.ebayopensource.turmeric.runtime.error.cassandra.dao;

import org.apache.cassandra.db.marshal.LongType;
import org.ebayopensource.turmeric.utils.cassandra.dao.AbstractColumnFamilyDao;

/**
 * The Class ErrorDAO.
 */
public class ErrorDAO extends AbstractColumnFamilyDao<Long, org.ebayopensource.turmeric.runtime.error.cassandra.model.Error>{

    /**
     * Instantiates a new error dao.
     *
     * @param clusterName the cluster name
     * @param host the host
     * @param s_keyspace the s_keyspace
     * @param keyTypeClass the key type class
     * @param persistentClass the persistent class
     * @param columnFamilyName the column family name
     */
    public ErrorDAO(String clusterName, String host, String s_keyspace, Class<Long> keyTypeClass, Class<org.ebayopensource.turmeric.runtime.error.cassandra.model.Error> persistentClass,
                    String columnFamilyName) {
        super(clusterName, host, s_keyspace, keyTypeClass, persistentClass, columnFamilyName);
    }

}
