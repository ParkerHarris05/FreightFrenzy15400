package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.robotHardware;

@Autonomous(name = "testAuto", group = "Auto")
public class testAuto extends LinearOpMode {

    // Declare OpMode Members
    private robotHardware robot = new robotHardware();
    private boolean isRed = true;

    public double imuX = 42069;
    public double imuY = 6969;

    private double velocityX = 0;
    private double velocityY = 0;

    private double deltaX = 0;
    private double deltaY = 0;

    private ElapsedTime timeX = new ElapsedTime();
    private ElapsedTime timeY = new ElapsedTime();

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

            Pose2d poseEstimate = drive.getPoseEstimate();

            Acceleration accel = robot.imu.imu.getLinearAcceleration();

            // X
            double xAccel = accel.xAccel;
            deltaX = (velocityX * timeX.seconds()) + (1/2 * xAccel * Math.pow(timeX.seconds(), 2));
            imuX = imuX + toInches(deltaX);
            velocityX = deltaX/timeX.seconds();
            timeX.reset();

            // Y
            double yAccel = accel.yAccel;
            deltaY = (velocityY * timeY.seconds()) + (1/2 * yAccel * Math.pow(timeY.seconds(), 2));
            imuY = imuY + toInches(deltaY);
            velocityY = deltaY/timeY.seconds();
            timeY.reset();

            // Telemetry
            telemetry.addData("imuX", imuX);
            telemetry.addData("imuY", imuY);
            telemetry.addData("imuHeading", robot.imu.GetHeading());
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());
            telemetry.update();
        }
    }
    private double toInches(double meters) {
        return meters*39.37;
    }
}
