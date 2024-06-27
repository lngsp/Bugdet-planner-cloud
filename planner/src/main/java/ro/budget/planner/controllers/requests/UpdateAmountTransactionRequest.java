package ro.budget.planner.controllers.requests;

public class UpdateAmountTransactionRequest {

    private String username;
    private String description;
    private Double newAmount;

    public UpdateAmountTransactionRequest() {
    }

    public UpdateAmountTransactionRequest(String username, String description, Double newAmount) {
        this.username = username;
        this.description = description;
        this.newAmount = newAmount;
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

    public Double getNewAmount() {
        return newAmount;
    }

    public void setNewAmount(Double newAmount) {
        this.newAmount = newAmount;
    }
}

