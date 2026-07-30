package com.auth.demo.util;

import com.auth.demo.dto.GithubPullRequest;

public class GithubPullRequestParser {

    public static GithubPullRequest parse(String url) {

        String[] parts = url.split("/");

        GithubPullRequest pr = new GithubPullRequest();

        pr.setOwner(parts[3]);
        pr.setRepo(parts[4]);
        pr.setPullNumber(Integer.parseInt(parts[6]));

        return pr;
    }
}
