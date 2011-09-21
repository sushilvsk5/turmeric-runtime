package org.ebayopensource.turmeric.runtime.error.cassandra.dao;

import me.prettyprint.cassandra.serializers.IntegerSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.SerializerTypeInferer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

import org.ebayopensource.turmeric.runtime.error.cassandra.model.Error;
import org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorValue;
import org.ebayopensource.turmeric.utils.cassandra.hector.HectorManager;


/**
 * The Class ErrorCountsDAO.
 */
public class ErrorCountsDAO {
    
    /** The cluster name. */
    private String clusterName;
    
    /** The host. */
    private String host;
    
    /** The key space. */
    private Keyspace keySpace;

    /**
     * Instantiates a new error counts dao.
     *
     * @param clusterName the cluster name
     * @param host the host
     * @param keyspaceName the keyspace name
     */
    public ErrorCountsDAO(String clusterName, String host, String keyspaceName) {
        this.clusterName = clusterName;
        this.host = host;
        this.keySpace = new HectorManager().getKeyspace(clusterName, host, keyspaceName, "ErrorCountsByCategory", false);
    }

    /**
     * Save error counts.
     *
     * @param errorToSave the error to save
     * @param errorValue the error value
     * @param errorValueKey the error value key
     * @param timeStamp the time stamp
     * @param errorCountToStore the error count to store
     */
    public void saveErrorCounts(org.ebayopensource.turmeric.runtime.error.cassandra.model.Error errorToSave,
                    ErrorValue errorValue, String errorValueKey, Long timeStamp, int errorCountToStore) {
        String categoryKey = createCategoryKeyByErrorValue(errorValue, errorToSave);
        String categoryKeyAllOps = createCategoryKeyByErrorValueForAllOps(errorValue, errorToSave);
        String severityKey = createSeverityKeyByErrorValue(errorValue, errorToSave);
        String severityKeyAllOps = createSeverityKeyByErrorValueForAllOps(errorValue, errorToSave);

        Mutator<String> mutator = HFactory.createMutator(keySpace, StringSerializer.get());
        HColumn<Long, String> categoryColumn = HFactory.createColumn(timeStamp, errorValueKey, LongSerializer.get(),
                        StringSerializer.get());
        HColumn<Long, String> severityColumn = HFactory.createColumn(timeStamp, errorValueKey, LongSerializer.get(),
                        StringSerializer.get());
        
        //all ops columns
        HColumn<Long, String> categoryColumnForAllOps = HFactory.createColumn(timeStamp, errorValueKey, LongSerializer.get(),
                        StringSerializer.get());
              
        
        HColumn<Long, String> severityColumnAllOps = HFactory.createColumn(timeStamp, errorValueKey, LongSerializer.get(),
                        StringSerializer.get());
        
        
        mutator.addInsertion(categoryKey, "ErrorCountsByCategory", categoryColumn);
        mutator.addInsertion(categoryKeyAllOps, "ErrorCountsByCategory", categoryColumnForAllOps);
        mutator.addInsertion(severityKey, "ErrorCountsBySeverity", severityColumn);
        mutator.addInsertion(severityKeyAllOps, "ErrorCountsBySeverity", severityColumnAllOps);
        mutator.execute();
    }

    /**
     * Creates the severity key by error value.
     *
     * @param errorValue the error value
     * @param error the error
     * @return the string
     */
    public String createSeverityKeyByErrorValue(ErrorValue errorValue, Error error) {
        return createSuffixedErrorCountKey(errorValue, error.getSeverity());
    }
    
    /**
     * Creates the severity key by error value for all ops.
     *
     * @param errorValue the error value
     * @param error the error
     * @return the string
     */
    public String createSeverityKeyByErrorValueForAllOps(ErrorValue errorValue, Error error) {
        return createSuffixedErrorCountKeyAllOps(errorValue, error.getSeverity());
    }

    /**
     * Creates the suffixed error count key.
     *
     * @param errorValue the error value
     * @param suffix the suffix
     * @return the string
     */
    public String createSuffixedErrorCountKey(ErrorValue errorValue, String suffix) {
        String key = errorValue.getServerName() + "-" + errorValue.getServiceAdminName() + "-"
                        + errorValue.getOperationName() + "-" + suffix;
        return key;
    }
    
    /**
     * Creates the suffixed error count key all ops.
     *
     * @param errorValue the error value
     * @param suffix the suffix
     * @return the string
     */
    public String createSuffixedErrorCountKeyAllOps(ErrorValue errorValue, String suffix) {
        String key = errorValue.getServerName() + "-" + errorValue.getServiceAdminName() + "-All-" + suffix;
        return key;
    }

    /**
     * Creates the category key by error value.
     *
     * @param errorValue the error value
     * @param error the error
     * @return the string
     */
    public String createCategoryKeyByErrorValue(ErrorValue errorValue, Error error) {
        return createSuffixedErrorCountKey(errorValue, error.getCategory());
    }
    
    /**
     * Creates the category key by error value for all ops.
     *
     * @param errorValue the error value
     * @param error the error
     * @return the string
     */
    public String createCategoryKeyByErrorValueForAllOps(ErrorValue errorValue, Error error) {
        return createSuffixedErrorCountKeyAllOps(errorValue, error.getCategory());
    }

}
