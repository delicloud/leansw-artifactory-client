package com.thoughtworks.lean.artifactory.impl;


import com.google.common.io.Resources;

import com.thoughtworks.lean.artifactory.domain.Artifact;
import com.thoughtworks.lean.artifactory.domain.BuildArtifacts;
import com.thoughtworks.lean.util.AQLQueryBuilder;
import com.thoughtworks.lean.util.JSONUtil;
import org.jfrog.artifactory.client.Artifactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Charsets.UTF_8;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArtifactoryImplTest {
    ArtifactoryClientImpl artifactoryClient;

    @Mock
    Artifactory artifactory;

    @Before
    public void  setup(){
        artifactoryClient=new ArtifactoryClientImpl(artifactory,"test-repo");
    }

    @Test
    public void should_return_when_get_all_artifact_by_pipeline_name() throws IOException {
        Map responseMap = JSONUtil.parseJSON(Resources.toString(this.getClass().getResource("/artifactory_result.json"), UTF_8));
        when(artifactory.restCall(Matchers.any())).thenReturn(responseMap);

        artifactoryClient.getAllArtifactsByPipelineName("test-pipeline-l1", 10, 0);
    }

    @Test
    public void should_return_when_get_latest_by_team_id() throws IOException {

        //when
        Map responseMap = JSONUtil.parseJSON(Resources.toString(this.getClass().getResource("/artifactory_result.json"), UTF_8));
        when(artifactory.restCall(Matchers.any())).thenReturn(responseMap);
        Map<String, List<Artifact>> resultMap = artifactoryClient.getLatestTeamArtifacts(Collections.singletonList("test-pipeline-1"));
        //then
        assertThat(resultMap.get("deliflow-webapp-0.1.0-SNAPSHOT.jar").size(), greaterThan(0));

    }

    @Test
    public void shoud_return_when_get_latest_by_pipeline_name() throws IOException {
        Map responseMap = JSONUtil.parseJSON(Resources.toString(this.getClass().getResource("/artifactory_result.json"), UTF_8));
        when(artifactory.restCall(Matchers.any())).thenReturn(responseMap);
        artifactoryClient.getLatestTenBuildArtifactsByPipelineName("test-pipeline-1");
    }

    @Test
    public void shoud_return_when_get_artifacts_by_buildNo() throws IOException {
        Map responseMap = JSONUtil.parseJSON(Resources.toString(this.getClass().getResource("/artifactory_search_by_build_result.json"), UTF_8));
        when(artifactory.restCall(Matchers.any())).thenReturn(responseMap);
        BuildArtifacts result = artifactoryClient.getArtifactsByBuild("test-pipeline-2", "29");
        Assert.assertEquals(3, result.getArtifacts().size());
    }

    @Test
    public void should_call_artifactory() throws IOException {

        Map responseMap = JSONUtil.parseJSON(Resources.toString(this.getClass().getResource("/artifactory_result.json"), UTF_8));

        when(artifactory.restCall(Matchers.any())).thenReturn(responseMap);
        when(artifactory.getUri()).thenReturn("test-url");

        // given
        List<BuildArtifacts> artifactsList = artifactoryClient.getBuildArtifacts(new AQLQueryBuilder().add(AQLQueryBuilder.match("haha", "haha")).build());
        // when


        // then

        Assert.assertEquals(23, artifactsList.size());
        //
        assertNotNull(artifactsList.get(0));
        Artifact firstArtifact = artifactsList.get(0).getArtifacts().stream().findFirst().get();
        assertEquals(firstArtifact.getName(), "deliflow-webapp-0.1.0-SNAPSHOT.jar");
        assertEquals(firstArtifact.getSize(), 39959050);
        assertEquals(firstArtifact.getRepository(), "test-repo");
        assertEquals(firstArtifact.getServerUrl(), "test-url");
        assertEquals(firstArtifact.getLastModified().getTime(), 1464757211736L);

    }

    @Test
    public void should_return_empty() throws IOException {

        Map responseMap = new LinkedHashMap<>();

        when(artifactory.restCall(Matchers.any())).thenReturn(responseMap);

        // given
        List<BuildArtifacts> artifactsList = artifactoryClient.getBuildArtifacts(new AQLQueryBuilder().add(AQLQueryBuilder.match("haha", "haha")).build());
        // when


        // then

        Assert.assertEquals(0, artifactsList.size());


    }


    @Test
    public void shoud_return_artifactory_uri() {
        String mockRet = "http://foo/";
        when(artifactory.getUri()).thenReturn(mockRet);
        String uri = artifactoryClient.getUri();
        Assert.assertEquals(mockRet, uri);
    }
}
