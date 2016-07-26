package com.thoughtworks.lean.artifactory.impl;

import com.thoughtworks.lean.artifactory.ArtifactService;
import com.thoughtworks.lean.artifactory.ArtifactoryClient;
import com.thoughtworks.lean.artifactory.domain.Artifact;
import com.thoughtworks.lean.artifactory.domain.BuildArtifacts;
import com.thoughtworks.lean.artifactory.domain.RepoInfo;
import com.thoughtworks.lean.common.Pagination;
import com.thoughtworks.lean.common.PaginationResults;
import com.thoughtworks.lean.util.AQLQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.thoughtworks.lean.util.AQLQueryBuilder.eq;
import static com.thoughtworks.lean.util.AQLQueryBuilder.match;


@Service
public class ArtifactServiceImpl implements ArtifactService {



    @Autowired
    private ArtifactoryClient artifactoryClient;

    @Value("${artifactory.repository}")
    private String repository;

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public void setArtifactoryClient(ArtifactoryClient artifactoryClient) {
        this.artifactoryClient = artifactoryClient;
    }

    public ArtifactServiceImpl() {
    }

    private List<BuildArtifacts> getLatestBuildArtifactsByAQL(String aqlQuery, int number) {
        return getAllBuildArtifacts(aqlQuery, 0, number).getResults();
    }

    @Override
    public PaginationResults<BuildArtifacts> getAllArtifactsByPipelineName(String pipelineName, int page, int pageSize) {
        AQLQueryBuilder queryBuilder = new AQLQueryBuilder();
        queryBuilder.add(eq("repo", repository))
                .add(eq("artifact.module.name", pipelineName))
                .include("*");
        String aqlQuery = queryBuilder.build();
        return getAllBuildArtifacts(aqlQuery, page, pageSize);
    }

    @Override
    public BuildArtifacts getArtifactsByBuild(String pipelineName, String buildNo) {
        String pathPartten = pipelineName + "/" + buildNo + ".*";
        AQLQueryBuilder queryBuilder = new AQLQueryBuilder();
        queryBuilder.add(eq("repo", repository))
                .add(eq("artifact.module.name", pipelineName))
                .add(match("path", pathPartten))
                .include("*");
        String aqlQuery = queryBuilder.build();

        List<BuildArtifacts> result = artifactoryClient.getBuildArtifacts(aqlQuery);

        return result.size() > 0 ? result.get(0) : null;
    }

    // 利用PaginationResults包装,方便分页
    private PaginationResults<BuildArtifacts> getAllBuildArtifacts(String aqlQuery, int page, int pageSize) {

        List<BuildArtifacts> buildArtifacts = artifactoryClient.getBuildArtifacts(aqlQuery);

        int offset = pageSize * page;
        int toIndex = Math.min(offset + pageSize, buildArtifacts.size());
        Pagination pagination = new Pagination(offset, pageSize, buildArtifacts.size());

        return new PaginationResults<>(pagination, buildArtifacts.subList(offset, toIndex));
    }

    @Override
    public List<BuildArtifacts> getLatestTenBuildArtifactsByPipelineName(String pipelineName) {
        AQLQueryBuilder queryBuilder = new AQLQueryBuilder();
        queryBuilder.add(eq("repo", repository))
                .add(eq("artifact.module.name", pipelineName))
                .include("*");
        return getLatestBuildArtifactsByAQL(queryBuilder.toString(), 10);
    }

    @Override
    public Map<String, List<Artifact>> getLatestTeamArtifacts(List<String> pipelineNames) {

        return pipelineNames.stream()
                .flatMap(pipeline -> getLatestTenBuildArtifactsByPipelineName(pipeline).stream())
                .flatMap(artifacts -> artifacts.getArtifacts().stream())
                .collect(Collectors.groupingBy(Artifact::getName));
    }

    @Override
    public RepoInfo getRepoInfomation() {
        return new RepoInfo(repository, artifactoryClient.getUri());
    }

}
