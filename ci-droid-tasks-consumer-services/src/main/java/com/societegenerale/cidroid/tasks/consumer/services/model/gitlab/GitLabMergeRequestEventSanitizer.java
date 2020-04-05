package com.societegenerale.cidroid.tasks.consumer.services.model.gitlab;

import com.fasterxml.jackson.databind.util.StdConverter;

public class GitLabMergeRequestEventSanitizer extends StdConverter<GitLabMergeRequestEvent, GitLabMergeRequestEvent> {

        @Override
        public GitLabMergeRequestEvent convert(GitLabMergeRequestEvent gitLabPushEvent) {
            return (GitLabMergeRequestEvent)GitLabEventSanitizerUtils.sanitizeProject(gitLabPushEvent);
        }

}

