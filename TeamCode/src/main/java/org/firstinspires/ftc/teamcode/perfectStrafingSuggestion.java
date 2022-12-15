package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class perfectStrafingSuggestion extends LinearOpMode {
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

        //These are basic variables to save the value of the left stick's x and y
        //i.e. when you tilt the left stick all the way up it = 1
        //And when you tilt it all the way down it = -1
        //Same for the x all the way left = -1 and the other way around
        double y = gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;

        waitForStart();

        if (isStopRequested()) return;

        //Anything inside of this will loop over and over until the op mode ends
        while (opModeIsActive()) {
            if (gamepad1.a) {
                y = 0; // Allows no input for front/back movement
                x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            }
            else {
                y = gamepad1.left_stick_y;  // set y to current stick input level
                x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            }
            //This is the variable that stores the right sticks x value to tell it when to turn
            double rx = gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            //We are replacing the value for the setPower to other variables specified above
            //So it is ever changing
            Fleft.setPower(frontLeftPower);
            Bleft.setPower(backLeftPower);
            Fright.setPower(frontRightPower);
            Bright.setPower(backRightPower);
        }
    }
}