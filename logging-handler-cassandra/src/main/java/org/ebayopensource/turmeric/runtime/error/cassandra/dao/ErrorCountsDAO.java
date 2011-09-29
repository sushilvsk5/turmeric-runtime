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
	 *            the cluster name
	 * @param host
	 *            the host
	 * @param keyspaceName
	 *            the keyspace name
	 */
	public ErrorCountsDAO(String clusterName, String host, String keyspaceName) {
		try {
			Cluster cluster = HFactory.getOrCreateCluster(clusterName,
					host);
			this.createCF(keyspaceName, "ErrorCountsByCategory", cluster,
					false, ComparatorType.LONGTYPE, ComparatorType.UTF8TYPE,
					ComparatorType.LONGTYPE, ComparatorType.LONGTYPE);
			this.createCF(keyspaceName, "ErrorCountsBySeverity", cluster,
					false, ComparatorType.LONGTYPE, ComparatorType.UTF8TYPE,
					ComparatorType.LONGTYPE, ComparatorType.LONGTYPE);

			
		} catch (Exception e) {

		}
		
		this.keySpace = new HectorManager().getKeyspace(clusterName, host,
				keyspaceName, "ErrorCountsByCategory", false, null,
				String.class);

	}

	private void createCF(final String kspace, final String columnFamilyName,
			final Cluster cluster, boolean isSuperColumn,
			final ComparatorType superKeyValidator,
			final ComparatorType keyValidator,
			final ComparatorType superComparator,
			final ComparatorType comparator) {

		if (isSuperColumn) {
			ThriftCfDef cfDefinition = (ThriftCfDef) HFactory
					.createColumnFamilyDefinition(kspace, columnFamilyName,
							superComparator, new ArrayList<ColumnDefinition>());
			cfDefinition.setColumnType(ColumnType.SUPER);
			cfDefinition
					.setKeyValidationClass(superKeyValidator.getClassName());
			cfDefinition.setSubComparatorType(comparator);
			cluster.addColumnFamily(cfDefinition);
		} else {
			ColumnFamilyDefinition cfDefinition = new ThriftCfDef(kspace,
					columnFamilyName);
			cfDefinition.setKeyValidationClass(keyValidator.getClassName());
			if ("MetricValuesByIpAndDate".equals(columnFamilyName)
					|| "MetricTimeSeries".equals(columnFamilyName)
					|| "ServiceCallsByTime".equals(columnFamilyName)
					|| "ErrorCountsByCategory".equals(columnFamilyName)
					|| "ErrorCountsBySeverity".equals(columnFamilyName)) {

				ComparatorType comparator1 = this.getComparator(Long.class);
				cfDefinition.setComparatorType(comparator1);
			} else {
				cfDefinition.setComparatorType(comparator);
			}

			cluster.addColumnFamily(cfDefinition);
		}
	}

	private ComparatorType getComparator(Class<?> keyTypeClass) {
		if ((keyTypeClass != null)
				&& String.class.isAssignableFrom(keyTypeClass)) {
			return ComparatorType.UTF8TYPE;
		} else if ((keyTypeClass != null)
				&& Integer.class.isAssignableFrom(keyTypeClass)) {
			return ComparatorType.INTEGERTYPE;
		} else if ((keyTypeClass != null)
				&& Long.class.isAssignableFrom(keyTypeClass)) {
			return ComparatorType.LONGTYPE;
		} else if ((keyTypeClass != null)
				&& Date.class.isAssignableFrom(keyTypeClass)) {
			return ComparatorType.TIMEUUIDTYPE;
		} else {
			return ComparatorType.BYTESTYPE; // by default
		}

	}

	/**
	 * Creates the category key by error value.
	 * 
	 * @param errorValue
	 *            the error value
	 * @param error
	 *            the error
	 * @return the string
	 */
	public String createCategoryKeyByErrorValue(ErrorValue errorValue,
			ErrorById error) {
		return this
				.createSuffixedErrorCountKey(errorValue, error.getCategory());
	}

	/**
	 * Creates the category key by error value for all ops.
	 * 
	 * @param errorValue
	 *            the error value
	 * @param error
	 *            the error
	 * @return the string
	 */
	public String createCategoryKeyByErrorValueForAllOps(ErrorValue errorValue,
			ErrorById error) {
		return this.createSuffixedErrorCountKeyAllOps(errorValue,
				error.getCategory());
	}

	/**
	 * Creates the severity key by error value.
	 * 
	 * @param errorValue
	 *            the error value
	 * @param error
	 *            the error
	 * @return the string
	 */
	public String createSeverityKeyByErrorValue(ErrorValue errorValue,
			ErrorById error) {
		return this
				.createSuffixedErrorCountKey(errorValue, error.getSeverity());
	}

	/**
	 * Creates the severity key by error value for all ops.
	 * 
	 * @param errorValue
	 *            the error value
	 * @param error
	 *            the error
	 * @return the string
	 */
	public String createSeverityKeyByErrorValueForAllOps(ErrorValue errorValue,
			ErrorById error) {
		return this.createSuffixedErrorCountKeyAllOps(errorValue,
				error.getSeverity());
	}

	/**
	 * Creates the suffixed error count key.
	 * 
	 * @param errorValue
	 *            the error value
	 * @param suffix
	 *            the suffix
	 * @return the string
	 */
	private String createSuffixedErrorCountKey(ErrorValue errorValue,
			String suffix) {
		String key = errorValue.getServerName() + KEY_SEPARATOR
				+ errorValue.getServiceAdminName() + KEY_SEPARATOR
				+ errorValue.getConsumerName() + KEY_SEPARATOR
				+ errorValue.getOperationName() + KEY_SEPARATOR + suffix
				+ KEY_SEPARATOR + errorValue.isServerSide();
		return key;
	}

	/**
	 * Creates the suffixed error count key all ops.
	 * 
	 * @param errorValue
	 *            the error value
	 * @param suffix
	 *            the suffix
	 * @return the string
	 */
	public String createSuffixedErrorCountKeyAllOps(ErrorValue errorValue,
			String suffix) {
		String key = errorValue.getServerName() + KEY_SEPARATOR
				+ errorValue.getServiceAdminName() + KEY_SEPARATOR
				+ errorValue.getConsumerName() + KEY_SEPARATOR + "All"
				+ KEY_SEPARATOR + suffix + KEY_SEPARATOR
				+ errorValue.isServerSide();
		return key;
	}

	/**
	 * Save error counts.
	 * 
	 * @param errorToSave
	 *            the error to save
	 * @param errorValue
	 *            the error value
	 * @param errorValueKey
	 *            the error value key
	 * @param timeStamp
	 *            the time stamp
	 * @param errorCountToStore
	 *            the error count to store
	 */
	public void saveErrorCounts(
			org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorById errorToSave,
			ErrorValue errorValue, String errorValueKey, Long timeStamp,
			int errorCountToStore) {
		String categoryKey = this.createCategoryKeyByErrorValue(errorValue,
				errorToSave);
		String categoryKeyAllOps = this.createCategoryKeyByErrorValueForAllOps(
				errorValue, errorToSave);
		String severityKey = this.createSeverityKeyByErrorValue(errorValue,
				errorToSave);
		String severityKeyAllOps = this.createSeverityKeyByErrorValueForAllOps(
				errorValue, errorToSave);

		Mutator<String> mutator = HFactory.createMutator(keySpace,
				StringSerializer.get());
		HColumn<Long, String> categoryColumn = HFactory.createColumn(timeStamp,
				errorValueKey, LongSerializer.get(), StringSerializer.get());
		HColumn<Long, String> severityColumn = HFactory.createColumn(timeStamp,
				errorValueKey, LongSerializer.get(), StringSerializer.get());

		// all ops columns
		HColumn<Long, String> categoryColumnForAllOps = HFactory.createColumn(
				timeStamp, errorValueKey, LongSerializer.get(),
				StringSerializer.get());

		HColumn<Long, String> severityColumnAllOps = HFactory.createColumn(
				timeStamp, errorValueKey, LongSerializer.get(),
				StringSerializer.get());

		mutator.insert(categoryKey, "ErrorCountsByCategory", categoryColumn);
		mutator.insert(categoryKeyAllOps, "ErrorCountsByCategory",
				categoryColumnForAllOps);
		mutator.insert(severityKey, "ErrorCountsBySeverity", severityColumn);
		mutator.insert(severityKeyAllOps, "ErrorCountsBySeverity",
				severityColumnAllOps);
		// mutator.execute();
	}

}