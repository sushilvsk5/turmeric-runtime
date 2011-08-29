package org.ebayopensource.turmeric.runtime.error.cassandra.dao;

import org.ebayopensource.turmeric.utils.cassandra.dao.AbstractColumnFamilyDao;

/**
 * The Class ErrorValueDAO.
 */
public class ErrorValueDAO extends AbstractColumnFamilyDao<String, org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorValue>{

    /**
     * Instantiates a new error value dao.
     *
     * @param clusterName the cluster name
     * @param host the host
     * @param s_keyspace the s_keyspace
     * @param keyTypeClass the key type class
     * @param persistentClass the persistent class
     * @param columnFamilyName the column family name
     */
    public ErrorValueDAO(String clusterName, String host, String s_keyspace, Class<String> keyTypeClass, Class<org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorValue> persistentClass,
                    String columnFamilyName) {
        super(clusterName, host, s_keyspace, keyTypeClass, persistentClass, columnFamilyName);
    }

}
