# Part 1 - Run Application Locally

- Clone the application to your local machine  `git clone https://github.com/james-willett/saplabs-demo.git`
- Compile and build the application `./mvnw clean compile package`
- Launch the application - `java -jar target/video-game-db-0.0.1-SNAPSHOT.jar`
- Browse to http://localhost:8080/
- Play around with the Swagger to make API calls to the application

# Part 2 - Deploy application on AWS manually

- Log in to AWS https://595773143287.signin.aws.amazon.com/console
- Change region to US-WEST2 (Oregon)

## Create a VPC

- Search for VPC in AWS Console and click on it
- Click `Create VPC`
- Choose `VPC and more`
- Create it as per the screenshot

## Create an EC2 Instance

### Create a Key Pair for EC2

- Go to the EC2 Console in AWS
- Click on `Key Pairs` from the left menu
- Create a Key Pair as per the screenshot
- Download and save the PEM file somewhere locally

### Create a Security Group

- Click on `security groups` on the left hand menu`
- Click `create security group`
- Provide the security group name, and select our VPC
- Add `Inbound Rules` for `SSH` with the source as `ANYWHERE IPV4`
- Add `Custom TCP` for port `8080` with the source as `ANYWHERE IPV4`
- Leave the `Outbound rules` as the defaults
- Click `Create Security Group`

### Deploy the EC2 Instance

- Click on `Instances` on the left hand menu
- Click `launch instances`
- Enter a name for the instance
- Leave the selection on Amazon Linux, t2 micro
- Choose the keypair we just created
- Click `Edit` on network settings
- Change to the SAPLABS VPC we created previously
- Change to a Public Subnet
- Set `Auto Assign IP` to TRUE
- Select the previously created security group
- See the screenshot
- Finally click `Launch Instance`

## Connect to the Instance

- Locate the instance in the AWS EC2 Console
- Wait for the instance to startup and checks to pass
- Get the `Ip v4` of the instance
- Open a terminal, in the same location that you saved the SSH key we downloaded previously
- change the permisions on the keypair with `chmod 400 saplabs-keypair.pem`
- ssh to the instance with `ssh -i "saplabs-keypair.pem" ec2-user@35.89.255.154` (change the IP to the IP of your instance)

### Install Java

- Do `sudo yum update` to update 
- Install Java with `sudo yum install java-17-amazon-corretto-headless.x86_64`
- Do `java -version` to check Java is installed
- Disconnect from the instance by typing `exit`

### Copy Application to Instance

- Open a terminal window in the folder you built the application in part 1
- Copy the file to the EC2 instance with `scp -i "saplabs-keypair.pem" target/video-game-db-0.0.1-SNAPSHOT.jar ec2-user@35.89.255.154:/home/ec2-user`

### Run the application on the instance

- Ssh back to the application `ssh -i "saplabs-keypair.pem" ec2-user@35.89.255.154`
- Type `pwd` to check you are in the `/home/ec2-user`
- Type `ls -la` to check the `video-game-db-0.0.1-SNAPSHOT.jar` exists
- Run the application with `java -jar video-game-db-0.0.1-SNAPSHOT.jar`
- Open a browser and go to `http://35.89.255.154:8080/` (change the IP to the IP of your instance)
- You should be able to see the Swagger UI


## Create an AMI Image

- In AWS console go to the Image
- Choose `Actions > Image & Templates > Create Image`
- Enter a name e.g. `saplabs-demo-image`
- Click `Create Image`
- Click AMIs on the left hand menu, you should see the image there


## Create S3 bucket

- Open S3 in AWS console
- Click `create bucket`
- Name the bucket e.g. `saplabs-20231208`
- Untick `Block all public access`
- Click `create bucket`
- Locate the bucket and click on it


## Create bash script that will deploy the application

- Open the terminal again in the project folder
- Create a new file called `startup.sh`
- Add the following script:

```
#!/bin/bash
cd /home/ec2-user
aws s3 cp s3://saplabs-20231208/video-game-db-0.0.1-SNAPSHOT.jar .
java -jar video-game-db-0.0.1-SNAPSHOT.jar
```

- Do `mvn clean package` to create a new JAR file with this script inside

## Upload the JAR file to the bucket

- Go back to the AWS S3 console
- Upload the JAR file to the bucket as per the screenshot

## Launch Another Image

- Go back to the EC2 Console
- Choose Launch Instance
- Name is `saplabsdemo-2`
- Choose `MY AMIs`
- Select the AMI we created previously
- Choose the Keypair we created previously
- Edit network settings
- Choose the VPC we created previously
- Change the subnet to a PUBLIC one
- Enable `Auto Assign IP`
- Choose the security group we created previously
- Expand `advanced details`
- Go down to `User Data`
- Either select the `startup.sh` file we just created, or copy and paste the script in
- Click `Launch Instance`
- Wait for the new instance to spin up, and for status checks to pass
- Find the public Ip V4 address of the instance
- Open a browser and go to `http://34.216.52.179:8080/` (replace with your IP)
- The application should be running and you should see the Swagger UI

## Cleanup

- Go back to the EC2 console
- Terminate both of the instances



# Part 3 - Deploy Application Through AWS Elastic Beanstalk

## Access Elastic Beanstalk

- Go back to AWS Console and search for `Elastic Beanstalk` service
- Choose `Create Application`
- Enter application name as `Sap Labs Demo`

## Create Environment

- For domain enter `saplabsdemo`
- For `Platform` choose `Java`
- For `Platform branch` choose `Corretto 17` , leave platform version as default
- Select `Upload your code`, then `Local file`
- For version label enter `v1`
- Click `Choose file` then locate the JAR file you built in the `target` folder of your project
- Change `Configuration presets` to `Single instance (free tier eligible)`
- Click `next`

## Service access

- Choose `Use an existing service role`
- Leave existing service role as the default of `aws-elasticbeankstalk-service-role`
- Choose EC2 Keypair of `saplabs-keypair` that we created previously
- Leave `EC2 Instance profile` as default of `aws-elasticbeanstalk-ec2-role`
- Click `next`

## Networking

- For the `VPC` choose the `saplabs-vpc`
- Tick `Public IP Address` to `Activated`
- Select zones `us-west-2a` and `us-west-2b` as the instance subnets. Make sure they are the public subnets
- Scroll to bottom and click `next`

## Instance Traffic and Scaling

- For `EC2 security groups` tick `saplabs-security-group`
- For `Capacity` select environment type as `Single Instance`
- Scroll to bottom and click `next`

## Monitoring

- Untick `Managed Updates` from `Activated`
- Scroll to bottom and click `Add environment property`. Add a property called `SERVER_PORT` with a value of `5000`. We need this because the Elastic Beanstalk Load balancer listens on port 5000 by default.
- Click `Next`

## Review

- Check the settings, scroll to the bottom
- Click `Submit` to begin the environment deployment

## Access your environment

- Elastic Beanstalk will take a few minutes to launch the environment
- You can see the event logs refresh every few seconds as the activities take place
- Once the application is deployed, click on the `Domain` (e.g. `saplabsdemo.us-west-2.elasticbeanstalk.com`) to access the running application
- Make a few API calls in the Swagger
- Go to `Instances` in AWS EC2 - from here you can view the running instances of your application
- Go back to Elastic Beanstalk, click on the `Monitoring` tab. You can see some metrics here.



# Part 4 - Update Application Running in Production

## Make a change to the application

- In your IDE, open the `src/main/java/io/videogamedb/api/SwaggerConfig.java` file
- On line 39 change the description, e.g. add `UPDATED` at the start
- Save the file

## Rebuild the application Jar file

- In the terminal do `./mvnw clean package` to rebuild the Jar file

## Upload the new version to Elastic Beanstalk

- Open AWS Elastic Beanstalk
- Go the environment that you deployed previously
- Click `upload and deploy`
- Click choose file
- Select the new JAR file we just built from the `/target` folder of our project
- Click `Deploy`
- AWS should now start deploying the new version of our application, it will take a few minutes

## Check the application is updated

- Open the Swagger page again for the application
- Check the description, it should say `UPDATED`




# Part 5 - Setup Cloudwatch alert

(use an AWS Lambda and CloudWatch alert to check the health of the deployed service)

## Create the Lambda that does the healthcheck

- In AWS Console, open the `Lambda` page
- Click `Create Function`
- Select `Use a blueprint`
- For the blueprint scroll down to the bottom and choose `Schedule a periodic check of any URL`
- For the Function name enter `saplabs-healthcheck`
- For execution role choose `Create a new role with basic Lambda permissions`
- Next to `EventBridge` choose `Create a new rule`
- Change rule name to `saplabs-event`
- For `schedule expression` enter `rate(2 minutes)`

- Under `environment variables` for the `site` change this to `http://saplabsdemo.us-west-2.elasticbeanstalk.com/swagger-ui/index.html` (or whatever the URL for the Swagger site is)
- Then change `expected` to `<!-- HTML for static distribution bundle build -->` - this the top line of HTML that appears if the page is working correctly
- Click on `Create Function`

## Test the function

- Once the function is created, click on the `Test` tab
- Click `Test`
- You should see a Green checkbox to show that the function works

## Create the Cloudwatch Alarm

- In AWS Console go to `Cloudwatch`
- Click on `All Alarms` on the left
- Click `Create Alarm`
- Click `Select Metric`
- Under browse click `Lambda`
- Click `By Function Name`
- Tick `saplabs-healthcheck` `Errors`
- Click `Select Metric`
- Scroll down to `conditions`
- Choose `Greater/Equal`
- Enter `1` for the threshold
- Click next

## SNS Notfication

- Click `Create New Topic`
- Name is `SAP Labs Topic`
- SAP_Labs_Topic
- Type an email address for the endpoint
- Click `Create Topic`
- Scroll down and click `next`

## Create the Alarm

- Enter the name as `Sap Labs Healthcheck`
- Click next
- Click Create Alarm

## Monitor the Alarm

- Now that the alarm is created, you can click on it in Cloud Watch
- Wait a few minutes for the check to fire a few times

## Cleanup

- Find the environment you created in Elastic Beanstalk
- Click `Actions` then `Terminate Environment`
- Enter the environment name then click `Terminate`
- Wait a few minutes for the environment to be terminated, and the instances destoryed

## Alarm is triggered

- Go back to the Cloudwatch alarms
- Wait a few minutes
- Now that the application is offline, the alarm should trigger
- Delete the alarm to stop it from triggering



