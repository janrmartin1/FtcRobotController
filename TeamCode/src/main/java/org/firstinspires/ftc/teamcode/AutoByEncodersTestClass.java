package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
@Disabled
@Autonomous
public class AutoByEncodersTestClass extends LinearOpMode {

    int LTarget = 0;
    int RTarget = 0;
    double runPower = 0.35;
    @Override
    public void runOpMode(){

        // Declare the motors
        DcMotorSimple Fleft =  hardwareMap.get(DcMotorSimple.class,"Fleft");
        DcMotor Bleft = hardwareMap.dcMotor.get("Bleft");
        DcMotor Fright = hardwareMap.dcMotor.get("Fright");
        DcMotor Bright = hardwareMap.dcMotor.get("Bright");
        DcMotor lift = hardwareMap.dcMotor.get("lift");
        Servo claw = hardwareMap.servo.get("Claw");

        // Reverse the motors
        Fleft.setDirection(DcMotorSimple.Direction.REVERSE);
        Fright.setDirection(DcMotorSimple.Direction.REVERSE);
        Bright.setDirection(DcMotorSimple.Direction.REVERSE);
        lift.setDirection(DcMotorSimple.Direction.REVERSE);

        // Set the motor behavior
        Fright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Bleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Bright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Fright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Bleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Bright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setTargetPosition(0);
        Fright.setTargetPosition(0);
        Bleft.setTargetPosition(0);
        Bright.setTargetPosition(0);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Fright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Bleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Bright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        claw.setPosition(1);

        waitForStart();

        if (isStopRequested()) return;
        while(opModeIsActive()){                // attempt to drive to a set motor position and mimic back motor power to the front motors in real time
            // set up telemetry to view values as the opmode executes
            telemetry.addLine()
                    .addData("FL Power", Fleft.getPower())
                    .addData("FR Power", Fright.getPower());
            telemetry.addLine()
                    .addData("BL Power", Bleft.getPower())
                    .addData("BR Power", Bright.getPower());
            telemetry.addLine()
                    .addData("BR Enc value",Bright.getCurrentPosition())
                    .addData("BL Enc value",Bleft.getCurrentPosition());
            telemetry.update();

            // simply move straight (hopefully) forward to encoder position 2000
            RTarget = 2000;
            LTarget = 2000;
            // runPower = .3;  
            Fright.setTargetPosition(RTarget);              // set encoded motors and power in RTP mode
            Bleft.setTargetPosition(RTarget);
            Bright.setTargetPosition(LTarget);
            Fright.setPower(runPower);
            Bleft.setPower(runPower);
            Bright.setPower(runPower);
            telemetry.update();
            while (Bleft.getCurrentPosition() != LTarget ){       // loop to send rear motor power values to Fleft motor and mimic movement
                Fleft.setPower(Bleft.getPower());
                telemetry.update();                          // update values on display
                
            }
            Fleft.setPower(0);                              // stop motors to end loop sequence
            Fright.setPower(0);
            Bleft.setPower(0);
            Bright.setPower(0);
                    
            // pause before movement sequence 2
            sleep(9000);

            // second movement is attempting to spin to the left
            RTarget = 4000;
            LTarget = 0;
            lift.setTargetPosition(350);
            lift.setPower(.75);
            Fright.setTargetPosition(RTarget);              // set encoded motors and power in RTP mode
            Bleft.setTargetPosition(RTarget);
            Bright.setTargetPosition(LTarget);
            Fright.setPower(runPower);
            Bleft.setPower(runPower);
            Bright.setPower(runPower);
            telemetry.update();
            while (Bleft.getCurrentPosition() != LTarget ){       // loop to send rear motor power values to Fleft motor and mimic movement
                Fleft.setPower(Bleft.getPower());
                telemetry.update();                          // update values on display
            }
            lift.setPower(.3);                          // lower lift to resting postion
            lift.setTargetPosition(0);
            Fleft.setPower(0);                              // stop motors to end loop sequence
            Fright.setPower(0);
            Bleft.setPower(0);
            Bright.setPower(0);
            sleep(3000);
            telemetry.update();
            break;                                       // end opmode when sequence of movements has stopped
        }
    }
}

