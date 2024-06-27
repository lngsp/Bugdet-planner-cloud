package ro.budget.planner.exceptions;

public class PlanningTransactionNotFoundException extends RuntimeException {
    public PlanningTransactionNotFoundException(String message) {
        super(message);
    }
}
