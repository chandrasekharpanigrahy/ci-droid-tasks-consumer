package com.societegenerale.cidroid.tasks.consumer.services.model.gitlab;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.societegenerale.cidroid.tasks.consumer.services.TestUtils.readFromInputStream;
import static org.assertj.core.api.Assertions.assertThat;

class GitLabPushEventTest {

    @Test
    void canDeserialize() throws IOException {

        String  pushEventPayload = readFromInputStream(getClass().getResourceAsStream("/pushEventGitLab.json"));

        GitLabPushEvent pushEvent = new ObjectMapper().readValue(pushEventPayload, GitLabPushEvent.class);

        assertThat(pushEvent).isNotNull();

        assertThat(pushEvent.getRepository()).isNotNull();
        assertThat(pushEvent.getRepository().getFullName()).isNotNull();
        assertThat(pushEvent.getRepository().getDefaultBranch()).isEqualTo("master");
        assertThat(pushEvent.getRepository().getId()).isEqualTo(15);

        assertThat(pushEvent.getUserEmail()).isEqualTo("john@example.com");
        assertThat(pushEvent.getUserName()).isEqualTo("John Smith");

        assertThat(pushEvent.getRef()).isEqualTo("refs/heads/master");

        assertThat(pushEvent.getNbCommits()).isGreaterThan(0);

        assertThat(pushEvent.getCommits()).isNotEmpty();
    }

}