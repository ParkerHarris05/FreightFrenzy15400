package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.robotHardware;

@TeleOp(group = "teleOp")
public class DriverControled extends LinearOpMode {

    // Declare OpMode Members
    private robotHardware robot = new robotHardware();

    public void runOpMode() throws InterruptedException {
        // init
        robot.init(hardwareMap);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

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
            double stackerUp = gamepad2.right_trigger;

            // Stacker Controls
            double stackerDown = -gamepad2.left_trigger;
            double stackerPower = stackerUp + stackerDown;
            boolean releaseElement = gamepad2.a;
            boolean secureElement = gamepad2.x;
            boolean unsecureElement = gamepad2.y;
            boolean elementSecured = false;

            // Other Controls
            boolean carouselButton = gamepad1.x;

            drive.setWeightedDrivePower(
                    new Pose2d(
                            -gamepad1.left_stick_y,
                            -gamepad1.left_stick_x,
                            -gamepad1.right_stick_x
                    )
            );
            drive.update();
            Pose2d poseEstimate = drive.getPoseEstimate();

            // Intake
            if (intakeButton) {
                robot.topIntake.setPower(1);
                robot.bottomIntake.setPower(-1);
            } else {
                robot.topIntake.setPower(0);
                robot.bottomIntake.setPower(0);
            }

            // Outake
            if (outakeButton) {
                robot.topIntake.setPower(-1);
                robot.bottomIntake.setPower(1);
            } else {
                robot.topIntake.setPower(0);
                robot.bottomIntake.setPower(0);
            }

            // Stacker
            robot.stackerMotor.setPower(stackerPower);
            if (releaseElement) {
                robot.stackerFront.setPosition(1);
            } else {
                robot.stackerFront.setPosition(0);
            }
            if (secureElement) {
                elementSecured = true;
            } else if(unsecureElement) {
                elementSecured = false;
            }
            if (elementSecured) {
                robot.stackerRear.setPosition(0);
            } else {
                robot.stackerRear.setPosition(1);
            }

            // Other
            if(carouselButton) {
                robot.carouselMotor.setPower(1);
            } else {
                robot.carouselMotor.setPower(0);
            }

            // Telemetry
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());
            telemetry.update();
        }
    }
}
