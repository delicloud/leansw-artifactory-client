package com.thoughtworks.lean.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.thoughtworks.lean.util.AQLQueryBuilder.eq;
import static com.thoughtworks.lean.util.AQLQueryBuilder.last;


/**
 * Created by qmxie on 4/27/16.
 */
public class AQLQueryBuilderTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AQLQueryBuilderTest.class.getSimpleName());


    @Test
    public void shoud_get_valid_aql_string_when_only_using_add(){
        AQLQueryBuilder queryBuilder = new AQLQueryBuilder();
        queryBuilder.add(eq("repo","test-repo"))
                    .add(eq("artifact.module.name","test-pipeline-1"));
        LOGGER.debug(queryBuilder.build());
        //assertEquals("items.find({\"repo\":{\"$eq\":\"test-repo\"},\"artifact.module.name\":{\"$eq\":\"test-pipeline-1\"}})",queryBuilder.build());
    }

    @Test
    public void should_get_valid_aql_string_when_using_or(){
        AQLQueryBuilder queryBuilder = new AQLQueryBuilder();
        queryBuilder.or(eq("artifact.module.name","test-pipeline-1")
                       ,eq("artifact.module.name","test-pipeline-2"))
                    .add(eq("repo","test-repo"));
        LOGGER.debug(queryBuilder.build());
    }

    @Test
    public void should_get_valid_aql_string_when_using_and(){
        AQLQueryBuilder queryBuilder = new AQLQueryBuilder();
        queryBuilder.and(eq("artifact.module.name","test-pipeline-1")
                ,eq("artifact.module.name","test-pipeline-2"))
                .add(eq("repo","test-repo"));
        LOGGER.debug(queryBuilder.build());
    }

    @Test
    public void should_get_valid_aql_string_when_using_limit(){
        AQLQueryBuilder queryBuilder = new AQLQueryBuilder();
        queryBuilder.or(eq("artifact.module.name","test-pipeline-1")
                ,eq("artifact.module.name","test-pipeline-2"))
                .add(eq("repo","test-repo"))
                .limit(10);
        LOGGER.debug(queryBuilder.build());
    }

    @Test
    public void should_get_valid_aql_string_when_using_offset(){
        AQLQueryBuilder queryBuilder = new AQLQueryBuilder();
        queryBuilder.or(eq("artifact.module.name","test-pipeline-1")
                ,eq("artifact.module.name","test-pipeline-2"))
                .add(eq("repo","test-repo"))
                .offset(3)
                .limit(10);
        LOGGER.debug(queryBuilder.build());
    }

    @Test
    public void should_get_valid_aql_string_when_using_include(){
        AQLQueryBuilder queryBuilder = new AQLQueryBuilder();
        queryBuilder.or(eq("artifact.module.name","test-pipeline-1")
                ,eq("artifact.module.name","test-pipeline-2"))
                .add(eq("repo","test-repo"))
                .include("repo","name","modified")
                .offset(3)
                .limit(10);
        LOGGER.debug(queryBuilder.build());
    }

    @Test
    public void should_get_valid_aql_string_when_query_last_1_day_data() {
        AQLQueryBuilder queryBuilder = new AQLQueryBuilder();
        queryBuilder.or(eq("artifact.module.name", "test-pipeline-1")
                , eq("artifact.module.name", "test-pipeline-2"))
                .add(eq("repo", "test-repo"))
                .add(last("modified", "1d"))
                .include("repo", "name", "modified")
                .offset(3)
                .limit(10);
        LOGGER.debug(queryBuilder.build());
    }


}
