/*******************************************************************************
 * Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.runtime.error.cassandra.dao;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

import org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorById;
import org.ebayopensource.turmeric.utils.cassandra.dao.AbstractColumnFamilyDao;

/**
 * The Class ErrorDAO.
 */
public class ErrorByIdDAO extends
                AbstractColumnFamilyDao<Long, org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorById> {

    /**
     * Instantiates a new error dao.
     * 
     * @param clusterName
     *            the cluster name
     * @param host
     *            the host
     * @param s_keyspace
     *            the s_keyspace
     * @param keyTypeClass
     *            the key type class
     * @param persistentClass
     *            the persistent class
     * @param columnFamilyName
     *            the column family name
     */
    public ErrorByIdDAO(String clusterName, String host, String s_keyspace, Class<Long> keyTypeClass,
                    Class<org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorById> persistentClass,
                    String columnFamilyName) {
        super(clusterName, host, s_keyspace, keyTypeClass, persistentClass, columnFamilyName);
    }

    /**
     * Save.
     * 
     * @param key
     *            the key
     * @param model
     *            the model
     * @param timestamp
     *            the timestamp
     * @param errorValueKey
     *            the error value key
     */
    public void save(Long key, ErrorById model, long timestamp, String errorValueKey) {
        super.save(key, model);
        Mutator<Long> mutator = HFactory.createMutator(keySpace, LongSerializer.get());
        HColumn<String, String> timestampColumn = HFactory.createColumn(timestamp + "", errorValueKey,
                        StringSerializer.get(), StringSerializer.get());

        mutator.addInsertion(key, columnFamilyName, timestampColumn);
        mutator.execute();
    }

}
