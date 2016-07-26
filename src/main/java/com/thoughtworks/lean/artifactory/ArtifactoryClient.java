package com.thoughtworks.lean.artifactory;

import com.thoughtworks.lean.artifactory.domain.BuildArtifacts;

import java.util.List;

/**
 * Created by qmxie on 5/3/16.
 */
public interface ArtifactoryClient {
    List<BuildArtifacts> getBuildArtifacts(String aqlQuery);

    String getUri();
}
