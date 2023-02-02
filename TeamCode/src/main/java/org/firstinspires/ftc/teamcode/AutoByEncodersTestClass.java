package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
public class AutoByEncodersTestClass extends LinearOpMode {

    int RTarget = 0;
    int LTarget = 0;
    double runPower = 0;
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
        Bleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Bright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setTargetPosition(0);
        Bleft.setTargetPosition(0);
        Bright.setTargetPosition(0);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
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
            runPower = .3;  
            Bleft.setTargetPosition(RTarget);              // set back motors and power in RTP mode
            Bright.setTargetPosition(LTarget);
            Bleft.setPower(runPower);
            Bright.setPower(runPower);
            while( Bleft.getCurrentPosition() < LTarget ){       // loop to send rear motor power values to front motors and mimic movement
                Fleft.setPower(Bleft.getPower());
                telemetry.update();
                if (Bright.getCurrentPosition() < RTarget ){
                    Fright.setPower(Bright.getPower());
                    telemetry.update();                 // update values on display
                } else {
                    Fleft.setPower(0);
                    Fright.setPower(0);
                    Bleft.setPower(0);
                    Bright.setPower(0);
                    break;                              // stop motors and end loop when movement has stopped
                }
            }
            // pause before movement sequence 2
            sleep(9000);

            // second movement is attempting to spin to the right
            RTarget = 4000;
            LTarget = 0;
            lift.setTargetPosition(350);
            lift.setPower(.75);
            Bleft.setTargetPosition(RTarget);              // set new target positions and reduce motor power
            Bright.setTargetPosition(LTarget);
            Bleft.setPower(runPower);
            Bright.setPower(runPower);
            while( Bleft.getCurrentPosition() < LTarget ){       // loop to send rear motor power values to front motors and mimic movement
                Fleft.setPower(Bleft.getPower());
                if (Bright.getCurrentPosition() < RTarget ){
                    Fright.setPower(Bright.getPower());
                    telemetry.update();                 // update values on display
                } else {
                    Fleft.setPower(0);
                    Fright.setPower(0);
                    Bleft.setPower(0);
                    Bright.setPower(0);
                    break;                              //  stop motors and end loop when movement has stopped
                }
            }
            lift.setPower(.3);                          // lower lift to resting postion
            lift.setTargetPosition(0);
            sleep(3000);
            telemetry.update();
            break;                                       // end opmode when sequence of movements has stopped
        }
    }
}

