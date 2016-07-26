package com.thoughtworks.lean.artifactory.domain;

/**
 * Created by qmxie on 4/29/16.
 */
public class RepoInfo {
    private String repository;
    private String artifactoryUri;

    public RepoInfo() {
        //default constructor
    }

    public RepoInfo(String repository, String artifactoryUri) {
        this.repository = repository;
        this.artifactoryUri = artifactoryUri;
    }

    public String getArtifactoryUri() {
        return artifactoryUri;
    }

    public void setArtifactoryUri(String artifactoryUri) {
        this.artifactoryUri = artifactoryUri;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }
}
