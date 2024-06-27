package ro.budget.planner.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.budget.planner.config.S3Connection;
import ro.budget.planner.controllers.requests.PlanningTransactionRequest;
import ro.budget.planner.controllers.requests.UpdateAmountTransactionRequest;
import ro.budget.planner.controllers.requests.UpdateCompletedRequest;
import ro.budget.planner.exceptions.PlanningTransactionNotFoundException;
import ro.budget.planner.exceptions.UserNotFoundException;
import ro.budget.planner.models.PlanningTransaction;
import ro.budget.planner.models.User;
import ro.budget.planner.repo.PlanningTransactionRepository;
import ro.budget.planner.repo.UserRepository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PlanningTransactionService {


    @Autowired
    private PlanningTransactionRepository planningTransactionRepository;

    @Autowired
    private UserRepository userRepository;


    public List<PlanningTransaction> getAllPlanningTransactions() {
        return planningTransactionRepository.findAll();
    }

    public Optional<PlanningTransaction> getPlanningTransactionById(Long id) {
        Optional<PlanningTransaction> planningTransaction = planningTransactionRepository.findById(id);
        if(planningTransaction==null){
            throw new PlanningTransactionNotFoundException("Transaction not found");
        }
        return planningTransaction;
    }

    public List<PlanningTransaction> getTransactionsByUsername(String username) {
        return planningTransactionRepository.findByUserUsername(username);
    }

    public PlanningTransaction createPlanningTransaction(PlanningTransactionRequest request) {

        User user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            throw new UserNotFoundException("User with username " + request.getUsername() + " not found.");
        }

        PlanningTransaction planningTransaction = new PlanningTransaction();
        planningTransaction.setDescription(request.getDescription());
        planningTransaction.setAmount(request.getAmount());
        planningTransaction.setCompleted(request.isCompleted());
        planningTransaction.setCompletionDate(request.getCompletionDate());
        planningTransaction.setUser(user);

        return planningTransactionRepository.save(planningTransaction);
    }

    public void updateTransactionAmount(UpdateAmountTransactionRequest request) {
        // Caută tranzacția bazată pe username și descrierea unică
        User user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            throw new UserNotFoundException("User with username " + request.getUsername() + " not found.");
        }

        PlanningTransaction transaction = planningTransactionRepository.findByUserAndDescription(user, request.getDescription());
        if (transaction == null) {
            throw new PlanningTransactionNotFoundException("Transaction with description " + request.getDescription() + " not found for user " + user.getUsername());
        }

        transaction.setAmount(request.getNewAmount());
        planningTransactionRepository.save(transaction);
    }

    public void updateCompleted(UpdateCompletedRequest request) {
        User user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            throw new UserNotFoundException("User with username " + request.getUsername() + " not found.");
        }

        PlanningTransaction existingTransaction = planningTransactionRepository.findByUserAndDescription(user, request.getDescription());

        if (existingTransaction == null) {
            throw new PlanningTransactionNotFoundException("Transaction with description " + request.getDescription() + " not found for user " + user.getUsername());
        }
        else{
            existingTransaction.setCompleted(request.getCompleted());

            // Upload record to S3 if completed is true
            if (request.getCompleted()) {
                user.setAmount(user.getAmount()-existingTransaction.getAmount());
                userRepository.save(user);

                String fileContent = "Transaction ID: " + existingTransaction.getId() + "\n"
                        + "Description: " + existingTransaction.getDescription() + "\n"
                        + "Amount: " + existingTransaction.getAmount() + "\n"
                        + "Completion Date: " + existingTransaction.getCompletionDate();

                String bucketName = "buget-planning-s3";
                Date currentTime = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                String formattedDateTime = sdf.format(currentTime);
                String key = "transaction_" + formattedDateTime + ".txt";

                S3Client s3 = S3Connection.createClient();

                try {
                    InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
                    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build();

                    s3.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, fileContent.length()));

                    planningTransactionRepository.delete(existingTransaction);
                } catch (S3Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void deletePlanningTransaction(Long id) {
        planningTransactionRepository.deleteById(id);
    }

}
