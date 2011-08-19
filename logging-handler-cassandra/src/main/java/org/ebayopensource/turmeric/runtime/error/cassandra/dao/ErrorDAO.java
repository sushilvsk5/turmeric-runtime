package org.ebayopensource.turmeric.runtime.error.cassandra.dao;

import org.apache.cassandra.db.marshal.LongType;
import org.ebayopensource.turmeric.utils.cassandra.dao.AbstractColumnFamilyDao;

public class ErrorDAO extends AbstractColumnFamilyDao<Long, org.ebayopensource.turmeric.runtime.error.cassandra.model.Error>{

    public ErrorDAO(String clusterName, String host, String s_keyspace, Class<Long> keyTypeClass, Class<org.ebayopensource.turmeric.runtime.error.cassandra.model.Error> persistentClass,
                    String columnFamilyName) {
        super(clusterName, host, s_keyspace, keyTypeClass, persistentClass, columnFamilyName);
    }

}
