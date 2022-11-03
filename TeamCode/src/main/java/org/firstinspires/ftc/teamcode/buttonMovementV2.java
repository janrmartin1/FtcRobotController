package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;


@TeleOp
public class buttonMovementV2 extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor Fleft = hardwareMap.dcMotor.get("Fleft");
        DcMotor Bleft = hardwareMap.dcMotor.get("Bleft");
        DcMotor Fright = hardwareMap.dcMotor.get("Fright");
        DcMotor Bright = hardwareMap.dcMotor.get("Bright");

        // Reverse the right side motors
        // Reverse left motors if you are using NeveRests
        Fright.setDirection(DcMotorSimple.Direction.REVERSE);
        Bright.setDirection(DcMotorSimple.Direction.REVERSE);

        final DcMotor.ZeroPowerBehavior BRAKE;

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {


           //This will move the robot forward for 1 second when the y button is pressed
            if (gamepad1.a) {
                Fleft.setPower(0.5);
                Bleft.setPower(0.5);
                Fright.setPower(0.5);
                Bright.setPower(0.5);
                sleep(700);
                Fleft.setPower(0);
                Bleft.setPower(0);
                Fright.setPower(0);
                Bright.setPower(0);
            }
            //This will move the robot to the right for 1 second when the b button is pressed
            if (gamepad1.x) {
                //The setPower will set the power of a motor 1 is the max
                Fleft.setPower(1);
                //-1 will make the motor spin backwards
                Bleft.setPower(-1);
                Fright.setPower(-1);
                Bright.setPower(1);
                //This will allow the motors to run at the set power for the time specified
                //The sleep function runs in milliseconds so 1000 milliseconds = 1 second
                sleep(700);
                //Then we set the power of all 4 motors back to 0 to stop them
                Fleft.setPower(0);
                Bleft.setPower(0);
                Fright.setPower(0);
                Bright.setPower(0);
            }
            //This will move the robot backwards for 1 second when the a button is pressed
            if (gamepad1.y) {
                Fleft.setPower(-1);
                Bleft.setPower(-1);
                Fright.setPower(-1);
                Bright.setPower(-1);
                sleep(700);
                Fleft.setPower(0);
                Bleft.setPower(0);
                Fright.setPower(0);
                Bright.setPower(0);
            }
            //This will move the robot to the left for 1 second when the x button is pressed
            if (gamepad1.b) {
                Fleft.setPower(-1);
                Bleft.setPower(1);
                Fright.setPower(1);
                Bright.setPower(-1);
                sleep(700);
                Fleft.setPower(0);
                Bleft.setPower(0);
                Fright.setPower(0);
                Bright.setPower(0);
            }
        }
    }
}