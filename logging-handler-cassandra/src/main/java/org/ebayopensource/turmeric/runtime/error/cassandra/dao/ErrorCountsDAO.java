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
import org.ebayopensource.turmeric.utils.cassandra.HectorHelper;
import org.ebayopensource.turmeric.utils.cassandra.HectorManager;

public class ErrorCountsDAO {
    private String clusterName;
    private String host;
    private Keyspace keySpace;

    public ErrorCountsDAO(String clusterName, String host, String keyspaceName) {
        this.clusterName = clusterName;
        this.host = host;
        this.keySpace = HectorManager.getKeyspace(clusterName, host, keyspaceName);
    }

    public void saveCountByCategory(org.ebayopensource.turmeric.runtime.error.cassandra.model.Error errorToSave,
                    ErrorValue errorValue, String errorValueKey, Long timeStamp, int errorCountToStore) {
        String categoryKey = createCategoryKeyByErrorValue(errorValue, errorToSave);
        String severityKey = createSeverityKeyByErrorValue(errorValue, errorToSave);

        Mutator<String> mutator = HFactory.createMutator(keySpace, StringSerializer.get());

        HColumn<Long, String> categoryColumn = HFactory.createColumn(timeStamp, errorValueKey, LongSerializer.get(),
                        StringSerializer.get());
        
        HColumn<Long, String> severityColumn = HFactory.createColumn(timeStamp, errorValueKey, LongSerializer.get(),
                        StringSerializer.get());
        mutator.addInsertion(categoryKey, "ErrorCountsByCategory", categoryColumn);
        mutator.addInsertion(severityKey, "ErrorCountsBySeverity", severityColumn);
        mutator.execute();
    }

    private String createSeverityKeyByErrorValue(ErrorValue errorValue, Error error) {
        return createSuffixedErrorCountKey(errorValue, error.getSeverity());
    }

    private String createSuffixedErrorCountKey(ErrorValue errorValue, String suffix) {
        String key = errorValue.getServerName() + "-" + errorValue.getServiceAdminName() + "-"
                        + errorValue.getOperationName() + "-" + suffix;
        return key;
    }

    private String createCategoryKeyByErrorValue(ErrorValue errorValue, Error error) {
        return createSuffixedErrorCountKey(errorValue, error.getCategory());
    }

}
