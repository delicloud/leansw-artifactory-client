package com.thoughtworks.lean.artifactory.impl;

import com.google.common.collect.Multimaps;
import com.google.common.collect.TreeMultimap;
import com.thoughtworks.lean.artifactory.ArtifactoryClient;
import com.thoughtworks.lean.artifactory.domain.Artifact;
import com.thoughtworks.lean.artifactory.domain.BuildArtifacts;
import com.thoughtworks.lean.util.MapUtil;
import org.jfrog.artifactory.client.Artifactory;
import org.jfrog.artifactory.client.ArtifactoryRequest;
import org.jfrog.artifactory.client.impl.ArtifactoryRequestImpl;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Created by qmxie on 5/3/16.
 */
@Component
public class ArtifactoryClientImpl implements ArtifactoryClient {

    @Autowired
    private Artifactory artifactory;

    @Override
    @Cacheable("buildArtifacts")
    public List<BuildArtifacts> getBuildArtifacts(String aqlQuery) {
        Map response = getAQLResponse(aqlQuery);
        List<Artifact> allArtifacts = this.getArtifactsFromResponse(response);

        TreeMultimap<String, Artifact> resultMap = this.getSortedMultimap(allArtifacts);

        Set<String> keys = resultMap.asMap().keySet();

        return keys.stream()
                .map(key -> new BuildArtifacts(key, resultMap.asMap().get(key)))
                .collect(Collectors.toList());
    }

    private TreeMultimap<String, Artifact> getSortedMultimap(List<Artifact> allArtifacts) {
        // paths are sorted (latest build first)
        // in each build, artifacts are sorted (last modified artifact first)
        TreeMultimap<String, Artifact> resultMap = TreeMultimap.create(
                (pathLeft, pathRight) -> (int) (getBuildNumber(pathRight) - getBuildNumber(pathLeft)),
                (artifactLeft, artifactRight) -> artifactLeft.getLastModified().compareTo(artifactRight.getLastModified()));
        resultMap.putAll(Multimaps.index(allArtifacts, Artifact::getPath));
        return resultMap;
    }

    private Map getAQLResponse(String aqlQuery) {
        ArtifactoryRequest repositoryRequest = new ArtifactoryRequestImpl()
                .apiUrl("api/search/aql")
                .requestBody(aqlQuery)
                .method(ArtifactoryRequest.Method.POST)
                .requestType(ArtifactoryRequest.ContentType.TEXT)
                .responseType(ArtifactoryRequest.ContentType.JSON);
        return artifactory.restCall(repositoryRequest);
    }

    // sorted by LastModified time from newest to oldest
    private List<Artifact> getArtifactsFromResponse(Map response) {
        List<Map> results = MapUtil.get(response, "results");
        if (isEmpty(results)) {
            return Collections.emptyList();
        }
        return results.stream().map(map -> {
            Artifact artifact = new Artifact();
            artifact.setName(map.get("name").toString());
            artifact.setLastModified(DateTime.parse(map.get("modified").toString()).toDate());
            artifact.setPath(map.get("path").toString());
            artifact.setSize(Long.parseLong(map.get("size").toString()));
            artifact.setRepository(map.get("repo").toString());
            artifact.setCreated(DateTime.parse(map.get("created").toString()).toDate());
            artifact.setMd5(map.get("actual_md5").toString());
            artifact.setServerUrl(artifactory.getUri());
            return artifact;
        }).sorted((o1, o2) -> o2.getLastModified().compareTo(o1.getLastModified())).collect(Collectors.toList());
    }

    private double getBuildNumber(String path) {
        return Double.parseDouble(path.substring(path.lastIndexOf('/') + 1));
    }

    @Override
    public String getUri() {
        return artifactory.getUri();
    }

    void setArtifactory(Artifactory artifactory) {
        this.artifactory = artifactory;
    }
}
