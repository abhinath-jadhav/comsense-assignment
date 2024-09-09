## Springboot project
### JAVA 11 required to run

## Log in
Use the below user pass for testing if you don't register
### Credintials
User : testuser@gmail.com
Pass: testpass

You cannot see upload until you login.

## API Details
- http://localhost:8080/swagger-ui/index.html
- Use Swagger
- For auth first sign incopy the key and save in authorise
- ![Alt text](./src/main/resources/auth.png?raw=true "Title")

## DATABASE
- We Have used PostgresSQL
- All the table will be created if you give you local DB address in YAML file.

# CSV
- I have attached test data CV FYI
- All columns are mandatory if anything is invalid from row then that row will not be processed.
  
### ID (id):

This field is auto-generated and does not require input.
### Employee ID (employeeId):

- Must not be blank or null.
- @NotBlank(message = "EmployeeID is required and cannot be empty.")
- Must be between 5 and 7 characters in length.
- @Size(min = 5, max = 7, message = "Employee ID length should be between 5-7")
- Must start with the letter "E".
- @Pattern(regexp = "E.*", message = "Employee ID should start with E")
### Review Date (reviewDate):

- Must not be null or empty.
- @NotNull(message = "ReviewDate is required and cannot be empty.")
- Must follow the format yyyy-MM-dd'T'HH:mm:ssZ.
- Example: 2024-09-09T12:00:00+0000
### Goal (goal):

- Must not be blank or null.
- @NotBlank(message = "Goal is required and cannot be empty.")
### Achievement (achievement):

- Must not be blank or null.
- @NotBlank(message = "Achievement is required and cannot be empty.")
### Rating (rating):

- Must not be null.
- @NotNull(message = "Rating is required.")
- Must be a decimal number between 1.0 and 5.0.
- @DecimalMin(value = "1.0", message = "Rating must be between 1.0 and 5.0.")
- @DecimalMax(value = "5.0", message = "Rating must be between 1.0 and 5.0.")
### Feedback (feedback):

- Must not be blank or null.
- @NotBlank(message = "Feedback is required and cannot be empty.")

## Contact For any help
### 9503698655
