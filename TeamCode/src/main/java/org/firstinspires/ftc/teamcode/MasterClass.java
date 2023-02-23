package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;


public class MasterClass {
   public DcMotorSimple FrontLeft;
   public DcMotor FrontRight;
   public DcMotor BackLeft;
   public DcMotor BackRight;
   public DcMotor Lift;
   public Servo Claw;
   public enum DriveDirection{
      LEFT,
      RIGHT,
      FORWARD,
      BACKWARD
   };

   public static final String TFOD_MODEL_ASSET = "CustomSleeveV2.tflite";
   //private static final String TFOD_MODEL_FILE  = "C:\\Users\\FTC A\\Desktop\\FtcRobotController\\FtcRobotController\\src\\main\\assets\\CustomSleeveV1.tflite";

   public String label = null;

   public boolean scanned = false;

   final String[] LABELS = {
           "black1",
           "green2",
           "purple3"
   };

   /*
    * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
    * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
    * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
    * web site at https://developer.vuforia.com/license-manager.
    *
    * Vuforia license keys are always 380 characters long, and look as if they contain mostly
    * random data. As an example, here is a example of a fragment of a valid key:
    *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
    * Once you've obtained a license key, copy the string from the Vuforia web site
    * and paste it in to your code on the next line, between the double quotes.
    */
   public static final String VUFORIA_KEY =
           "AQeqctv/////AAABmcheFpUrvEpYg1bT/7gYJZ05yezUO4K5a8GbBMHpHZsTZJmY1wFdUqsOfNbxQamxzJ" +
                   "OP/uu5xUXtWmz22anWHk63K+of7qzB3t6L6bHGkXQlaDJhxcEnLgLzGH/tstClC6UNOU+oJecuxvgkG+Mc/UNRlwt" +
                   "sQvGh50Ha2o47szXNiF+oTUYjW3Vftd3/yVKrQn6qCvExwJFsiXAG6FixEii31yHl3GP2Z/MFgcH0TREzcN2cdfcLo" +
                   "yIvyJT71xxGVfXzjTXp3uMk6zgr7hCQ93OBm1QngV7u" +
                   "uhAx7BI9V1xv9hEJW3wKq/fVMeIRz6zeMBQk1bMw5hTvW/2fZ0o8PBV5QSRJ6V8Sw8AGMEr8B8AV";


   private VuforiaLocalizer vuforia;

   private TFObjectDetector tfod;

   // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
   // first.





   public void Init(HardwareMap map){
      FrontLeft = map.get(DcMotorSimple.class, "Fleft");
      FrontRight = map.get(DcMotor.class,"Fright");
      BackLeft = map.get(DcMotor.class,"Bleft");
      BackRight = map.get(DcMotor.class,"Bright");
      Lift = map.get(DcMotor.class, "lift");
      Claw = map.get(Servo.class,"Claw");

      initVuforia(map);
      initTfod(map);



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
      switch(pos) {
         case "High":
            Lift.setTargetPosition(1080);

         case "Medium":
            Lift.setTargetPosition(833);

         case "Low":
            Lift.setTargetPosition(490);

         case "Ground":
            Lift.setTargetPosition(133);

         case "Reset":
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
         default: POWER = 0; telemetry.addData(".", "You entered a wrong direction into the Turn method");
      }


      FrontLeft.setPower(POWER);
      FrontRight.setPower(-POWER);
      BackLeft.setPower(POWER);
      BackRight.setPower(-POWER);


   }

   public void Scan() {

      /**
       * Activate TensorFlow Object Detection before we wait for the start command.
       * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
       **/
      if (tfod != null) {
         tfod.activate();

         // The TensorFlow software will scale the input images from the camera to a lower resolution.
         // This can result in lower detection accuracy at longer distances (> 55cm or 22").
         // If your target is at distance greater than 50 cm (20") you can increase the magnification value
         // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
         // should be set to the value of the images used to create the TensorFlow Object Detection model
         // (typically 16/9).
         tfod.setZoom(1.0, 16.0 / 9.0);
      }

      if (tfod != null) {
         // getUpdatedRecognitions() will return null if no new information is available since
         // the last time that call was made.
         List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
         if (updatedRecognitions != null) {

            // step through the list of recognitions and display image position/size information for each one
            // Note: "Image number" refers to the randomized image orientation/number
            for (Recognition recognition : updatedRecognitions) {
               double col = (recognition.getLeft() + recognition.getRight()) / 2;
               double row = (recognition.getTop() + recognition.getBottom()) / 2;
               double width = Math.abs(recognition.getRight() - recognition.getLeft());
               double height = Math.abs(recognition.getTop() - recognition.getBottom());


               if (recognition.getConfidence() >= .7) {
                  label = recognition.getLabel();
                  scanned = true;
               }
            }

         }
      }
   }
      private void initVuforia(HardwareMap map) {
         /*
          * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
          */
         VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

         parameters.vuforiaLicenseKey = VUFORIA_KEY;
         parameters.cameraName = map.get(WebcamName.class, "Webcam 1");

         //  Instantiate the Vuforia engine
         vuforia = ClassFactory.getInstance().createVuforia(parameters);
      }

      /**
       * Initialize the TensorFlow Object Detection engine.
       */
      private void initTfod(HardwareMap map) {
         int tfodMonitorViewId = map.appContext.getResources().getIdentifier(
                 "tfodMonitorViewId", "id", map.appContext.getPackageName());
         TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
         tfodParameters.minResultConfidence = 0.75f;
         tfodParameters.isModelTensorFlow2 = true;
         tfodParameters.inputSize = 300;
         tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);

         // Use loadModelFromAsset() if the TF Model is built in as an asset by Android Studio
         // Use loadModelFromFile() if you have downloaded a custom team model to the Robot Controller's FLASH.
         tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
         //tfod.loadModelFromFile(TFOD_MODEL_FILE, LABELS);
      }
   }
