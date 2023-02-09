package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
@Disabled
@Autonomous(name = "Attempt1Encoders")
public class James_sAutoByEncoder extends LinearOpMode {
    @Override
    public void runOpMode(){

        DcMotorSimple Fleft =  hardwareMap.get(DcMotorSimple.class, "Fleft");
        DcMotor Bleft =  hardwareMap.dcMotor.get("Bleft");
        DcMotor Fright = hardwareMap.dcMotor.get("Fright");
        DcMotor Bright = hardwareMap.dcMotor.get("Bright");
        DcMotor lift = hardwareMap.dcMotor.get("lift");
        Servo claw = hardwareMap.servo.get("Claw");

        Fleft.setDirection(DcMotorSimple.Direction.REVERSE);
        Fright.setDirection(DcMotorSimple.Direction.REVERSE);
        Bright.setDirection(DcMotorSimple.Direction.REVERSE);
        // Bleft.setDirection(DcMotorSimple.Direction.REVERSE);

        lift.setDirection(DcMotorSimple.Direction.REVERSE);

        Fright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Bleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Bright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //Fleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //Fright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Bleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Bright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setTargetPosition(0);
        //Fright.setTargetPosition(0);
        Bleft.setTargetPosition(0);
        Bright.setTargetPosition(0);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //Fright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Bleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Bright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        claw.setPosition(1);

        waitForStart();
        while(opModeIsActive()){
            telemetry.addData("BleftPosition", Bleft.getCurrentPosition());
            telemetry.addData("BrightPosition", Bright.getCurrentPosition());
            Bleft.setTargetPosition(100);
            Bright.setTargetPosition(100);
            Fleft.setPower(-Bleft.getPower());
            Fright.setPower(-Bright.getPower());
            Bleft.setPower(1);
            Bright.setPower(1);
            if(Bright.getCurrentPosition() >= 100){
                Bleft.setPower(0);
                Bright.setPower(0);
                Fright.setPower(0);
                Fleft.setPower(0);
                sleep(999999999);
            }
            telemetry.update();
        }
    }
}
