package org.ebayopensource.turmeric.runtime.error.cassandra.dao;

import org.ebayopensource.turmeric.utils.cassandra.dao.AbstractColumnFamilyDao;

public class ErrorValueDAO extends AbstractColumnFamilyDao<String, org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorValue>{

    public ErrorValueDAO(String clusterName, String host, String s_keyspace, Class<String> keyTypeClass, Class<org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorValue> persistentClass,
                    String columnFamilyName) {
        super(clusterName, host, s_keyspace, keyTypeClass, persistentClass, columnFamilyName);
    }

}
