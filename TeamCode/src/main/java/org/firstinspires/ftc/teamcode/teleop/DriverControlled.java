package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robotHardware;

@TeleOp(name = "driverControlled", group = "teleOp")
public class DriverControlled extends LinearOpMode {

    // Declare OpMode Members
    private robotHardware robot = new robotHardware();

    private boolean intaking = false;
    private boolean outaking = false;

    public void runOpMode() throws InterruptedException {
        // init
        robot.init(hardwareMap);

        telemetry.addLine("Initialized");
        // init loop
        while (!opModeIsActive()) {

        }

        waitForStart();

        // loop
        while (!isStopRequested()) {

            // Intake Controls
            boolean intakeButton = gamepad1.a;
            boolean outakeButton = gamepad1.b;

            // Stacker Controls
            double stackerUp = gamepad2.right_trigger;
            double stackerDown = -gamepad2.left_trigger;
            double stackerPower = stackerUp + stackerDown;

            // Other Controls
            boolean carouselButtonBlue = gamepad1.x;
            boolean carouselButtonRed = gamepad1.y;

            double y = gamepad1.left_stick_x * 1.1; // Remember, this is reversed!
            double x = gamepad1.right_trigger-gamepad1.left_trigger; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x - rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x + rx) / denominator;

            robot.leftFront.setPower(frontLeftPower);
            robot.leftRear.setPower(backLeftPower);
            robot.rightFront.setPower(frontRightPower);
            robot.rightRear.setPower(backRightPower);

            // Intake
            if (intakeButton && intaking) {
                robot.topIntake.setPower(0);
                robot.bottomIntake.setPower(0);
                intaking = false;
                outaking = false;
            } else if (intakeButton) {
                robot.topIntake.setPower(1);
                robot.bottomIntake.setPower(-1);
                intaking = true;
                outaking = false;
            }

            // Outake
            if (outakeButton && outaking) {
                robot.topIntake.setPower(0);
                robot.bottomIntake.setPower(0);
                outaking = false;
                intaking = false;
            } else if (outakeButton) {
                robot.topIntake.setPower(-1);
                robot.bottomIntake.setPower(1);
                outaking = true;
                intaking = false;
            }

            // Stacker
            if (!robot.stackerLimit.isPressed()) {
                robot.stackerMotor.setPower(stackerPower);
            } else if (robot.stackerLimit.isPressed()) {
                robot.stackerMotor.setPower(stackerUp);
            }

            // Other
            if(carouselButtonBlue) {
                robot.carouselMotor.setPower(1);
            } else if (carouselButtonRed) {
                robot.carouselMotor.setPower(-1);
            } else {
                robot.carouselMotor.setPower(0);
            }

            // Telemetry
            telemetry.addData("stackerLimit", robot.stackerLimit.isPressed());
            telemetry.addData("frontLeftPower", frontLeftPower);
            telemetry.addData("frontRightPower", frontRightPower);
            telemetry.addData("backLeftPower", backLeftPower);
            telemetry.addData("backRightPower", backRightPower);
            telemetry.addData("intaking", intaking);
            telemetry.addData("outaking", outaking);
            telemetry.update();
        }
    }
}
