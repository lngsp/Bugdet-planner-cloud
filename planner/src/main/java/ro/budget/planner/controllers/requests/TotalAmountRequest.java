package ro.budget.planner.controllers.requests;

public class TotalAmountRequest {

    private String username;
    private Double amount;

    // Getteri și setteri

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
