    package org.firstinspires.ftc.teamcode;

    import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
    import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
    import com.qualcomm.robotcore.hardware.DcMotor;
    import com.qualcomm.robotcore.hardware.DcMotorSimple;
    import com.qualcomm.robotcore.hardware.Servo;
    import com.qualcomm.robotcore.util.ElapsedTime;

    @TeleOp(name = "ONLY DRIVE")
    public class OnlyDrive extends LinearOpMode {
        // variables
        static final double     COUNTS_PER_MOTOR_REV    = 288 ;    // eg: TETRIX Motor Encoder
        static final double     DRIVE_GEAR_REDUCTION    = 1 ;     // This is < 1.0 if geared UP
        static final double     WHEEL_DIAMETER_INCHES   = .5 ;     // For figuring circumference
        static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
        private ElapsedTime     runtime = new ElapsedTime();    // sets up a timer function
        private ElapsedTime     runtime2 = new ElapsedTime();    // sets up a timer function
        private double          frontLeftPower = 0;     // declare motor power variable
        private double          backLeftPower = 0;      // declare motor power variable
        private double          frontRightPower = 0;    // declare motor power variable
        private double          backRightPower = 0;     // declare motor power variable
        private double          denominator = 1;        // declare motor power calculation variable
        private int             precision = 2;          // chassis motor power reduction factor 1 = full 2 = half power 3 = third power 4 = quarter power
                                                        // ** 230118 set default speed to half power


        @Override
        public void runOpMode() throws InterruptedException {
            // Declare our motors
            // Make sure your ID's match your configuration

            DcMotorSimple Fleft =  hardwareMap.get(DcMotorSimple.class,"Fleft");
            DcMotor Bleft = hardwareMap.dcMotor.get("Bleft");
            DcMotor Fright = hardwareMap.dcMotor.get("Fright");
            DcMotor Bright = hardwareMap.dcMotor.get("Bright");

            // Reverse the right side motors
            // Reverse left motors if you are using NeveRests

            Fleft.setDirection(DcMotorSimple.Direction.REVERSE);
            Fright.setDirection(DcMotorSimple.Direction.REVERSE);
            Bright.setDirection(DcMotorSimple.Direction.REVERSE);
            
            //Fleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);  // motor controller has a physical switch
            Fright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            Bleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            Bright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            waitForStart();

            //if (isStopRequested()) return;

            while (opModeIsActive()) {

                // check for driving input

                double y = gamepad1.left_stick_y; // Remember, this is reversed!
                double x = -gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
                double rx = -gamepad1.right_stick_x;

                telemetry.update();

                // calculate motor movement math and adjust according to lift height or manual precision mode selection

                denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
                precision = 2;            // reset default speed to half power

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



