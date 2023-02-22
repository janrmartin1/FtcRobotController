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
            Robot.MoveLift("High"); //Enter in what junction you want the lift to go to and enter reset to go back to the ground

            Robot.Drive("Forwards"); // Enter the direction you want to drive. You do have to put a sleep after for how long you want to drive

            sleep(999); //This will run the stuff above it for however long you put in it REMINDER it reads the numbers as milliseconds not seconds

            Robot.StopDrive(); // This is self explanitory it stops the drive function above

            Robot.Turn("CCW"); //Enter in which way you want it to turn
        }
    }

}
