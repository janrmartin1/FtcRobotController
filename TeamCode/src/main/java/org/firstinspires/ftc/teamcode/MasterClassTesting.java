package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;
@Autonomous
public class MasterClassTesting extends LinearOpMode {

        MasterClass Robot = new MasterClass();

        public void runOpMode() {
            // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
            // first.
            Robot.Init(hardwareMap);

            /**
             * Activate TensorFlow Object Detection before we wait for the start command.
             * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
             **/


            /** Wait for the game to begin */
            telemetry.addData(">", "Press Play to start op mode");
            telemetry.update();
            waitForStart();

            if (opModeIsActive()) {
                while (opModeIsActive()) {
                    Robot.MoveLift("High", true);
                    sleep(2000);
                    Robot.MoveLift("Reset", false);
                    sleep(2000);
                    Robot.MoveLift("Medium", true);
                    sleep(2000);
                    Robot.MoveLift("Reset", false);
                    sleep(2000);
                    Robot.MoveLift("Low", true);
                    sleep(3000);
                    Robot.MoveLift("Reset", false);
                    sleep(2000);
                    Robot.MoveLift("Ground", true);
                    sleep(2000);
                    Robot.MoveLift("Reset", false);
                    sleep(2000);
                    Robot.Drive("Forward", 2);
                    sleep(3000);
                    Robot.Drive("Backwards", 2);
                    sleep(3000);
                    Robot.Strafe("Left", 1);
                    sleep(2000);
                    Robot.Strafe("Right", 1);
                    break;

                }
            }
        }
}
