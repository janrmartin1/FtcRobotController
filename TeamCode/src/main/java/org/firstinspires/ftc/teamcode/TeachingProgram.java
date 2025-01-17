package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp
public class TeachingProgram extends LinearOpMode {
    @Override
    public void runOpMode(){
        MasterClass Robot = new MasterClass(); //This creates the Robot object which is a child of MasterClass
        Robot.Init(hardwareMap);//This initializes all of the motors/servos and sets the modes
        waitForStart();
        while(opModeIsActive()){
            Robot.MoveLift("High",true); //Enter in what junction you want the lift to go to and enter reset to go back to the ground

            Robot.MoveLift(180, true);

            Robot.Drive("Forwards", 2); // Enter the direction you want to drive. You do have to put a sleep after for how long you want to drive.
            // It takes 1440 milliseconds to travel 1 block in straight and backwards

            sleep(999); //This will run the stuff above it for however long you put in it REMINDER it reads the numbers as milliseconds not seconds

            Robot.Stop(); // This is self explanatory it stops the robot

            Robot.Turn(2); //Enter in which way you want it to turn Example: CCW = Clockwise and CC = Clockwise

            Robot.Strafe("Left", 2); //Enter in what direction you want it to strafe in. To strafe 1 block it takes 1680 milliseconds for right and left movement

            Robot.Scan(); //You run this when you want to scan the signal cone
            if(Robot.scanned){//This is checking if the robot has scanned yet
                switch(Robot.label){//This is checking the label variable which contains our variable
                    case"black1":telemetry.addData("Marty", "I spy with my little eye something black"); break;//This checks to see if it is black1
                    case"green2":telemetry.addData("Marty", "I spy with my little eye something green"); break;//This checks to see if it is green2
                    case"purple3":telemetry.addData("Marty", "I spy with my little eye something purple"); break;//This checks to see if it is purple3
                    default: telemetry.addData("Marty", "I don't wanna play this game");//This is the default if it sees none of the above
                }
            }

            telemetry.update();//Updates the telemetry so we can see what the robot actually scanned
        }
    }

}
