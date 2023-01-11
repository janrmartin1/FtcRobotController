package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class LiftTest extends LinearOpMode {
    @Override
    public void runOpMode(){
        DcMotor lift = hardwareMap.dcMotor.get("lift");
        lift.setDirection(DcMotorSimple.Direction.REVERSE);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();
        while(opModeIsActive()){
            if(gamepad1.a){
                lift.setPower(.7);
            }
            else if(gamepad1.b){
                lift.setPower(-.2);
            }
            else{lift.setPower(0);}
            telemetry.addData("Lift Power", lift.getPower());
            telemetry.update();
        }

    }
}
