package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.HardwareRobot;

import java.util.Objects;

import static org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity.TeamColor;
import static org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity.timeDelay;


@Autonomous(name = "Simple Auto", group = "Linear OpModes")
public class Simple_Auto extends LinearOpMode {

    private enum Alliance {
        RED, BLUE, NONE
    }

    private enum Direction {
        LEFT, RIGHT, NONE
    }

    private Alliance Color = Alliance.NONE;

    private HardwareRobot robot = new HardwareRobot();
    private ElapsedTime runtime = new ElapsedTime();

    private static final double COUNTS_PER_MOTOR_REV = 1120;

    private static final double DRIVE_GEAR_REDUCTION = 1.0;
    private static final double WHEEL_DIAMETER_INCHES = 4.0;
    private static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * (Math.PI));

    private static final double rtTwo = Math.sqrt(2);

    @Override
    public void runOpMode() throws InterruptedException {

        if (Objects.equals(TeamColor, "Blue")) {

            Color = Alliance.BLUE;

        } else if (Objects.equals(TeamColor, "Red")) {

            Color = Alliance.RED;

        }

        robot.init(hardwareMap);

        telemetry.addData("Status", "Initializing");
        telemetry.addData("Notice", "Do NOT click START!");
        telemetry.update();

        robot.leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.beltMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.beltMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        runtime.reset();

        while (runtime.seconds() <= 3) {
            idle();
        }

        telemetry.addData("Status: ", "READY");
        telemetry.addData("Start", "OK!");
        telemetry.update();

        waitForStart();

        runtime.reset();

        while (runtime.seconds() < timeDelay) {
            idle();
        }

        telemetry.addData("Status: ", "Running");
        telemetry.update();

        Move(0.4, 20, 3, 1);

        Launch(2);

        Move(0.4, 5, 3, 1);

        telemetry.addData("Status: ", "Complete");
        telemetry.update();


    }

    private void Move(double speed, double distance, double time, double pause) throws InterruptedException {
        int target;

        target = (int) (distance * COUNTS_PER_INCH);

        robot.leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.leftDrive.setTargetPosition(target);
        robot.rightDrive.setTargetPosition(target);

        runtime.reset();
        robot.leftDrive.setPower(speed);
        robot.rightDrive.setPower(speed);

        while (opModeIsActive() && (runtime.seconds() < time) && (robot.leftDrive.isBusy() && robot.rightDrive.isBusy())) {

            telemetry.addData("Move Action", "Running at %7d :%7d",
                    robot.leftDrive.getCurrentPosition(),
                    robot.rightDrive.getCurrentPosition());
            telemetry.update();

            idle();
        }

        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);

        robot.leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        sleep((int) (1000 * pause));
    }

    /*private void MoveToRange(double speed, double waitTime, double distance, double pause) throws InterruptedException {

        robot.leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        runtime.reset();
        robot.leftDrive.setPower(speed);
        robot.rightDrive.setPower(speed);

        while (robot.range.getDistance(DistanceUnit.CM) >= distance) {

            telemetry.addData("Distance", robot.range.getDistance(DistanceUnit.CM));
            telemetry.update();
            idle();

        }

        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);

        robot.leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        sleep((int) (1000 * pause));
    }

    private void Turn(double speed, double angle, Direction dir, double time, double pause) throws InterruptedException {
        int target;

        target = (int) (angle * COUNTS_PER_INCH);

        robot.leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if (Color == Alliance.BLUE) {
            switch (dir) {

                case LEFT:
                    dir = Direction.RIGHT;
                    break;

                case RIGHT:
                    dir = Direction.LEFT;
                    break;
            }
        }

        switch (dir) {

            case LEFT:
                robot.leftDrive.setTargetPosition(-target);
                robot.rightDrive.setTargetPosition(target);
                break;

            case RIGHT:
                robot.leftDrive.setTargetPosition(target);
                robot.rightDrive.setTargetPosition(-target);
                break;

        }

        runtime.reset();
        robot.leftDrive.setPower(speed);
        robot.rightDrive.setPower(speed);

        while (opModeIsActive() && (runtime.seconds() < time) && (robot.leftDrive.isBusy() && robot.rightDrive.isBusy())) {

            telemetry.addData("Move Action", "Running at %7d :%7d",
                    robot.leftDrive.getCurrentPosition(),
                    robot.rightDrive.getCurrentPosition());
            telemetry.update();

            idle();
        }

        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);

        robot.leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        sleep((int) (1000 * pause));
    }
*/
    private void Launch(double balls) throws InterruptedException {

        runtime.reset();

        robot.leftLaunch.setPower(0.5);
        robot.rightLaunch.setPower(0.5);

        for (int l = 1; l <= balls; l++) {

            while (opModeIsActive() && (runtime.seconds() < 1)) {
                idle();
            }

            robot.beltMotor.setPower(0.25);

            while ((robot.beltMotor.getCurrentPosition() < 600) && (robot.beltMotor.getCurrentPosition() > -600)) {
                idle();
            }

            robot.beltMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Reset belt encoder
            robot.beltMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // Reset mode to use encoder

            runtime.reset();

        }

        robot.leftLaunch.setPower(0);
        robot.rightLaunch.setPower(0);

    }


   /* private void BeaconTest() throws InterruptedException {

        telemetry.addData("Beacon Test:", "NOW");

        robot.btnPushLeft.setPosition(0.3);
        robot.btnPushRight.setPosition(0.7);

        robot.leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.leftDrive.setTargetPosition((int) (-18 * COUNTS_PER_INCH));
        robot.rightDrive.setTargetPosition((int) (-18 * COUNTS_PER_INCH));

        robot.leftDrive.setPower(0.1);
        robot.rightDrive.setPower(0.1);

        runtime.reset();

        switch (Color) {

            case BLUE:
                robot.colorSensor.enableLed(false);
                while (true) {
                    if (robot.colorSensor.blue() >= 2 && robot.colorSensor.red() <= 1) {
                        robot.btnPushRight.setPosition(0);
                        break;
                    }
                }
                break;

            case RED:
                robot.colorSensor.enableLed(false);
                while (true) {
                    if (robot.colorSensor.red() >= 2 && robot.colorSensor.blue() <= 1) {
                        robot.btnPushLeft.setPosition(0.8);
                        break;
                    }
                }
                break;

        }

        while (robot.leftDrive.isBusy() && robot.rightDrive.isBusy()) {
            idle();
        }

        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);
        robot.btnPushLeft.setPosition(0.3);
        robot.btnPushRight.setPosition(0.7);

        robot.leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    }


    private void GyroTurn(double speed, double angle, Direction dir, double time, double pause) throws InterruptedException {

        double gyroAngle = 0; //Simple initialization of gyro turn target angle

        if (Color == Alliance.BLUE) {
            switch (dir) {

                case LEFT:
                    dir = Direction.RIGHT;
                    break;

                case RIGHT:
                    dir = Direction.LEFT;
                    break;
            }
        }

        switch (dir) {

            case LEFT:
                gyroAngle = 370 - angle;
                break;

            case RIGHT:
                gyroAngle = angle - 10;
                break;

        }


        robot.leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        runtime.reset();

        switch (dir) {

            case LEFT:
                robot.leftDrive.setPower(speed);
                robot.rightDrive.setPower(-speed);
                break;

            case RIGHT:
                robot.leftDrive.setPower(-speed);
                robot.rightDrive.setPower(speed);
                break;
        }


        while (runtime.seconds() < time) {

            telemetry.addData("Gyro: ", robot.gyroSensor.getHeading());
            telemetry.update();

            if ((robot.gyroSensor.getHeading() <= (gyroAngle + 2) && robot.gyroSensor.getHeading() >= (gyroAngle - 2))) {
                //These 2 degrees on either side are because MR sensor does not update angle every time it changes
                break;
            }

        }

        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);

        sleep((int) (pause * 1000));
    }

    private void compassTurn(double speed, double angle, Direction dir, double time, double pause) throws InterruptedException{

        double Offset, CompassAngle;

        Offset = robot.compass.getDirection();

        if (Color == Alliance.BLUE) {
            switch (dir) {

                case LEFT:
                    dir = Direction.RIGHT;
                    break;

                case RIGHT:
                    dir = Direction.LEFT;
                    break;
            }
        }

        if(dir == Direction.LEFT){
            angle = angle * -1;
        }

        CompassAngle= Offset + angle;

        if(CompassAngle >= 360){
            CompassAngle-=360;
        }

        switch (dir) {

            case LEFT:
                robot.leftDrive.setPower(speed);
                robot.rightDrive.setPower(-speed);
                break;

            case RIGHT:
                robot.leftDrive.setPower(-speed);
                robot.rightDrive.setPower(speed);
                break;
        }

        while (runtime.seconds() < time) {

            telemetry.addData("Compass: ", robot.compass.getDirection());
            telemetry.update();

            if ((robot.compass.getDirection() <= (CompassAngle + 2) && robot.compass.getDirection() >= (CompassAngle - 2))) {
                //These 2 degrees on either side are because MR sensor does not update angle every time it changes
                break;
            }

        }

        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);

        sleep((int) (pause * 1000));

    }

    /*double getBatteryVoltage() {
        double result = Double.POSITIVE_INFINITY;
        for (VoltageSensor sensor : hardwareMap.voltageSensor) {
            double voltage = sensor.getVoltage();
            if (voltage > 0) {
                result = Math.min(result, voltage);
            }
        }
        return result;
    }*/

}

//Compass.getDirection