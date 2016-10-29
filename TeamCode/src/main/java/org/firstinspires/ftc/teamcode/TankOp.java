package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Template: TankOp", group = "Linear Opmode")
public class TankOp extends OpMode {
    HardwareRobot robot = new HardwareRobot();

    final static double Arm_Min_Range = 0;
    final static double Arm_Max_Range = 1;

    double armPosition = 0;
    double armChange = 0.025;
    double LEDmode = 0;
    double beltMode = 0;
    double collectMode = 0;
    double extendMode = 0;


    public TankOp() {

    }

    @Override
    public void init() {
        // Servo Default Position
        armPosition = 0;

        // Sets Default Color Senor Mode
        LEDmode = 0;

        // Set Belt Mode
        beltMode = 0;

        // Set Collect Mode
        collectMode = 0;

        // Ser Extend Mode
        extendMode = 0;

    }

    @Override
    public void loop() {

        // Color Sensor Mode Changer
        if (LEDmode == 0) {
            robot.colorS1.enableLed(false);
        } else if (LEDmode == 1) {
            robot.colorS1.enableLed(true);
        }

        // Belt Code
        if (beltMode == 0) {
            robot.motor5.setPower(0);
        } else if (beltMode == 1) {
            robot.motor5.setPower(gamepad2.left_stick_y);
        }

        // Collect Code
        if (collectMode == 0) {
            robot.motor6.setPower(0);
        } else if (collectMode == 1) {
            robot.motor6.setPower(1);
        } else if (collectMode == 2) {
            robot.motor6.setPower(-1);
        }

        // Extend Code
        if (extendMode == 0) {
            robot.motor7.setPower(0);
        } else if (extendMode == 1) {
            robot.motor7.setPower(0.5);
        } else if (extendMode == 2) {
            robot.motor7.setPower(-0.5);
        }

        // Feedback Data
        telemetry.addData("Feedback Data for", "TankOp");
        telemetry.addData("Left Wheels", robot.motor1.getPower());
        telemetry.addData("Right Wheels", robot.motor2.getPower());
        telemetry.addData("Left Launching Power", robot.motor3.getPower() * -1);
        telemetry.addData("Right Launching Power", robot.motor4.getPower() * -1);
        telemetry.addData("Belt Speed", robot.motor5.getPower());
        telemetry.addData("Collector Speed", robot.motor6.getPower());
        telemetry.addData("Extend Speed", robot.motor7.getPower());
        telemetry.addData("Servos 1 and 2", robot.arm1.getPosition());
        telemetry.addData("Color Sensor: Red", robot.colorS1.red());
        telemetry.addData("Color Sensor: Green", robot.colorS1.green());
        telemetry.addData("Color Sensor: Blue", robot.colorS1.blue());

        //Assigns Joystick Values
        float Left_Motor = Range.clip(gamepad1.left_stick_y, -1, 1);
        float Right_Motor = Range.clip(gamepad1.right_stick_y, -1, 1);
        float Left_Launch = Range.clip(gamepad2.left_stick_y, -1, 1);
        float Right_Launch = Range.clip(gamepad2.right_stick_y, -1, 1);

        // Drive Code
        if (Left_Motor != 0) {
            robot.motor1.setPower(gamepad1.left_stick_y);
        }
        if (Right_Motor != 0) {
            robot.motor2.setPower(gamepad1.right_stick_y);
        }

        // Stops The Motors if Joypads Aren't Used
        if (Left_Motor == 0) {
            robot.motor1.setPower(0);
        }
        if (Right_Motor == 0) {
            robot.motor2.setPower(0);
        }

        // Launching Code
        if (Left_Launch != 0) {
            robot.motor3.setPower(gamepad2.left_stick_y);
        }
        if (Right_Launch != 0) {
            robot.motor4.setPower(gamepad2.right_stick_y);
        }

        // Stops The Motors if Joypads Aren't Used
        if (Left_Launch >= 0) {
            robot.motor3.setPower(0);
        }
        if (Right_Launch >= 0) {
            robot.motor4.setPower(0);
        }

        //Servo Position Updater
        if (gamepad1.a) {
            armPosition += armChange;
        }
        if (gamepad1.b) {
            armPosition -= armChange;
        }

        //Limits Servo Movement
        armPosition = Range.clip(armPosition, Arm_Min_Range, Arm_Max_Range);

        // Updates Servoes
        robot.arm1.setPosition(armPosition);
        robot.arm2.setPosition(armPosition);

        // LED Mode Updater
        if (gamepad2.left_bumper) {
            if (LEDmode == 0) {
                LEDmode = 2;
            } else if (LEDmode == 1) {
                LEDmode = 3;
            }
        }
        if (!gamepad2.left_bumper) {
            if (LEDmode == 2) {
                LEDmode = 1;
            } else if (LEDmode == 3) {
                LEDmode = 0;
            }
        }

        // Belt Mode Update
        if (gamepad2.a) {
            if (beltMode == 0) {
                beltMode = 2;
            } else if (beltMode == 1) {
                beltMode = 3;
            }
        }
        if (!gamepad2.a) {
            if (beltMode == 2) {
                beltMode = 1;
            } else if (beltMode == 3) {
                beltMode = 0;
            }
        }

        // Collector Mode Updater
        if (gamepad2.x || gamepad2.y) {
            if (collectMode == 0 && gamepad2.x) {
                collectMode = 3;
            } else if (collectMode == 0 && gamepad2.y) {
                collectMode = 4;
            } else if (collectMode == 1 || collectMode == 2) {
                collectMode = 5;
            }
        }
        if (!gamepad2.x && !gamepad2.y) {
            if (collectMode == 3) {
                collectMode = 1;
            } else if (collectMode == 4) {
                collectMode = 2;
            } else if (collectMode == 5) {
                collectMode = 0;
            }
        }

        // Extender Mode Updater
        // *NOTE* Add in a limit to how far motor as extend and contract so it doesn't break itself
        if (gamepad1.x || gamepad1.y) {
            if (extendMode == 0 && gamepad1.x) {
                extendMode = 3;
            } else if (extendMode == 0 && gamepad1.y) {
                extendMode = 4;
            } else if (extendMode == 1 || extendMode == 2) {
                extendMode = 5;
            }
        }
        if (!gamepad1.x && !gamepad1.y) {
            if (extendMode == 3) {
                extendMode = 1;
            } else if (extendMode == 4) {
                extendMode = 2;
            } else if (extendMode == 5) {
                extendMode = 0;
            }
        }

    }


    @Override
    public void stop() {

    }
}



