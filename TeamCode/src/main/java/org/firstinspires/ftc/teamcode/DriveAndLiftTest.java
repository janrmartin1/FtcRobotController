package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
@Disabled
@TeleOp
public class DriveAndLiftTest extends LinearOpMode {

    static final double     COUNTS_PER_MOTOR_REV    = 288 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = .5 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);

    static final double     LIFT_HEIGHT                = (int)40 * 22.388; //22 time
    static final double     MAX_LIFT_HEIGHT         = 1100;;
    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        double power = 0.5;
        boolean closed = true;
        // Make sure your ID's match your configuration

        DcMotorSimple Fleft =  hardwareMap.get(DcMotorSimple.class,"Fleft");
        DcMotor Bleft = hardwareMap.dcMotor.get("Bleft");
        DcMotor Fright = hardwareMap.dcMotor.get("Fright");
        DcMotor Bright = hardwareMap.dcMotor.get("Bright");
        DcMotor lift = hardwareMap.dcMotor.get("lift");
        //Servo Claw = hardwareMap.servo.get("Claw");

        // Reverse the right side motors
        // Reverse left motors if you are using NeveRests

        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Fleft.setDirection(DcMotorSimple.Direction.REVERSE);
        Fright.setDirection(DcMotorSimple.Direction.REVERSE);
        Bright.setDirection(DcMotorSimple.Direction.REVERSE);
        lift.setDirection(DcMotorSimple.Direction.REVERSE);
        // lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setTargetPosition(0);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        //Claw.setPosition(0);

        waitForStart();

        int liftTarget = 0;

        //if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = gamepad1.left_stick_y; // Remember, this is reversed!
            double x = -gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = -gamepad1.right_stick_x;

            if (gamepad2.right_bumper) {
                //lift.setPower(.5);
            } else if (gamepad2.left_bumper) {
                //lift.setPower(-.7);
            } else {
                //lift.setPower(0);
                }

                if (gamepad2.dpad_up) {
                    //Set the lift to the top position
                    liftTarget = 800;//lift.getCurrentPosition() + (int)(LIFT_HEIGHT * COUNTS_PER_INCH) / 20;
                } else if (gamepad2.dpad_left) {
                    //Set the lift to middle junction height
                    liftTarget = 500;
                } else if (gamepad2.dpad_right) {
                    //Set the lift to the lowest junction height
                    liftTarget = 250;
                } else if (gamepad2.dpad_down) {
                    //Set the lift to ground position
                    liftTarget = 0;
                }
                if(!(liftTarget > MAX_LIFT_HEIGHT)) {
                    lift.setTargetPosition(liftTarget);
                    lift.setPower(.5);

                if (lift.isBusy()) {
                    telemetry.addData("lift", "Running at %7d",
                            lift.getCurrentPosition());
                    // telemetry.update();
                }
               // lift.setPower(0);
                }

                if (gamepad2.a && closed) {
                    //Claw.setPosition(1);
                    sleep(550);
                    closed = false;
                } else if (gamepad2.a && !closed) {
                    //Claw.setPosition(0);
                    sleep(550);
                    closed = true;
                }
                telemetry.addData("Position", closed);
                //telemetry.addData("Servo Position", Claw.getPosition());
                telemetry.update();

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



