package com.thoughtworks.lean.artifactory.impl;

import com.google.common.io.Resources;
import com.thoughtworks.lean.artifactory.ArtifactoryClient;
import com.thoughtworks.lean.artifactory.domain.Artifact;
import com.thoughtworks.lean.artifactory.domain.BuildArtifacts;
import com.thoughtworks.lean.artifactory.impl.ArtifactoryClientImpl;
import com.thoughtworks.lean.util.JSONUtil;
import org.jfrog.artifactory.client.Artifactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Charsets.UTF_8;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArtifactServiceImplTest {
    @InjectMocks
    ArtifactServiceImpl artifactService = new ArtifactServiceImpl();

    @Mock
    Artifactory artifactory;

    @InjectMocks
    private ArtifactoryClient artifactoryClient = new ArtifactoryClientImpl();

    @Test
    public void should_return_when_get_all_artifact_by_pipeline_name() throws IOException {
        artifactService.setArtifactoryClient(artifactoryClient);
        Map responseMap = JSONUtil.parseJSON(Resources.toString(this.getClass().getResource("/artifactory_result.json"), UTF_8));
        when(artifactory.restCall(Matchers.any())).thenReturn(responseMap);

        artifactService.getAllArtifactsByPipelineName("test-pipeline-l1", 10, 0);
    }

    @Test
    public void should_return_when_get_latest_by_team_id() throws IOException {

        //when
        artifactService.setArtifactoryClient(artifactoryClient);
        Map responseMap = JSONUtil.parseJSON(Resources.toString(this.getClass().getResource("/artifactory_result.json"), UTF_8));
        when(artifactory.restCall(Matchers.any())).thenReturn(responseMap);
        Map<String, List<Artifact>> resultMap = artifactService.getLatestTeamArtifacts(Collections.singletonList("test-pipeline-1"));
        //then
        assertThat(resultMap.get("deliflow-webapp-0.1.0-SNAPSHOT.jar").size(), greaterThan(0));

    }

    @Test
    public void shoud_return_when_get_latest_by_pipeline_name() throws IOException {
        artifactService.setArtifactoryClient(artifactoryClient);
        Map responseMap = JSONUtil.parseJSON(Resources.toString(this.getClass().getResource("/artifactory_result.json"), UTF_8));
        when(artifactory.restCall(Matchers.any())).thenReturn(responseMap);
        artifactService.getLatestTenBuildArtifactsByPipelineName("test-pipeline-1");
    }

    @Test
    public void shoud_return_when_get_artifacts_by_buildNo() throws IOException {
        artifactService.setArtifactoryClient(artifactoryClient);
        Map responseMap = JSONUtil.parseJSON(Resources.toString(this.getClass().getResource("/artifactory_search_by_build_result.json"), UTF_8));
        when(artifactory.restCall(Matchers.any())).thenReturn(responseMap);
        BuildArtifacts result = artifactService.getArtifactsByBuild("test-pipeline-2", "29");
        assertEquals(3, result.getArtifacts().size());
    }

}
