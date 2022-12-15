package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous
public class redHighJuncParkCorner extends LinearOpMode {

    @Override
    public void runOpMode(){
        // Declare our motors
        double power = 0.5;
        // Make sure your ID's match your configuration
        DcMotor Fleft = hardwareMap.dcMotor.get("Fleft");
        DcMotor Bleft = hardwareMap.dcMotor.get("Bleft");
        DcMotor Fright = hardwareMap.dcMotor.get("Fright");
        DcMotor Bright = hardwareMap.dcMotor.get("Bright");

        // Reverse the right side motors
        // Reverse left motors if you are using NeveRests
        Fright.setDirection(DcMotorSimple.Direction.REVERSE);
        Bright.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            Fright.setPower(1);
            Fleft.setPower(1);
            Bright.setPower(1);
            Bleft.setPower(1);
            sleep(1000);
            Fright.setPower(0);
            Fleft.setPower(0);
            Bright.setPower(0);
            Bleft.setPower(0);





        }
    }
}
