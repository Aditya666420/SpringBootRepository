package com.auth.demo.dto;



public class GithubPullRequest {

    private String owner;
    private String repo;
    private int pullNumber;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public int getPullNumber() {
        return pullNumber;
    }

    public void setPullNumber(int pullNumber) {
        this.pullNumber = pullNumber;
    }
}
