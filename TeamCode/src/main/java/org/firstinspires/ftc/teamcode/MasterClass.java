package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class MasterClass {
   public DcMotorSimple FrontLeft;
   public DcMotor FrontRight;
   public DcMotor BackLeft;
   public DcMotor BackRight;
   public DcMotor Lift;
   public Servo Claw;


   public void Init(HardwareMap map){
      FrontLeft = map.get(DcMotorSimple.class, "Fleft");
      FrontRight = map.get(DcMotor.class,"Fright");
      BackLeft = map.get(DcMotor.class,"Bleft");
      BackRight = map.get(DcMotor.class,"Bright");
      Lift = map.get(DcMotor.class, "lift");
      Claw = map.get(Servo.class,"Claw");

      BackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
      Lift.setDirection(DcMotorSimple.Direction.REVERSE);

      Lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

      BackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
      FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
      BackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
      Lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
      Lift.setTargetPosition(0);
      Lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      Claw.setPosition(0);
   }

   public void Drive(String dir){
      if(dir == "Forward"){
         FrontLeft.setPower(.5);
         FrontRight.setPower(.5);
         BackLeft.setPower(.5);
         BackRight.setPower(.5);}
      else if(dir == "Backwards"){
         FrontLeft.setPower(-.5);
         FrontRight.setPower(-.5);
         BackLeft.setPower(-.5);
         BackRight.setPower(-.5);
      }
   }
   public void StopDrive(){
      FrontLeft.setPower(0);
      FrontRight.setPower(0);
      BackLeft.setPower(0);
      BackRight.setPower(0);
   }

   public void MoveLift(String pos){
      if(pos == "High"){
         Lift.setTargetPosition(1600);
      }
      else if(pos == "Medium"){
         Lift.setTargetPosition(1200);
      }
      else if(pos == "Low"){
         Lift.setTargetPosition(650);
      }
      else if(pos == "Ground"){
         Lift.setTargetPosition(200);
      }
      else if(pos == "Reset"){
         Lift.setTargetPosition(0);
      }
      if(Lift.getCurrentPosition() > Lift.getTargetPosition()){
         Lift.setPower(.5);
      }
      else {
         Lift.setPower(1);
      }
   }

   public void Strafe(String dir){
      double POWER = 0;
      switch(dir){
         case "Left": POWER = .5; break;
         case "Right": POWER = -.5; break;
         default: telemetry.addData(".", "You entered a wrong direction into the Strafe method");
         POWER = 0;
      }
      telemetry.addData("Fleft Power", FrontLeft.getPower());

      FrontLeft.setPower(POWER);
      FrontRight.setPower(-POWER);
      BackLeft.setPower(-POWER);
      BackRight.setPower(POWER);

      telemetry.update();
   }

   public void Turn(String dir){
      double POWER = 0;
      switch (dir){
         case "CW": POWER = .5; break;
         case "CCW": POWER = -.5; break;
         default: POWER = 0; telemetry.addData(".", "Youd entered a wrong direction into the Turn method");
      }
      telemetry.addData("Fleft Power", FrontLeft.getPower());

      FrontLeft.setPower(POWER);
      FrontRight.setPower(-POWER);
      BackLeft.setPower(POWER);
      BackRight.setPower(-POWER);

      telemetry.update();
   }
}
