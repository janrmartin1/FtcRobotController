 package org.firstinspires.ftc.teamcode;

    import com.qualcomm.robotcore.eventloop.opmode.Disabled;
    import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
    import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
    import com.qualcomm.robotcore.hardware.DcMotor;
    import com.qualcomm.robotcore.hardware.DcMotorSimple;
    import com.qualcomm.robotcore.hardware.Servo;
    import com.qualcomm.robotcore.util.ElapsedTime;

    import org.firstinspires.ftc.robotcore.external.Telemetry;
    @Disabled
    @TeleOp(name = "TEST DRIVE W/tele")
    public class DriveWTele extends LinearOpMode {
        // variables
        static final double     COUNTS_PER_MOTOR_REV    = 288 ;    // eg: TETRIX Motor Encoder
        static final double     DRIVE_GEAR_REDUCTION    = 1 ;     // This is < 1.0 if geared UP
        static final double     WHEEL_DIAMETER_INCHES   = .5 ;     // For figuring circumference
        static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
        static final double     LIFT_HEIGHT                = (int)40 * 22.388; //22 time
        static final double     MAX_LIFT_HEIGHT         = 1000;;
        private ElapsedTime     runtime = new ElapsedTime();    // sets up a timer function
        private ElapsedTime     runtime2 = new ElapsedTime();    // sets up a timer function
        private double          frontLeftPower = 0;     // declare motor power variable
        private double          backLeftPower = 0;      // declare motor power variable
        private double          frontRightPower = 0;    // declare motor power variable
        private double          backRightPower = 0;     // declare motor power variable
        private double          denominator = 1;        // declare motor power calculation variable
        private int             precision = 2;          // chassis motor power reduction factor 1 = full 2 = half power 3 = third power 4 = quarter power
                                                        // ** 230118 set default speed to half power
        private double          liftPower = 0.75;        // declare lift power variable ** 230118 increased from 0.50 for testing **
        private int             liftTarget = 0;         // declare lift target position variable
        private boolean         closed = false;          // declare claw status variable
        
        @Override
        public void runOpMode() throws InterruptedException {
            // Declare our motors
            // Make sure your ID's match your configuration

            DcMotorSimple Fleft =  hardwareMap.get(DcMotorSimple.class,"Fleft");
            DcMotor Bleft = hardwareMap.dcMotor.get("Bleft");
            DcMotor Fright = hardwareMap.dcMotor.get("Fright");
            DcMotor Bright = hardwareMap.dcMotor.get("Bright");
            DcMotor lift = hardwareMap.dcMotor.get("lift");
            Servo Claw = hardwareMap.servo.get("Claw");

            // Reverse the right side motors
            // Reverse left motors if you are using NeveRests

            Fleft.setDirection(DcMotorSimple.Direction.REVERSE);
            Fright.setDirection(DcMotorSimple.Direction.REVERSE);
            Bright.setDirection(DcMotorSimple.Direction.REVERSE);
            lift.setDirection(DcMotorSimple.Direction.REVERSE);
            Bright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            Bleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            
            //Fleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);  // motor controller has a physical switch
            //Fleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            Fright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            Bright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            lift.setTargetPosition(0);
            lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            Claw.setPosition(1);

            waitForStart();

            //if (isStopRequested()) return;

            while (opModeIsActive()) {

                // check for driving input

                double y = gamepad1.left_stick_y; // Remember, this is reversed!
                double x = -gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
                double rx = -gamepad1.right_stick_x;
                
                // check for lift input

                if (gamepad2.dpad_up && runtime.seconds() > 1.0) {
                    //Set the lift to the top position
                    liftTarget = 830; //lift.getCurrentPosition() + (int)(LIFT_HEIGHT * COUNTS_PER_INCH) / 20;   ** 230118 lowered from 850 **
                    runtime.reset();  //resets runtime delay timer
                } else if (gamepad2.dpad_left && runtime.seconds() > 1.0) {
                    //Set the lift to middle junction height
                    liftTarget = 580;   // ** 230118 lowered from 600 **
                    runtime.reset();  //resets runtime delay timer
                } else if (gamepad2.dpad_right && runtime.seconds() > 1.0) {
                    //Set the lift to the lowest junction height
                    liftTarget = 370; //26.32
                    runtime.reset();  //resets runtime delay timer
                } else if (gamepad2.dpad_down && runtime.seconds() > 1.0) {
                    //Set the lift to ground position
                    liftTarget = 0;
                    runtime.reset();  //resets runtime delay timer
                } //lse if

                // check for lift height fine tuning and manual up or down 

                if( (gamepad2.right_bumper) && (liftTarget + 10) < MAX_LIFT_HEIGHT){     // 230118 set increment to a lower value to test
                    liftTarget = liftTarget + 1;    // push to raise the lift by small increments above preset positions
                }
                if(gamepad2.x && !gamepad2.b && lift.getCurrentPosition() < 600){
                    liftTarget = liftTarget + 1;    // constant increment of liftTarget while holding x
                }
                if(gamepad2.b && !gamepad2.x && lift.getCurrentPosition() < 620 && liftTarget >= 1){
                    liftTarget = liftTarget - 1;    // constant increment of liftTarget while holding x
                }
                // adjust lift height if within safe operating range

                if(liftTarget > lift.getCurrentPosition()){
                    liftPower = 1;
                }      
                else {liftPower = 0.75;}

                if(!(liftTarget > MAX_LIFT_HEIGHT)) {
                    lift.setTargetPosition(liftTarget);
                    lift.setPower(liftPower);

                    if(lift.isBusy()) {
                        telemetry.addData("lift", "Running at %7d",
                                lift.getCurrentPosition());
                    }
                }

                // check for claw activation input

                if (gamepad2.a && closed && runtime2.seconds() > 0.25) {
                    Claw.setPosition(1);
                    closed = false;
                    runtime2.reset();  //resets runtime2 delay timer
                } else if (gamepad2.a && !closed && runtime2.seconds() > 0.25) {
                    Claw.setPosition(0);
                    closed = true;
                    runtime2.reset();  //resets runtime2 delay timer
                }

                // telemetry.addData("Position", closed);
                telemetry.addData("Lift Power", liftPower);
                telemetry.addData("Servo Position", Claw.getPosition());
                //telemetry.addline()
                telemetry.addData("BR Enc value",Bright.getCurrentPosition()) ;
                telemetry.addData("BL Enc value",Bleft.getCurrentPosition());
 
                telemetry.update();

                // calculate motor movement math and adjust according to lift height or manual precision mode selection

                denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
                precision = 2;            // reset default speed to half power
                if (lift.getCurrentPosition() > 450 || gamepad1.right_bumper) {   // check for manual or force precision driving mode
                    precision = 4;       // sets speed to one quarter power
                }
                if (lift.getCurrentPosition() < 451 && gamepad1.left_bumper) {  // check for turbo mode when available at lower lift height
                    precision = 1;       // sets speed to full power "TURBO" mode
                }
                denominator = denominator * precision;
                frontLeftPower = (y + x + rx) / denominator;
                backLeftPower = (y - x + rx) / denominator;
                frontRightPower = (y - x - rx) / denominator;
                backRightPower = (y + x - rx) / denominator;

                // issue chassis power for movement

                Fleft.setPower(frontLeftPower);
                Bleft.setPower(backLeftPower);
                Fright.setPower(frontRightPower);
                Bright.setPower(backRightPower);
                
            }

        }
    }



