package com.vahagn.barber_line.Classes;

public class Problem {
    private String name;
    private String email;
    private String problem;


    public Problem() {
    }

    public Problem(String name, String email, String problem) {
        this.name = name;
        this.email = email;
        this.problem = problem;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
