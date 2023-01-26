/* Copyright (c) 2019 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

/**
 * This 2022-2023 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine which image is being presented to the robot.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@Disabled
@Autonomous(name = "Forwards Right Parking And Deliver", group = "Concept")
public class ForwardsRightParkingAndDelivery extends LinearOpMode {

    /*
     * Specify the source for the Tensor Flow Model.
     * If the TensorFlowLite object model is included in the Robot Controller App as an "asset",
     * the OpMode must to load it using loadModelFromAsset().  However, if a team generated model
     * has been downloaded to the Robot Controller's SD FLASH memory, it must to be loaded using loadModelFromFile()
     * Here we assume it's an Asset.    Also see method initTfod() below .
     */
    private static final String TFOD_MODEL_ASSET = "PowerPlay.tflite";
    // private static final String TFOD_MODEL_FILE  = "/sdcard/FIRST/tflitemodels/CustomTeamModel.tflite";

    static final double     LIFT_HEIGHT                = (int)40 * 22.388; //22 time
    static final double     MAX_LIFT_HEIGHT         = 1000;;

    private double          liftPower = 0.75;        // declare lift power variable ** 230118 increased from 0.50 for testing **
    private int          liftTarget = 0;         // declare lift target position variable
    private boolean         closed = false;          // declare claw status variable

    String label = null;
    boolean scanYet = false;
    boolean moveYet = true;
    boolean parkYet = false;
    boolean deliverYet = false;
    double i = 1;
    private int previousLiftTarget = liftTarget;

    private static final String[] LABELS = {
            "1 Bolt",
            "2 Bulb",
            "3 Panel"
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
    private static final String VUFORIA_KEY =
            "AQeqctv/////AAABmcheFpUrvEpYg1bT/7gYJZ05yezUO4K5a8GbBMHpHZsTZJmY1wFdUqsOfNbxQamxzJOP/uu5xUXtWmz22anWHk63K+of7qzB3t6L6bHGkXQlaDJhxcEnLgLzGH/tstClC6UNOU+oJecuxvgkG+Mc/UNRlwtsQvGh50Ha2o47szXNiF+oTUYjW3Vftd3/yVKrQn6qCvExwJFsiXAG6FixEii31yHl3GP2Z/MFgcH0TREzcN2cdfcLoyIvyJT71xxGVfXzjTXp3uMk6zgr7hCQ93OBm1QngV7uuhAx7BI9V1xv9hEJW3wKq/fVMeIRz6zeMBQk1bMw5hTvW/2fZ0o8PBV5QSRJ6V8Sw8AGMEr8B8AV";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    @Override
    public void runOpMode() {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();
        initTfod();
        //Here we declare all the motors and servos
        DcMotorSimple Fleft =  hardwareMap.get(DcMotorSimple.class,"Fleft");
        DcMotor Bleft = hardwareMap.dcMotor.get("Bleft");
        DcMotor Fright = hardwareMap.dcMotor.get("Fright");
        DcMotor Bright = hardwareMap.dcMotor.get("Bright");
        DcMotor lift = hardwareMap.dcMotor.get("lift");
        Servo claw = hardwareMap.servo.get("Claw");

        // Reverse the right side motors
        // Reverse left motors if you are using NeveRests
        //Fleft.setDirection(DcMotorSimple.Direction.REVERSE);
        Bleft.setDirection(DcMotorSimple.Direction.REVERSE);
        lift.setDirection(DcMotorSimple.Direction.REVERSE);

        //setting all the modes of the motors and setting starting positions for the lift and claw
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Fright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Bleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Bright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setTargetPosition(0);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        claw.setPosition(1);




        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        //Initializing TensorFlow
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

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();
        //String label = "";
        //if (opModeIsActive()) {
        // Fright.setPower(1);
        //Fleft.setPower(1);
        //Bright.setPower(1);
        //Bleft.setPower(1);

        while (opModeIsActive()) {

            if (tfod != null && scanYet == true) {//Checking if scanYet is true and if it is it will scan the cone
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Objects Detected", updatedRecognitions.size());

                    // step through the list of recognitions and display image position/size information for each one
                    // Note: "Image number" refers to the randomized image orientation/number
                    for (Recognition recognition : updatedRecognitions) {
                        double col = (recognition.getLeft() + recognition.getRight()) / 2;
                        double row = (recognition.getTop() + recognition.getBottom()) / 2;
                        double width = Math.abs(recognition.getRight() - recognition.getLeft());
                        double height = Math.abs(recognition.getTop() - recognition.getBottom());

                        telemetry.addData("", " ");
                        telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
                        telemetry.addData("- Position (Row/Col)", "%.0f / %.0f", row, col);
                        telemetry.addData("- Size (Width/Height)", "%.0f / %.0f", width, height);

                        moveYet = false;
                        scanYet = false;
                        deliverYet = true;

                        if(recognition.getConfidence() >= .7){
                            label = recognition.getLabel();
                            Fright.setPower(1);
                            Fleft.setPower(1);//Moves to the starting position of the loop below
                            Bright.setPower(1);
                            Bleft.setPower(1);
                            sleep(400);
                            Fright.setPower(-1);
                            Fleft.setPower(1);//Turns to face the side stack
                            Bright.setPower(-1);
                            Bleft.setPower(1);
                            sleep(250);
                            Fright.setPower(0);
                            Fleft.setPower(0);//Stops facing the cones
                            Bright.setPower(0);
                            Bleft.setPower(0);
                        }

                    }


                }

            }
            if(label == null && moveYet == true){
                Fright.setPower(1);
                Fleft.setPower(1);//moves forward so it can be close enough to scan the cone
                Bright.setPower(1);
                Bleft.setPower(1);
                sleep(200);
                Fright.setPower(0);
                //Fleft.setPower(0);
                Bright.setPower(0);
                Bleft.setPower(0);
                sleep(100);
                Fleft.setPower(0);
                sleep(5000);
                scanYet = true; //sets scanYet to true so it will scan the cone up above

            }
            else if (deliverYet == true){
                while(i <= 5){//Anything in this loop will only loop 5 times
                    Fright.setPower(-.5);
                    Fleft.setPower(-.5);
                    Bright.setPower(-.5);//This starts facing the side stack and will move forwards towards the side stacks
                    Bleft.setPower(-.5);
                        sleep(300);
                    if(liftTarget == 0){liftTarget = 375;}//This is setting the lift target to the low junction
                    else{liftTarget = previousLiftTarget;}//After delivering the cone it will reset it to the position it picked the last cone up from
                    liftTarget = liftTarget - 25;//This is lowering the lift everytime it goes back for a cone so it will pick up the next cone
                    previousLiftTarget = liftTarget;//previousLiftTarget is saving the position of the cone it just picked up so it will set it back to that and lower it from it to grab the next cone
                    claw.setPosition(0);//This is opening the claw
                        sleep(50);
                    Fright.setPower(0);
                    Fleft.setPower(0);
                    Bright.setPower(0);//This is stopping all the motors
                    Bleft.setPower(0);
                    liftTarget = 375;//Setting the lift target to the lowest junction
                        sleep(200);
                    Fright.setPower(.5);
                    Fleft.setPower(.5);//Moves backwards to the lowest junction
                    Bright.setPower(.5);
                    Bleft.setPower(.5);
                        sleep(300);
                    Fright.setPower(.5);
                    Fleft.setPower(-.5);//Turns towards the lowest junction
                    Bright.setPower(.5);
                    Bleft.setPower(-.5);
                        sleep(150);
                    claw.setPosition(1);//Opens the claw to drop the cone
                        sleep(250);
                    Fright.setPower(-.5);
                    Fleft.setPower(.5);//Turns back to face the stacks
                    Bright.setPower(-.5);
                    Bleft.setPower(.5);
                        sleep(150);
                    i++;//Adds 1 to i so it will add up and when it = 5 it will stop the loop
                    if(i >= 5){
                        parkYet = true;//Will let it park from where it is at
                        deliverYet = false;//Stops the loop so it can't deliver anymore cones
                    }
                }

            }
            if(liftTarget >= lift.getCurrentPosition()){liftPower = 1;}//Sets the speed of the lift whether it is going up or down
            else{liftPower = 0.75;}

            if(lift.getCurrentPosition() != liftTarget && liftTarget < MAX_LIFT_HEIGHT){//Moves the lift up or down by liftTarget
                lift.setTargetPosition(liftTarget);
                lift.setPower(liftPower);
            }

            //This is looking to see if the bolt has been detected and if it has it runs the code inside it
            if (label == "1 Bolt" && parkYet == true) {
                Fright.setPower(.5);
                Fleft.setPower(.5);
                Bright.setPower(.5);//This will move it backwards
                Bleft.setPower(.5);
                sleep(100);
                Fright.setPower(.5);
                Fleft.setPower(-.5);//This will turn it back to it's original spot
                Bright.setPower(.5);
                Bleft.setPower(-.5);
                sleep(375);
                Fright.setPower(-.5);
                Fleft.setPower(-.5);//Drives towards where the scanning cone "was"
                Bright.setPower(-.5);
                Bleft.setPower(-.5);
                sleep(300);
                Fright.setPower(-.5);
                Fleft.setPower(.5);
                Bright.setPower(.5);//Goes it's right "parking position 1"
                Bleft.setPower(-.5);
                sleep(400);
                Fright.setPower(0);
                Fleft.setPower(0);//Stops the motors
                Bright.setPower(0);
                Bleft.setPower(0);
                break;//Breaks the loop and ends the autonomous
                /*Fright.setPower(-1);
                Fleft.setPower(1);
                Bright.setPower(1);
                Bleft.setPower(-1);
                sleep(800);
                Fright.setPower(0);
                Fleft.setPower(0);
                Bright.setPower(0);
                Bleft.setPower(0);
                break;
               /* Fright.setPower(.7);
                Fleft.setPower(.7);
                Bright.setPower(.7);
                Bleft.setPower(.7);
                sleep(1300);
                Fright.setPower(0);
                Bright.setPower(0);
                Bleft.setPower(0);
                sleep(100);
                Fright.setPower(1);
                Fleft.setPower(-1);
                Bright.setPower(-1);
                Bleft.setPower(1);
                sleep(850);
                    /*
                    Fright.setPower(0);
                    Fleft.setPower(1);
                    Bright.setPower(1);
                    Bleft.setPower(0);
                    sleep(500);
                    /*Fright.setPower(1);
                    Fleft.setPower(1);
                    Bright.setPower(1);
                    Bleft.setPower(1);
                    sleep(500);
                Fright.setPower(0);
                Fleft.setPower(0);
                Bright.setPower(0);
                Bleft.setPower(0);
                sleep(9999999);*/
            }
            //This is looking to see if the bulb has been detected and if it has it runs the code inside it
            else if (label == "2 Bulb" && parkYet == true) {
                Fright.setPower(.5);
                Fleft.setPower(.5);
                Bright.setPower(.5);//Backs it up just a little bit
                Bleft.setPower(.5);
                sleep(100);
                Fright.setPower(.5);
                Fleft.setPower(-.5);//turns to face where the scanning cone was
                Bright.setPower(.5);
                Bleft.setPower(-.5);
                sleep(375);
                Fright.setPower(0);
                Fleft.setPower(0);//stops the motors since it is already in parking position 2
                Bright.setPower(0);
                Bleft.setPower(0);
                break;
            }
            //This is looking to see if the panel has been detected and if it has it runs the code inside it
            else if (label == "3 Panel" && parkYet == true) {
                Fright.setPower(.5);
                Fleft.setPower(.5);//Moves it back a little bit
                Bright.setPower(.5);
                Bleft.setPower(.5);
                sleep(100);
                Fright.setPower(.5);
                Fleft.setPower(-.5);//Turns it back to where the scanning cone was
                Bright.setPower(.5);
                Bleft.setPower(-.5);
                sleep(375);
                Fright.setPower(-.5);
                Fleft.setPower(-.5);
                Bright.setPower(-.5);//drives forwards to where the cone was
                Bleft.setPower(-.5);
                sleep(300);
                Fright.setPower(.5);
                Fleft.setPower(-.5);//goes to it's left to park
                Bright.setPower(-.5);
                Bleft.setPower(.5);
                sleep(400);
                Fright.setPower(0);
                Fleft.setPower(0);//stops in parking position 3
                Bright.setPower(0);
                Bleft.setPower(0);
                break;
                /*
                Fright.setPower(-1);
                Fleft.setPower(1);
                Bright.setPower(1);
                Bleft.setPower(-1);
                sleep(800);
                Fright.setPower(0);
                Fleft.setPower(0);
                Bright.setPower(0);
                Bleft.setPower(0);
                break;
                /*Fright.setPower(.7);
                Fleft.setPower(.7);
                Bright.setPower(.7);
                Bleft.setPower(.7);
                sleep(900);
                Fright.setPower(0);
                Bright.setPower(0);
                Bleft.setPower(0);
                sleep(100);
                Fright.setPower(-1);
                Fleft.setPower(1);
                Bright.setPower(1);
                Bleft.setPower(-1);
                sleep(800);
                    /*
                    Fright.setPower(0);
                    Fleft.setPower(1);
                    Bright.setPower(1);
                    Bleft.setPower(0);
                    sleep(500);
                    /*Fright.setPower(1);
                    Fleft.setPower(1);
                    Bright.setPower(1);
                    Bleft.setPower(1);
                    sleep(500);
                Fright.setPower(0);
                Fleft.setPower(0);
                Bright.setPower(0);
                Bleft.setPower(0);
                sleep(9999999);*/
            }
            else {
                Fright.setPower(0);
                Fleft.setPower(0);
                Bleft.setPower(0);
                Bright.setPower(0);}
            telemetry.addData("Parking?", parkYet);
            telemetry.addData("Deliver?", deliverYet);
            telemetry.addData("Scan?", scanYet);
            telemetry.addData("Move?", moveYet);
            telemetry.addData("Lift Position", liftTarget);
            telemetry.update();
        }
    }



    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.75f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 300;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);

        // Use loadModelFromAsset() if the TF Model is built in as an asset by Android Studio
        // Use loadModelFromFile() if you have downloaded a custom team model to the Robot Controller's FLASH.
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
        // tfod.loadModelFromFile(TFOD_MODEL_FILE, LABELS);
    }
}
