package com.societegenerale.cidroid.tasks.consumer.infrastructure.bitbucket.mocks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.mockserver.model.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@Slf4j
public class BitBucketMockServer extends MockServer {

    public static final int BITBUCKET_MOCK_PORT = 9800;

    private final ObjectMapper objectMapper;

    public BitBucketMockServer() {
        super(BITBUCKET_MOCK_PORT);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    protected void initRoutes() {
        mockServer
                .when(request()
                        .withMethod("GET")
                        .withPath("/api/projects/public-project/repos/my-repo/pull-requests"))
                .respond(getOpenPullRequests());

        mockServer
                .when(request()
                        .withMethod("GET")
                        .withPath("/api/projects/public-project/repos/my-repo/pull-requests/[0-9]+"))
                .respond(request -> getPullRequest());

        mockServer
                .when(request()
                        .withMethod("POST")
                        .withPath("/api/projects/public-project/repos/my-repo/pull-requests"))
                .respond(getPullRequest());


        mockServer
                .when(request()
                        .withMethod("GET")
                        .withPath("/api/users/sekhar"))
                .respond(getUser());

        mockServer
                .when(request()
                        .withMethod("POST")
                        .withPath("/api/projects/public-project/repos/my-repo/pull-requests/[0-9]+/comment"))
                .respond(response().withStatusCode(200));

        mockServer
                .when(request()
                        .withMethod("GET")
                        .withPath("/api/projects/public-project/repos/my-repo/raw/Jenkinsfile")
                        .withQueryStringParameter("at", "newJavaImageForJenkinsBuild"))
                .respond(returnContent());

        mockServer
                .when(request()
                        .withMethod("PATCH")
                        .withPath("/api/projects/public-project/repos/my-repo/pull-requests/[0-9]+"))
                .respond(response().withStatusCode(200));
        mockServer
                .when(request()
                        .withMethod("GET")
                        .withPath("/api/projects/public-project/repos/my-repo"))
                .respond(returnRepository());
        mockServer
                .when(request()
                        .withMethod("GET")
                        .withPath("/api/projects/public-project/repos/my-repo/commits/master"))
                .respond(returnReference());
        mockServer
                .when(request()
                        .withMethod("GET")
                        .withPath("/api/projects/public-project/repos/my-repo/commits")
                        .withQueryStringParameter("until", "newJavaImageForJenkinsBuild")
                        .withQueryStringParameter("limit", "1"))

                .respond(returnCommits());
        mockServer
                .when(request()
                        .withMethod("PUT")
                        .withPath("/api/projects/public-project/repos/my-repo/browse/Jenkinsfile"))
                .respond(returnUpdatedResource());
        mockServer
                .when(request()
                        .withMethod("POST")
                        .withPath("/api/projects/public-project/repos/my-repo/branches"))
                .respond(returnReference());
    }

    private HttpResponse returnContent() {
        return response()
                .withBody("JDK1.8-20170718-121455-e2e6123")
                .withHeader("Content-Type", "application/json")
                .withStatusCode(200);
    }

    private HttpResponse returnRepository() {
        return response()
                .withBody(readFromFile("repository.json"))
                .withHeader("Content-Type", "application/json")
                .withStatusCode(200);
    }

    private HttpResponse returnReference() {
        return response()
                .withBody(readFromFile("reference.json"))
                .withHeader("Content-Type", "application/json")
                .withStatusCode(200);
    }

    @SneakyThrows
    private HttpResponse returnCommits() {
        return response()
                .withBody(readFromFile("commits.json"))
                .withHeader("Content-Type", "application/json")
                .withStatusCode(200);
    }

    private HttpResponse returnUpdatedResource() {
        return response()
                .withBody(readFromFile("updatedResource.json"))
                .withHeader("Content-Type", "application/json")
                .withStatusCode(200);
    }

    private HttpResponse getOpenPullRequests() {
        return response()
                .withBody(readFromFile("pullRequests.json"))
                .withHeader("Content-Type", "application/json");
    }

    @SneakyThrows
    private HttpResponse getPullRequest() {
        return response()
                .withBody(readFromFile("singlePullRequest.json"))
                .withHeader("Content-Type", "application/json");
    }

    @SneakyThrows
    private HttpResponse getUser() {
        return response()
                .withBody(readFromFile("user.json"))
                .withHeader("Content-Type", "application/json");
    }

    private String readFromFile(String fileName) {

        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            return IOUtils.toString(Objects.requireNonNull(resourceAsStream), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(String.format("The file %s does not exist", fileName));
        }
    }
}
