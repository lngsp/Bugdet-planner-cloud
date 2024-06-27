package ro.budget.planner.controllers.requests;

public class UpdateCompletedRequest {

    private String username;
    private String description;
    private boolean completed;

    public UpdateCompletedRequest(){}

    public UpdateCompletedRequest(String username, String description, boolean completed) {
        this.username = username;
        this.description = description;
        this.completed = completed;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getCompleted() {
        return completed;
    }


    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
