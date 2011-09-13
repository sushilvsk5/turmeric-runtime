/*******************************************************************************
 * Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.runtime.error.cassandra.model;

import org.ebayopensource.turmeric.common.v1.types.CommonErrorData;

/**
 * The Class Error.
 */
public class Error {

    /** The error id. */
    private Long errorId;

    /** The name. */
    private String name;

    /** The category. */
    private String category;

    /** The severity. */
    private String severity;

    /** The domain. */
    private String domain;

    /** The sub domain. */
    private String subDomain;

    /** The organization. */
    private String organization;

    /**
     * Instantiates a new error.
     * 
     * @param commonErrorData
     *            the common error data
     */
    public Error(CommonErrorData commonErrorData) {
        this.setErrorId(commonErrorData.getErrorId());
        this.setName(commonErrorData.getErrorName());
        this.setCategory(commonErrorData.getCategory().toString());
        this.setSeverity(commonErrorData.getSeverity().toString());
        this.setDomain(commonErrorData.getDomain());
        this.setSubDomain(commonErrorData.getSubdomain());
        this.setOrganization(commonErrorData.getOrganization());
    }

    /**
     * Instantiates a new error.
     */
    public Error() {

    }

    /**
     * Gets the error id.
     * 
     * @return the error id
     */
    public Long getErrorId() {
        return errorId;
    }

    /**
     * Sets the error id.
     * 
     * @param errorId
     *            the new error id
     */
    public void setErrorId(Long errorId) {
        this.errorId = errorId;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the category.
     * 
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category.
     * 
     * @param category
     *            the new category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the severity.
     * 
     * @return the severity
     */
    public String getSeverity() {
        return severity;
    }

    /**
     * Sets the severity.
     * 
     * @param severity
     *            the new severity
     */
    public void setSeverity(String severity) {
        this.severity = severity;
    }

    /**
     * Gets the domain.
     * 
     * @return the domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Sets the domain.
     * 
     * @param domain
     *            the new domain
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * Gets the sub domain.
     * 
     * @return the sub domain
     */
    public String getSubDomain() {
        return subDomain;
    }

    /**
     * Sets the sub domain.
     * 
     * @param subDomain
     *            the new sub domain
     */
    public void setSubDomain(String subDomain) {
        this.subDomain = subDomain;
    }

    /**
     * Gets the organization.
     * 
     * @return the organization
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * Sets the organization.
     * 
     * @param organization
     *            the new organization
     */
    public void setOrganization(String organization) {
        this.organization = organization;
    }

}
