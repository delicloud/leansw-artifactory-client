package com.thoughtworks.lean.artifactory.domain;

import com.thoughtworks.lean.artifactory.domain.Artifact;

import java.util.Collection;

/**
 * Created by qmxie on 4/29/16.
 */
// pipeline level
public class BuildArtifacts {
    private String buildPath;
    Collection<Artifact> artifacts;

    public BuildArtifacts(String buildPath, Collection<Artifact> artifacts) {
        this.buildPath = buildPath;
        this.artifacts = artifacts;
    }

    public Collection<Artifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(Collection<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    public String getBuildPath() {
        return buildPath;
    }

    public void setBuildPath(String buildPath) {
        this.buildPath = buildPath;
    }
}
