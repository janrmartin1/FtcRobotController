package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class testCalibration extends LinearOpMode{
    static final double     COUNTS_PER_MOTOR_REV    = 383.6 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);

    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        double power = 0.5;
        // Make sure your ID's match your configuration
        DcMotor Fleft = hardwareMap.dcMotor.get("Fleft");
        DcMotor Bleft = hardwareMap.dcMotor.get("Bleft");
        //DcMotor Fright = hardwareMap.dcMotor.get("Fright");
        DcMotor Bright = hardwareMap.dcMotor.get("Bright");

        // Reverse the right side motors
        // Reverse left motors if you are using NeveRests
        //Fright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Bright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //Fright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Bright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            int newLeftTarget;
            int newRightTarget;


            newLeftTarget = Bright.getCurrentPosition() + (int)(50.2655 * COUNTS_PER_INCH);
            //newRightTarget = Fright.getCurrentPosition() + (int)(50.2655 * COUNTS_PER_INCH);
            Bright.setTargetPosition(newLeftTarget);
            //Fright.setTargetPosition(newRightTarget);


            Bright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //Fright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            Bright.setPower(.2);
            //Fright.setPower(.2);

            while(Bright.isBusy()){
                telemetry.addData("Bright", "Running at %7d :%7d",
                        Bright.getCurrentPosition());
            }
            Bright.setPower(0);
            //Fright.setPower(0);
            //sleep(100000000);
            break;
        }

    }
}
