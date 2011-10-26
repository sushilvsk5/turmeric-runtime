package org.ebayopensource.turmeric.runtime.error.cassandra.dao;

import static org.ebayopensource.turmeric.runtime.error.cassandra.handler.CassandraErrorLoggingHandler.KEY_SEPARATOR;

import java.util.ArrayList;
import java.util.Date;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.ThriftCfDef;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.ddl.ColumnDefinition;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ColumnType;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

import org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorById;
import org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorValue;
import org.ebayopensource.turmeric.utils.cassandra.hector.HectorManager;

/**
 * The Class ErrorCountsDAO.
 */
public class ErrorCountsDAO {

   /** The key space. */
   private final Keyspace keySpace;

   /**
    * Instantiates a new error counts dao.
    * 
    * @param clusterName
    *           the cluster name
    * @param host
    *           the host
    * @param keyspaceName
    *           the keyspace name
    */
   public ErrorCountsDAO(String clusterName, String host, String keyspaceName) {
      try {
         Cluster cluster = HFactory.getOrCreateCluster(clusterName, host);
         this.createCF(keyspaceName, "ErrorCountsByCategory", cluster, false, ComparatorType.LONGTYPE,
                  ComparatorType.UTF8TYPE, ComparatorType.LONGTYPE, ComparatorType.LONGTYPE);
         this.createCF(keyspaceName, "ErrorCountsBySeverity", cluster, false, ComparatorType.LONGTYPE,
                  ComparatorType.UTF8TYPE, ComparatorType.LONGTYPE, ComparatorType.LONGTYPE);

      } catch (Exception e) {

      }

      this.keySpace = new HectorManager().getKeyspace(clusterName, host, keyspaceName, "ErrorCountsByCategory", false,
               null, String.class);

   }

   /**
    * Creates the cf.
    * 
    * @param kspace
    *           the kspace
    * @param columnFamilyName
    *           the column family name
    * @param cluster
    *           the cluster
    * @param isSuperColumn
    *           the is super column
    * @param superKeyValidator
    *           the super key validator
    * @param keyValidator
    *           the key validator
    * @param superComparator
    *           the super comparator
    * @param comparator
    *           the comparator
    */
   private void createCF(final String kspace, final String columnFamilyName, final Cluster cluster,
            boolean isSuperColumn, final ComparatorType superKeyValidator, final ComparatorType keyValidator,
            final ComparatorType superComparator, final ComparatorType comparator) {

      if (isSuperColumn) {
         ThriftCfDef cfDefinition = (ThriftCfDef) HFactory.createColumnFamilyDefinition(kspace, columnFamilyName,
                  superComparator, new ArrayList<ColumnDefinition>());
         cfDefinition.setColumnType(ColumnType.SUPER);
         cfDefinition.setKeyValidationClass(superKeyValidator.getClassName());
         cfDefinition.setSubComparatorType(comparator);
         cluster.addColumnFamily(cfDefinition);
      } else {
         ColumnFamilyDefinition cfDefinition = new ThriftCfDef(kspace, columnFamilyName);
         cfDefinition.setKeyValidationClass(keyValidator.getClassName());
         if ("MetricValuesByIpAndDate".equals(columnFamilyName) || "MetricTimeSeries".equals(columnFamilyName)
                  || "ServiceCallsByTime".equals(columnFamilyName) || "ErrorCountsByCategory".equals(columnFamilyName)
                  || "ErrorCountsBySeverity".equals(columnFamilyName)) {

            ComparatorType comparator1 = this.getComparator(Long.class);
            cfDefinition.setComparatorType(comparator1);
         } else {
            cfDefinition.setComparatorType(comparator);
         }

         cluster.addColumnFamily(cfDefinition);
      }
   }

   /**
    * Gets the comparator.
    * 
    * @param keyTypeClass
    *           the key type class
    * @return the comparator
    */
   private ComparatorType getComparator(Class<?> keyTypeClass) {
      if ((keyTypeClass != null) && String.class.isAssignableFrom(keyTypeClass)) {
         return ComparatorType.UTF8TYPE;
      } else if ((keyTypeClass != null) && Integer.class.isAssignableFrom(keyTypeClass)) {
         return ComparatorType.INTEGERTYPE;
      } else if ((keyTypeClass != null) && Long.class.isAssignableFrom(keyTypeClass)) {
         return ComparatorType.LONGTYPE;
      } else if ((keyTypeClass != null) && Date.class.isAssignableFrom(keyTypeClass)) {
         return ComparatorType.TIMEUUIDTYPE;
      } else {
         return ComparatorType.BYTESTYPE; // by default
      }

   }

   /**
    * Creates the category key by error value.
    * 
    * @param errorValue
    *           the error value
    * @param error
    *           the error
    * @return the string
    */
   public String createCategoryKeyByErrorValue(ErrorValue errorValue, ErrorById error) {
      return this.createSuffixedErrorCountKey(errorValue, error.getCategory());
   }

   /**
    * Creates the category key by error value for all ops.
    * 
    * @param errorValue
    *           the error value
    * @param error
    *           the error
    * @return the string
    */
   public String createCategoryKeyByErrorValueForAllOps(ErrorValue errorValue, ErrorById error) {
      return this.createSuffixedErrorCountKeyAllOps(errorValue, error.getCategory());
   }

   /**
    * Creates the severity key by error value.
    * 
    * @param errorValue
    *           the error value
    * @param error
    *           the error
    * @return the string
    */
   public String createSeverityKeyByErrorValue(ErrorValue errorValue, ErrorById error) {
      return this.createSuffixedErrorCountKey(errorValue, error.getSeverity());
   }

   /**
    * Creates the severity key by error value for all ops.
    * 
    * @param errorValue
    *           the error value
    * @param error
    *           the error
    * @return the string
    */
   public String createSeverityKeyByErrorValueForAllOps(ErrorValue errorValue, ErrorById error) {
      return this.createSuffixedErrorCountKeyAllOps(errorValue, error.getSeverity());
   }

   /**
    * Creates the suffixed error count key.
    * 
    * @param errorValue
    *           the error value
    * @param suffix
    *           the suffix
    * @return the string
    */
   private String createSuffixedErrorCountKey(ErrorValue errorValue, String suffix) {
      String key = errorValue.getServerName() + KEY_SEPARATOR + errorValue.getServiceAdminName() + KEY_SEPARATOR
               + errorValue.getConsumerName() + KEY_SEPARATOR + errorValue.getOperationName() + KEY_SEPARATOR + suffix
               + KEY_SEPARATOR + errorValue.isServerSide();
      return key;
   }

   /**
    * Creates the suffixed error count key all ops.
    * 
    * @param errorValue
    *           the error value
    * @param suffix
    *           the suffix
    * @return the string
    */
   public String createSuffixedErrorCountKeyAllOps(ErrorValue errorValue, String suffix) {
      String key = errorValue.getServerName() + KEY_SEPARATOR + errorValue.getServiceAdminName() + KEY_SEPARATOR
               + errorValue.getConsumerName() + KEY_SEPARATOR + "All" + KEY_SEPARATOR + suffix + KEY_SEPARATOR
               + errorValue.isServerSide();
      return key;
   }

   /**
    * Save error counts.
    * 
    * @param errorToSave
    *           the error to save
    * @param errorValue
    *           the error value
    * @param errorValueKey
    *           the error value key
    * @param timeStamp
    *           the time stamp
    * @param errorCountToStore
    *           the error count to store
    */
   public void saveErrorCounts(org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorById errorToSave,
            ErrorValue errorValue, String errorValueKey, Long timeStamp, int errorCountToStore) {
      Mutator<String> mutator = createStringMutator();
      saveErrorCountsByCategory(errorToSave, errorValue, mutator, errorValueKey, timeStamp);
      saveErrorCountsBySeverity(errorToSave, errorValue, mutator, errorValueKey, timeStamp);
   }

   /**
    * Save error counts by severity.
    * 
    * @param errorToSave
    *           the error to save
    * @param errorValue
    *           the error value
    * @param mutator
    *           the mutator
    * @param errorValueKey
    *           the error value key
    * @param timeStamp
    *           the time stamp
    */
   private void saveErrorCountsBySeverity(
            org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorById errorToSave, ErrorValue errorValue,
            Mutator<String> mutator, String errorValueKey, Long timeStamp) {
      HColumn<Long, String> severityColumn = HFactory.createColumn(timeStamp, errorValueKey, LongSerializer.get(),
               StringSerializer.get());

      String severityKey = this.createSeverityKeyByErrorValue(errorValue, errorToSave);
      String severityKeyAllOps = this.createSeverityKeyByErrorValueForAllOps(errorValue, errorToSave);
      String severityKeyAllOpsAllConsumers = this.createSeverityKeyByErrorValueForAllOpsAllConsumers(errorValue,
               errorToSave);
      String severityKeyAllServers = this.createSeverityKeyByErrorValueForAllServer(errorValue, errorToSave);
      String severityKeyAllConsumers = createSeverityKeyByErrorValueForAllConsumers(errorValue, errorToSave);
      String severityKeyAllServersAllOperations = createSeverityKeyByErrorValueForAllServersAllOperations(errorValue,
               errorToSave);
      String severityKeyAllServersAllConsumers = createSeverityKeyByErrorValueForAllServersAllConsumers(errorValue,
               errorToSave);

      String severityKeyAllServersSomeConsumerSomeOperation = createSeverityKeyByErrorValueForAllServersSomeConsumerSomeOperation(
               errorValue, errorToSave);

      mutator.insert(severityKey, "ErrorCountsBySeverity", severityColumn);
      mutator.insert(severityKeyAllOps, "ErrorCountsBySeverity", severityColumn);
      mutator.insert(severityKeyAllOpsAllConsumers, "ErrorCountsBySeverity", severityColumn);
      mutator.insert(severityKeyAllServers, "ErrorCountsBySeverity", severityColumn);
      mutator.insert(severityKeyAllConsumers, "ErrorCountsBySeverity", severityColumn);
      mutator.insert(severityKeyAllServersAllOperations, "ErrorCountsBySeverity", severityColumn);
      mutator.insert(severityKeyAllServersAllConsumers, "ErrorCountsBySeverity", severityColumn);
      mutator.insert(severityKeyAllServersSomeConsumerSomeOperation, "ErrorCountsBySeverity", severityColumn);
   }

   /**
    * Creates the severity key by error value for all servers some consumer some operation.
    * 
    * @param errorValue
    *           the error value
    * @param errorToSave
    *           the error to save
    * @return the string
    */
   private String createSeverityKeyByErrorValueForAllServersSomeConsumerSomeOperation(ErrorValue errorValue,
            ErrorById errorToSave) {
      return concatValues("All", errorValue.getServiceAdminName(), errorValue.getConsumerName(),
               errorValue.getOperationName(), errorToSave.getSeverity(), String.valueOf(errorValue.isServerSide()));
   }

   /**
    * Creates the severity key by error value for all servers all consumers.
    * 
    * @param errorValue
    *           the error value
    * @param errorToSave
    *           the error to save
    * @return the string
    */
   private String createSeverityKeyByErrorValueForAllServersAllConsumers(ErrorValue errorValue, ErrorById errorToSave) {
      return concatValues("All", errorValue.getServiceAdminName(), "All", errorValue.getOperationName(),
               errorToSave.getSeverity(), String.valueOf(errorValue.isServerSide()));
   }

   /**
    * Creates the severity key by error value for all servers all operations.
    * 
    * @param errorValue
    *           the error value
    * @param errorToSave
    *           the error to save
    * @return the string
    */
   private String createSeverityKeyByErrorValueForAllServersAllOperations(ErrorValue errorValue, ErrorById errorToSave) {
      return concatValues("All", errorValue.getServiceAdminName(), errorValue.getConsumerName(), "All",
               errorToSave.getSeverity(), String.valueOf(errorValue.isServerSide()));
   }

   /**
    * Creates the string mutator.
    * 
    * @return the mutator
    */
   private Mutator<String> createStringMutator() {
      Mutator<String> mutator = HFactory.createMutator(keySpace, StringSerializer.get());
      return mutator;
   }

   /**
    * Save error counts by category.
    * 
    * @param errorToSave
    *           the error to save
    * @param errorValue
    *           the error value
    * @param mutator
    *           the mutator
    * @param errorValueKey
    *           the error value key
    * @param timeStamp
    *           the time stamp
    */
   private void saveErrorCountsByCategory(
            org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorById errorToSave, ErrorValue errorValue,
            Mutator<String> mutator, String errorValueKey, Long timeStamp) {
      String detailedKey = this.createCategoryKeyByErrorValue(errorValue, errorToSave);
      String keyAllOps = this.createCategoryKeyByErrorValueForAllOps(errorValue, errorToSave);
      String keyAllOpsAllConsumers = this.createCategoryKeyByErrorValueForAllOpsAllConsumers(errorValue, errorToSave);
      String categoryKeyAllConsumers = this.createCategoryKeyByErrorValueForAllConsumers(errorValue, errorToSave);
      String categoryKeyAllServers = this.createCategoryKeyByErrorValueForAllServer(errorValue, errorToSave);
      String categoryKeyAllServersWithAllConsumersAndSomeOperation = this
               .createCategoryKeyByErrorValueForcategoryKeyAllServersWithAllConsumersAndSomeOperation(errorValue,
                        errorToSave);
      String categoryKeyAllServersWithSomeConsumersAndSomeOperation = this
               .createCategoryKeyByErrorValueForcategoryKeyAllServersWithSomeConsumerAndSomeOperation(errorValue,
                        errorToSave);
      String categoryKeyAllServersSomeConsumersAllOperation = this
               .createCategoryKeyByErrorValueForcategoryKeyAllServersSomeConsumersAllOperation(errorValue, errorToSave);

      HColumn<Long, String> categoryColumn = HFactory.createColumn(timeStamp, errorValueKey, LongSerializer.get(),
               StringSerializer.get());
      HColumn<Long, String> categoryColumnForAllOps = HFactory.createColumn(timeStamp, errorValueKey,
               LongSerializer.get(), StringSerializer.get());

      mutator.insert(detailedKey, "ErrorCountsByCategory", categoryColumn);
      mutator.insert(keyAllOps, "ErrorCountsByCategory", categoryColumnForAllOps);
      mutator.insert(keyAllOpsAllConsumers, "ErrorCountsByCategory", categoryColumnForAllOps);
      mutator.insert(categoryKeyAllServers, "ErrorCountsByCategory", categoryColumnForAllOps);
      mutator.insert(categoryKeyAllConsumers, "ErrorCountsByCategory", categoryColumnForAllOps);
      mutator.insert(categoryKeyAllServersWithAllConsumersAndSomeOperation, "ErrorCountsByCategory",
               categoryColumnForAllOps);
      mutator.insert(categoryKeyAllServersWithSomeConsumersAndSomeOperation, "ErrorCountsByCategory",
               categoryColumnForAllOps);
      mutator.insert(categoryKeyAllServersSomeConsumersAllOperation, "ErrorCountsByCategory", categoryColumnForAllOps);
   }

   /**
    * Creates the category key by error value forcategory key all servers some consumers all operation.
    * 
    * @param errorValue
    *           the error value
    * @param errorToSave
    *           the error to save
    * @return the string
    */
   private String createCategoryKeyByErrorValueForcategoryKeyAllServersSomeConsumersAllOperation(ErrorValue errorValue,
            ErrorById errorToSave) {
      return concatValues("All", errorValue.getServiceAdminName(), errorValue.getConsumerName(), "All",
               errorToSave.getCategory(), String.valueOf(errorValue.isServerSide()));
   }

   /**
    * Creates the category key by error value forcategory key all servers with some consumer and some operation.
    * 
    * @param errorValue
    *           the error value
    * @param errorToSave
    *           the error to save
    * @return the string
    */
   private String createCategoryKeyByErrorValueForcategoryKeyAllServersWithSomeConsumerAndSomeOperation(
            ErrorValue errorValue, ErrorById errorToSave) {
      return concatValues("All", errorValue.getServiceAdminName(), errorValue.getConsumerName(),
               errorValue.getOperationName(), errorToSave.getCategory(), String.valueOf(errorValue.isServerSide()));
   }

   /**
    * Creates the category key by error value forcategory key all servers with all consumers and some operation.
    * 
    * @param errorValue
    *           the error value
    * @param errorToSave
    *           the error to save
    * @return the string
    */
   private String createCategoryKeyByErrorValueForcategoryKeyAllServersWithAllConsumersAndSomeOperation(
            ErrorValue errorValue, ErrorById errorToSave) {
      return concatValues("All", errorValue.getServiceAdminName(), "All", errorValue.getOperationName(),
               errorToSave.getCategory(), String.valueOf(errorValue.isServerSide()));
   }

   /**
    * Creates the severity key by error value for all consumers.
    * 
    * @param errorValue
    *           the error value
    * @param errorToSave
    *           the error to save
    * @return the string
    */
   private String createSeverityKeyByErrorValueForAllConsumers(ErrorValue errorValue, ErrorById errorToSave) {
      return concatValues(errorValue.getServerName(), errorValue.getServiceAdminName(), "All",
               errorValue.getOperationName(), errorToSave.getSeverity(), String.valueOf(errorValue.isServerSide()));
   }

   /**
    * Creates the category key by error value for all consumers.
    * 
    * @param errorValue
    *           the error value
    * @param errorToSave
    *           the error to save
    * @return the string
    */
   private String createCategoryKeyByErrorValueForAllConsumers(ErrorValue errorValue, ErrorById errorToSave) {
      return concatValues(errorValue.getServerName(), errorValue.getServiceAdminName(), "All",
               errorValue.getOperationName(), errorToSave.getCategory(), String.valueOf(errorValue.isServerSide()));
   }

   /**
    * Creates the category key by error value for all server.
    * 
    * @param errorValue
    *           the error value
    * @param errorToSave
    *           the error to save
    * @return the string
    */
   private String createCategoryKeyByErrorValueForAllServer(ErrorValue errorValue, ErrorById errorToSave) {
      return this.createSuffixedErrorCountKeyAllServers(errorValue, errorToSave.getCategory());
   }

   /**
    * Creates the severity key by error value for all server.
    * 
    * @param errorValue
    *           the error value
    * @param errorToSave
    *           the error to save
    * @return the string
    */
   private String createSeverityKeyByErrorValueForAllServer(ErrorValue errorValue, ErrorById errorToSave) {
      return this.createSuffixedErrorCountKeyAllServers(errorValue, errorToSave.getSeverity());
   }

   /**
    * Creates the suffixed error count key all servers.
    * 
    * @param errorValue
    *           the error value
    * @param suffix
    *           the suffix
    * @return the string
    */
   private String createSuffixedErrorCountKeyAllServers(ErrorValue errorValue, String suffix) {
      String key = "All" + KEY_SEPARATOR + errorValue.getServiceAdminName() + KEY_SEPARATOR + "All" + KEY_SEPARATOR
               + "All" + KEY_SEPARATOR + suffix + KEY_SEPARATOR + errorValue.isServerSide();
      return key;
   }

   /**
    * Creates the category key by error value for all ops all consumers.
    * 
    * @param errorValue
    *           the error value
    * @param errorToSave
    *           the error to save
    * @return the string
    */
   private String createCategoryKeyByErrorValueForAllOpsAllConsumers(ErrorValue errorValue, ErrorById errorToSave) {
      return this.createSuffixedErrorCountKeyAllOpsAllConsumers(errorValue, errorToSave.getCategory());
   }

   /**
    * Creates the severity key by error value for all ops all consumers.
    * 
    * @param errorValue
    *           the error value
    * @param errorToSave
    *           the error to save
    * @return the string
    */
   private String createSeverityKeyByErrorValueForAllOpsAllConsumers(ErrorValue errorValue, ErrorById errorToSave) {
      return this.createSuffixedErrorCountKeyAllOpsAllConsumers(errorValue, errorToSave.getSeverity());
   }

   /**
    * Creates the suffixed error count key all ops all consumers.
    * 
    * @param errorValue
    *           the error value
    * @param suffix
    *           the suffix
    * @return the string
    */
   private String createSuffixedErrorCountKeyAllOpsAllConsumers(ErrorValue errorValue, String suffix) {
      String key = errorValue.getServerName() + KEY_SEPARATOR + errorValue.getServiceAdminName() + KEY_SEPARATOR
               + "All" + KEY_SEPARATOR + "All" + KEY_SEPARATOR + suffix + KEY_SEPARATOR + errorValue.isServerSide();
      return key;
   }

   /**
    * Concat values.
    * 
    * @param values
    *           the values
    * @return the string
    */
   private String concatValues(String... values) {
      StringBuilder valueBuilder = new StringBuilder();
      for (int i = 0; i < values.length - 1; i++) {
         valueBuilder.append(values[i]).append(KEY_SEPARATOR);
      }
      if (values != null && values.length > 0) {
         valueBuilder.append(values[values.length - 1]);
      }
      return valueBuilder.toString();

   }

}
