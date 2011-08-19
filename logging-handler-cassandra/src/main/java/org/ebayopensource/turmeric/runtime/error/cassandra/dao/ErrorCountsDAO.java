package org.ebayopensource.turmeric.runtime.error.cassandra.dao;

import me.prettyprint.cassandra.serializers.IntegerSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.SerializerTypeInferer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

import org.ebayopensource.turmeric.utils.cassandra.HectorHelper;
import org.ebayopensource.turmeric.utils.cassandra.HectorManager;

public class ErrorCountsDAO {
    private String clusterName; 
    private String host; 
    private Keyspace keySpace;
    
    public ErrorCountsDAO(String clusterName, String host, String keyspaceName){
        this.clusterName = clusterName;
        this.host = host;
        this.keySpace = HectorManager.getKeyspace(clusterName, host, keyspaceName);
    }
    
    public void saveCountByCategory(String serverName, org.ebayopensource.turmeric.runtime.error.cassandra.model.Error error, long timeStamp, int errorCountToStore) {
//        String key = serverName+"-"+error.get;
//        
//        HectorManager.
//        Mutator<String> mutator = HFactory.createMutator(keySpace,
//                StringSerializer.get());
//        
//        HColumn<Long, Integer> column = HFactory.createColumn(timeStamp, errorCountToStore, LongSerializer.get(),
//                        IntegerSerializer.get());
//
//                      columns.add(column);
//                      
//        for (HColumn<?, ?> column : HectorHelper.getColumns(model)) {
//            mutator.addInsertion(key, "ErrorCountsByCategory", column);
//        }
//
//        mutator.execute();
    }

}
