package com.thoughtworks.lean.artifactory.domain;

import java.util.Date;

/**
 * Created by qmxie on 4/26/16.
 */
public class Artifact {

    private String name;
    private String path;
    private Date lastModified;
    private long size;
    private String repository;
    private String serverUrl;
    private Date created;
    private String md5;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public Date getCreated() {
        return created;
    }

    public Artifact setCreated(Date created) {
        this.created = created;
        return this;
    }

    public String getBuildNo() {
        return this.path.substring(this.path.lastIndexOf('/') + 1);
    }

    public String getMd5() {
        return md5;
    }

    public Artifact setMd5(String md5) {
        this.md5 = md5;
        return this;
    }

    @Override
    public String toString() {
        return "Artifact{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", lastModified=" + lastModified +
                ", size=" + size +
                ", repository='" + repository + '\'' +
                ", serverUrl='" + serverUrl + '\'' +
                ", created=" + created +
                ", md5='" + md5 + '\'' +
                '}';
    }
}
