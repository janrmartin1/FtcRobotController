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
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

/**
 * This 2022-2023 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine which image is being presented to the robot.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@Disabled
@Autonomous(name = "CustomSleeve", group = "Concept")
public class CustomSleeveV2 extends LinearOpMode {

    /*
     * Specify the source for the Tensor Flow Model.
     * If the TensorFlowLite object model is included in the Robot Controller App as an "asset",
     * the OpMode must to load it using loadModelFromAsset().  However, if a team generated model
     * has been downloaded to the Robot Controller's SD FLASH memory, it must to be loaded using loadModelFromFile()
     * Here we assume it's an Asset.    Also see method initTfod() below .
     */
    private static final String TFOD_MODEL_ASSET = "CustomSleeveV2.tflite";
    //private static final String TFOD_MODEL_FILE  = "C:\\Users\\FTC A\\Desktop\\FtcRobotController\\FtcRobotController\\src\\main\\assets\\CustomSleeveV3.tflite";

    String label = null;
    boolean scanYet = false;
    boolean moveYet = true;

    private static final String[] LABELS = {
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
    private static final String VUFORIA_KEY =
            "AQeqctv/////AAABmcheFpUrvEpYg1bT/7gYJZ05yezUO4K5a8GbBMHpHZsTZJmY1wFdUqsOfNbxQamxzJ" +
                    "OP/uu5xUXtWmz22anWHk63K+of7qzB3t6L6bHGkXQlaDJhxcEnLgLzGH/tstClC6UNOU+oJecuxvgkG+Mc/UNRlwt" +
                    "sQvGh50Ha2o47szXNiF+oTUYjW3Vftd3/yVKrQn6qCvExwJFsiXAG6FixEii31yHl3GP2Z/MFgcH0TREzcN2cdfcLo" +
                    "yIvyJT71xxGVfXzjTXp3uMk6zgr7hCQ93OBm1QngV7u" +
                    "uhAx7BI9V1xv9hEJW3wKq/fVMeIRz6zeMBQk1bMw5hTvW/2fZ0o8PBV5QSRJ6V8Sw8AGMEr8B8AV";

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
        DcMotorSimple Fleft =  hardwareMap.get(DcMotorSimple.class,"Fleft");
        DcMotor Bleft = hardwareMap.dcMotor.get("Bleft");
        DcMotor Fright = hardwareMap.dcMotor.get("Fright");
        DcMotor Bright = hardwareMap.dcMotor.get("Bright");
        DcMotor lift = hardwareMap.dcMotor.get("lift");
        Servo claw = hardwareMap.servo.get("Claw");

        // Reverse the right side motors
        // Reverse left motors if you are using NeveRests
        Fleft.setDirection(DcMotorSimple.Direction.REVERSE);
        Fleft.setDirection(DcMotorSimple.Direction.REVERSE);
        Bright.setDirection(DcMotorSimple.Direction.REVERSE);
        Fright.setDirection(DcMotorSimple.Direction.REVERSE);
        lift.setDirection(DcMotorSimple.Direction.REVERSE);

        Fright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Bleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Bright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setTargetPosition(0);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        claw.setPosition(0);


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
            tfod.setZoom(1.0, 16.0/9.0);
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                telemetry.addData("scanYet", scanYet);
                telemetry.addData("moveYet", moveYet);
                telemetry.addData("Label", label);
                if (tfod != null && scanYet) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Objects Detected", updatedRecognitions.size());

                        // step through the list of recognitions and display image position/size information for each one
                        // Note: "Image number" refers to the randomized image orientation/number
                        for (Recognition recognition : updatedRecognitions) {
                            double col = (recognition.getLeft() + recognition.getRight()) / 2 ;
                            double row = (recognition.getTop()  + recognition.getBottom()) / 2 ;
                            double width  = Math.abs(recognition.getRight() - recognition.getLeft()) ;
                            double height = Math.abs(recognition.getTop()  - recognition.getBottom()) ;

                            telemetry.addData(""," ");
                            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100 );
                            telemetry.addData("- Position (Row/Col)","%.0f / %.0f", row, col);
                            telemetry.addData("- Size (Width/Height)","%.0f / %.0f", width, height);

                            moveYet = false;
                            scanYet = false;

                            if(recognition.getConfidence() >= .7){
                                label = recognition.getLabel();
                            }
                        }

                        telemetry.update();
                    }
                }
                if(label == null && moveYet == true){
                    Fright.setPower(.5);
                    Fleft.setPower(.5);
                    Bright.setPower(.5);
                    Bleft.setPower(.5);
                    sleep(350);
                    Fright.setPower(0);
                    //Fleft.setPower(0);
                    Bright.setPower(0);
                    Bleft.setPower(0);
                    Fleft.setPower(0);
                    sleep(5000);
                    scanYet = true;
                }

                //This is looking to see if the bolt has been detected and if it has it runs the code inside it
                if (label == "black1") {
                    Fright.setPower(.45);
                    Fleft.setPower(-.5);
                    Bright.setPower(-.5);
                    Bleft.setPower(.45);
                    sleep(1600);
                    Fright.setPower(.5);
                    Fleft.setPower(.5);
                    Bright.setPower(.5);
                    Bleft.setPower(.5);
                    sleep(1300);
                    Fright.setPower(.5);
                    Fleft.setPower(-.5);
                    Bright.setPower(.5);
                    Bleft.setPower(-.5);
                    sleep(1000);
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
                else if (label == "green2") {
                    Fright.setPower(.4);
                    Fleft.setPower(.4);
                    Bright.setPower(.4);
                    Bleft.setPower(.4);
                    sleep(2500);
                    Fright.setPower(.5);
                    Fleft.setPower(-.5);
                    Bright.setPower(.5);
                    Bleft.setPower(-.5);
                    sleep(1000);
                    break;
                }
                //This is looking to see if the panel has been detected and if it has it runs the code inside it
                else if (label == "purple3") {
                    Fright.setPower(-.5);
                    Fleft.setPower(.45);
                    Bright.setPower(.45);
                    Bleft.setPower(-.5);
                    sleep(1600);
                    Fright.setPower(.5);
                    Fleft.setPower(.5);
                    Bright.setPower(.5);
                    Bleft.setPower(.5);
                    sleep(1300);
                    Fright.setPower(.5);
                    Fleft.setPower(-.5);
                    Bright.setPower(.5);
                    Bleft.setPower(-.5);
                    sleep(1000);
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
                telemetry.update();
            }
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
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

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
        //tfod.loadModelFromFile(TFOD_MODEL_FILE, LABELS);
    }
}
