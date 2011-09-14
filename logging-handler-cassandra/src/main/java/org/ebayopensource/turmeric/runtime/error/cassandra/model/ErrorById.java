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
public class ErrorById {

    /** The category. */
    protected String category;

    /** The domain. */
    protected String domain;

    /** The error id. */
    protected Long errorId;

    /** The name. */
    protected String name;

    /** The organization. */
    protected String organization;

    /** The severity. */
    protected String severity;

    /** The sub domain. */
    protected String subDomain;

    /**
     * Instantiates a new error.
     */
    public ErrorById() {

    }

    /**
     * Instantiates a new error.
     * 
     * @param commonErrorData
     *            the common error data
     */
    public ErrorById(CommonErrorData commonErrorData) {
        this.setErrorId(commonErrorData.getErrorId());
        this.setName(commonErrorData.getErrorName());
        this.setCategory(commonErrorData.getCategory().toString());
        this.setSeverity(commonErrorData.getSeverity().toString());
        this.setDomain(commonErrorData.getDomain());
        this.setSubDomain(commonErrorData.getSubdomain());
        this.setOrganization(commonErrorData.getOrganization());
    }

    /**
     * Instantiates a new error by id.
     * 
     * @param category
     *            the category
     * @param domain
     *            the domain
     * @param errorId
     *            the error id
     * @param name
     *            the name
     * @param organization
     *            the organization
     * @param severity
     *            the severity
     * @param subDomain
     *            the sub domain
     */
    public ErrorById(String category, String domain, Long errorId, String name, String organization, String severity,
                    String subDomain) {
        super();
        this.category = category;
        this.domain = domain;
        this.errorId = errorId;
        this.name = name;
        this.organization = organization;
        this.severity = severity;
        this.subDomain = subDomain;
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
     * Gets the domain.
     * 
     * @return the domain
     */
    public String getDomain() {
        return domain;
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
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
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
     * Gets the severity.
     * 
     * @return the severity
     */
    public String getSeverity() {
        return severity;
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
     * Sets the category.
     * 
     * @param category
     *            the new category
     */
    public void setCategory(String category) {
        this.category = category;
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
     * Sets the error id.
     * 
     * @param errorId
     *            the new error id
     */
    public void setErrorId(Long errorId) {
        this.errorId = errorId;
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
     * Sets the organization.
     * 
     * @param organization
     *            the new organization
     */
    public void setOrganization(String organization) {
        this.organization = organization;
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
     * Sets the sub domain.
     * 
     * @param subDomain
     *            the new sub domain
     */
    public void setSubDomain(String subDomain) {
        this.subDomain = subDomain;
    }

}
