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

(deploy application to multiple instances with a load balancer through AWS Elastic Beanstalk)
TO DO



# Part 4 - Update Application Running in Production

(make a small change to the application and update it)
TO DO

# Part 5 - Setup Cloudwatch alert

(use an AWS Lambda and CloudWatch alert to check the health of the deployed service)