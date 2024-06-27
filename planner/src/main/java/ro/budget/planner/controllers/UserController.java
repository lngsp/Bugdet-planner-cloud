package ro.budget.planner.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.budget.planner.config.ApiGatewayConfig;
import ro.budget.planner.config.S3Connection;
import ro.budget.planner.controllers.requests.TotalAmountRequest;
import ro.budget.planner.controllers.requests.UserLoginRequest;
import ro.budget.planner.exceptions.UserNotFoundException;
import ro.budget.planner.models.User;
import ro.budget.planner.services.UserService;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

@RestController
@RequestMapping("/api/planner/users")
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final ApiGatewayConfig apiGatewayConfig;


    public UserController(UserService userService, ApiGatewayConfig apiGatewayConfig) {
        this.userService = userService;
        this.apiGatewayConfig = apiGatewayConfig;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        if(registeredUser == null){
            return new ResponseEntity<>("User already exists!", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequest loginRequest) {
        try {
            User authenticatedUser = userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
            System.out.println("USER LOGAT " + loginRequest.getPassword() + " "+  loginRequest.getUsername());
            return new ResponseEntity<>(authenticatedUser, HttpStatus.OK);
        }catch (UserNotFoundException e){
            return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/amount")
    public ResponseEntity<?> setTotalAmount(@RequestBody TotalAmountRequest totalAmountRequest) {
        try{
            userService.setTotalAmount(totalAmountRequest.getUsername(), totalAmountRequest.getAmount());
            return new ResponseEntity<>("Amount added!", HttpStatus.OK);
        }catch (UserNotFoundException e){
            return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/amount/{username}")
    public ResponseEntity<?> getTotalAmount(@PathVariable String username) {
        try{
            Double totalAmount = userService.getTotalAmount(username);
            return new ResponseEntity<>(totalAmount, HttpStatus.OK);
        }catch (UserNotFoundException e){
            return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/email/{username}")
    public ResponseEntity<?> getEmail(@PathVariable String username) {
        try{
            String email = userService.getUserEmail(username);
            return new ResponseEntity<>(email, HttpStatus.OK);
        }catch (UserNotFoundException e){
            return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/history")
    public void listObjects() {
        String bucketName = "buget-planning-s3";
        S3Client s3 = S3Connection.createClient();

        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();

        ListObjectsV2Response listResponse = s3.listObjectsV2(listRequest);
        for (S3Object object : listResponse.contents()) {
            System.out.println("Object Key: " + object.key());
        }
    }

    @PostMapping("/newsletters/{username}")
    public ResponseEntity<?> subscribe(@PathVariable String username){
        String email = userService.getUserEmail(username);
        apiGatewayConfig.subscription(email);
        return new ResponseEntity<>("Subscription created!", HttpStatus.OK);
    }

    //for test only
    @PostMapping("/newsletters-publish")
    public ResponseEntity<?> publishMessage(){
        apiGatewayConfig.publishMessage();
        return new ResponseEntity<>("Message published!", HttpStatus.OK);
    }


}
