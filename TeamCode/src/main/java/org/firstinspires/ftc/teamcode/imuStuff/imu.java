package org.firstinspires.ftc.teamcode.imuStuff;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.*;

public class imu {

    public BNO055IMU imu;

    public double imuX = 0;
    public double imuY = 0;

    private double velocityX = 0;
    private double velocityY = 0;

    private double deltaX = 0;
    private double deltaY = 0;

    private ElapsedTime time = new ElapsedTime();

    public void initIMU(HardwareMap hardwareMap) {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.loggingEnabled = true;
        parameters.loggingTag     = "IMU";
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
    }
    public double GetHeading() {
        Orientation orient = imu.getAngularOrientation();
        return orient.firstAngle;
    }
    public void updateX() {
        //formula: delta X = velocity * time + 1/2 * acceleration * time^2
        Acceleration accel = imu.getLinearAcceleration();
        double xAccel = accel.xAccel;
        deltaX = (velocityX * time.seconds()) + (1/2 * xAccel * Math.pow(time.seconds(), 2));
        imuX = imuX + toInches(deltaX);
        velocityX = deltaX/time.seconds();
        time.reset();
    }
    public void updateY() {
        Acceleration accel = imu.getLinearAcceleration();
        double yAccel = accel.yAccel;
        deltaY = (velocityY * time.seconds()) + (1/2 * yAccel * Math.pow(time.seconds(), 2));
        imuY = imuY + toInches(deltaY);
        velocityY = deltaY/time.seconds();
        time.reset();
    }
    private double toInches(double meters) {
        return meters*39.37;
    }
}
