package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
@Disabled
@TeleOp
public class NewAndImprovedDrive extends LinearOpMode {
    // variables
    static final double     COUNTS_PER_MOTOR_REV    = 288 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = .5 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     LIFT_HEIGHT                = (int)40 * 22.388; //22 time
    static final double     MAX_LIFT_HEIGHT         = 1000;;
    private ElapsedTime     runtime = new ElapsedTime();    // sets up a timer function
    private ElapsedTime     runtime2 = new ElapsedTime();    // sets up a timer function
    private double          frontLeftPower = 0;     // declare motor power variable
    private double          backLeftPower = 0;      // declare motor power variable
    private double          frontRightPower = 0;    // declare motor power variable
    private double          backRightPower = 0;     // declare motor power variable
    private double          denominator = 1;        // declare motor power calculation variable
    private int             precision = 4;          // chassis motor power reduction factor 1 = full 2 = half power 3 = third power 4 = quarter power
    private double          liftPower = 0.5;        // declare lift power variable
    private int             liftTarget = 0;         // declare lift target position variable
    private boolean         closed = false;          // declare claw status variable
    private double          maxSpeed = 0.50;
    private int             manualConePickup = 6;

    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration

        DcMotorSimple Fleft =  hardwareMap.get(DcMotorSimple.class,"Fleft");
        DcMotor Bleft = hardwareMap.dcMotor.get("Bleft");
        DcMotor Fright = hardwareMap.dcMotor.get("Fright");
        DcMotor Bright = hardwareMap.dcMotor.get("Bright");
        DcMotor lift = hardwareMap.dcMotor.get("lift");
        Servo Claw = hardwareMap.servo.get("Claw");

        // Reverse the right side motors
        // Reverse left motors if you are using NeveRests

        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Fleft.setDirection(DcMotorSimple.Direction.REVERSE);
        Fright.setDirection(DcMotorSimple.Direction.REVERSE);
        Bright.setDirection(DcMotorSimple.Direction.REVERSE);
        lift.setDirection(DcMotorSimple.Direction.REVERSE);

        //Fleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);  // motor controller has a physical switch
        Fright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Bleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Bright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setTargetPosition(0);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Claw.setPosition(1);

        // int liftTarget = 0;

        waitForStart();

        //if (isStopRequested()) return;

        while (opModeIsActive()) {

            // check for driving input

            double y = gamepad1.left_stick_y; // Remember, this is reversed!
            double x = -gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = -gamepad1.right_stick_x;

            // check for lift input

            if (gamepad2.dpad_up && runtime.seconds() > 1.0) {
                //Set the lift to the top position
                liftTarget = 850; //lift.getCurrentPosition() + (int)(LIFT_HEIGHT * COUNTS_PER_INCH) / 20;
                runtime.reset();  //resets runtime delay timer
            } else if (gamepad2.dpad_left && runtime.seconds() > 1.0) {
                //Set the lift to middle junction height
                liftTarget = 600;
                runtime.reset();  //resets runtime delay timer
            } else if (gamepad2.dpad_right && runtime.seconds() > 1.0) {
                //Set the lift to the lowest junction height
                liftTarget = 370; //26.32
                runtime.reset();  //resets runtime delay timer
            } else if (gamepad2.dpad_down && runtime.seconds() > 1.0) {
                //Set the lift to ground position
                liftTarget = 0;
                runtime.reset();  //resets runtime delay timer
            }

            if (gamepad1.x && liftTarget <= 370){
                if(manualConePickup == 5){ liftTarget = 350; }
                else if(manualConePickup == 4){ liftTarget = 325; }
                else if(manualConePickup == 3){ liftTarget = 300; }
                else if(manualConePickup == 2){ liftTarget = 275; }
                else if(manualConePickup == 1){ liftTarget = 250; }
                else if(manualConePickup == 6){ manualConePickup = 1; }
                manualConePickup += 1;
            }
            else if (gamepad1.b && liftTarget <= 370){
                if(manualConePickup == 5){ liftTarget = 350; }
                else if(manualConePickup == 4){ liftTarget = 325; }
                else if(manualConePickup == 3){ liftTarget = 300; }
                else if(manualConePickup == 2){ liftTarget = 275; }
                else if(manualConePickup == 1){ liftTarget = 250; }
                else if(manualConePickup == 0){ manualConePickup = 5; }
                manualConePickup -= 1;
            }
            telemetry.addData("Side Stack Position", manualConePickup);

            // check for lift height fine tuning

            if( (gamepad2.right_bumper) && (liftTarget + 10) < MAX_LIFT_HEIGHT){
                liftTarget = liftTarget + 10;    // push to raise the lift by small increments above preset positions
            }

            // adjust lift height if within safe operating range

            if(!(liftTarget > MAX_LIFT_HEIGHT)) {
                lift.setTargetPosition(liftTarget);
                lift.setPower(liftPower);

                if(lift.isBusy()) {
                    telemetry.addData("lift", "Running at %7d",
                            lift.getCurrentPosition());
                }
            }

            // check for claw activation input

            if (gamepad2.a && closed && runtime2.seconds() > 0.25) {
                Claw.setPosition(1);
                closed = false;
                runtime2.reset();  //resets runtime2 delay timer
            } else if (gamepad2.a && !closed && runtime2.seconds() > 0.25) {
                Claw.setPosition(0);
                closed = true;
                runtime2.reset();  //resets runtime2 delay timer
            }

            telemetry.addData("Position", closed);
            telemetry.addData("Servo Position", Claw.getPosition());
            telemetry.update();

            // calculate motor movement math and adjust according to lift height or manual precision mode selection

            denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            if (lift.getCurrentPosition() > 450 || gamepad1.right_bumper) {   // check for manual or force precision driving mode
                denominator = denominator * precision;
            }

            frontLeftPower = (y + x + rx) / denominator;
            backLeftPower = (y - x + rx) / denominator;
            frontRightPower = (y - x - rx) / denominator;
            backRightPower = (y + x - rx) / denominator;

            if (frontRightPower >= maxSpeed || frontLeftPower >= maxSpeed && !gamepad1.left_bumper){
                frontLeftPower = maxSpeed;
                frontRightPower = maxSpeed;
                backRightPower = maxSpeed;
                backLeftPower = maxSpeed;
            }

            // issue chassis power for movement

            Fleft.setPower(frontLeftPower);
            Bleft.setPower(backLeftPower);
            Fright.setPower(frontRightPower);
            Bright.setPower(backRightPower);

        }

    }
}

