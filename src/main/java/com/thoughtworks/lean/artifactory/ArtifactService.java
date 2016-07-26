package com.thoughtworks.lean.artifactory;


import com.thoughtworks.lean.artifactory.domain.Artifact;
import com.thoughtworks.lean.artifactory.domain.BuildArtifacts;
import com.thoughtworks.lean.artifactory.domain.RepoInfo;
import com.thoughtworks.lean.common.PaginationResults;

import java.util.List;
import java.util.Map;

/**
 * Created by qmxie on 4/26/16.
 */
public interface ArtifactService {

    List<BuildArtifacts> getLatestTenBuildArtifactsByPipelineName(String pipelineName);

    PaginationResults<BuildArtifacts> getAllArtifactsByPipelineName(String pipelineName, int page, int pageSize);

    Map<String, List<Artifact>> getLatestTeamArtifacts(List<String> pipelineNames);

    RepoInfo getRepoInfomation();

    BuildArtifacts getArtifactsByBuild(String pipelineName, String buildNo);
}
