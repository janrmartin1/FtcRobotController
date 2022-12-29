package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class basicDriveControls extends LinearOpMode{


    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        double power = 0.5;
        // Make sure your ID's match your configuration
        DcMotor Fleft = hardwareMap.dcMotor.get("Fleft");
        DcMotor Bleft = hardwareMap.dcMotor.get("Bleft");
        DcMotor Fright = hardwareMap.dcMotor.get("Fright");
        DcMotor Bright = hardwareMap.dcMotor.get("Bright");
        //DcMotor Lift = hardwareMap.dcMotor.get("Lift");
        //Servo Claw = hardwareMap.servo.get("Claw");

        // Reverse the right side motors
        // Reverse left motors if you are using NeveRests
        Fright.setDirection(DcMotorSimple.Direction.REVERSE);
        Bright.setDirection(DcMotorSimple.Direction.REVERSE);
        //Lift.setDirection(DcMotorSimple.Direction.REVERSE);
        //Claw.setPosition(0);

        waitForStart();

        //if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = gamepad1.left_stick_y; // Remember, this is reversed!
            double x = -gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = -gamepad1.right_stick_x;

            if(gamepad1.dpad_up) {
                //Set the lift to the top position
            }
            else if (gamepad1.dpad_left){
                //Set the lift to middle junction height
            }
            else if (gamepad1.dpad_right){
                //Set the lift to the lowest junction height
            }
            else if (gamepad1.dpad_down){
                //Set the lift to ground position
            }


            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            Fleft.setPower(frontLeftPower);
            Bleft.setPower(backLeftPower);
            Fright.setPower(frontRightPower);
            Bright.setPower(backRightPower);

        }

    }
}
