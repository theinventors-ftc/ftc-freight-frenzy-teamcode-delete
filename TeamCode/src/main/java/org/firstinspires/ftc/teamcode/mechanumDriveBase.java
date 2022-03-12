package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "Concept: MechanumBaseDrive", group = "Concept")

public class mechanumDriveBase extends LinearOpMode {
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor rearRight;
    private DcMotor rearLeft;
    private DcMotor intake;
    BNO055IMU imu;

    private boolean gyroFollowEnabled = false;

    private double target = 0;
    private double kp = 0.005;

    private String dpad_pressed = "";
    private boolean modeBtn_pressed = false;

    double[] calculatePowersMechanum(double xG, double yG, double rxG){
        double rx = (-rxG / 2) * (gamepad1.left_trigger + 1);
        double trig = gamepad1.right_trigger;
        double x = ((xG * 1.1) / 2) * (1 + trig);
        double y = (yG / 2) * (1 + trig);

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

        double[] returnThePowers = new double[4];
        returnThePowers[0] = (y + x + rx) / denominator;
        returnThePowers[1] = (y - x + rx) / denominator;
        returnThePowers[2] = (y - x - rx) / denominator;
        returnThePowers[3] = (y + x - rx) / denominator;

        return returnThePowers;
    }

    @Override
    public void runOpMode() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        rearRight = hardwareMap.get(DcMotor.class, "rearRight");
        rearLeft = hardwareMap.get(DcMotor.class, "rearLeft");
        intake = hardwareMap.get(DcMotor.class, "intake");

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        rearRight.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while(opModeIsActive()) {
            double gyroValue = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
            double[] allPower;

            if(gamepad1.dpad_right) {
                dpad_pressed = "right";
            } else if(gamepad1.dpad_left) {
                dpad_pressed = "left";
            }

            if(dpad_pressed == "right" && !gamepad1.dpad_right) {
                dpad_pressed = "";
                if(target < 90  ) {
                    target += 90;
                }
            }


            if(dpad_pressed == "left" && !gamepad1.dpad_left) {
                if(target > -90) {
                    target -= 90;
                }
            }

            if(gamepad1.right_stick_button) {
                modeBtn_pressed = true;
            }
            if(modeBtn_pressed && !gamepad1.right_stick_button) {
                modeBtn_pressed = false;
                if(gyroFollowEnabled) {
                    gyroFollowEnabled = false;
                } else {
                    gyroFollowEnabled = true;
                }
            }

            if (!gyroFollowEnabled) {
                allPower = calculatePowersMechanum(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
                frontLeft.setPower(allPower[0]);
                rearLeft.setPower(allPower[1]);
                frontRight.setPower(allPower[2]);
                rearRight.setPower(allPower[3]);
            } else {
                double rot = (target - gyroValue) * kp;
                allPower = calculatePowersMechanum(gamepad1.left_stick_x, gamepad1.left_stick_y, rot);
                frontLeft.setPower(allPower[3]);
                rearLeft.setPower(allPower[1]);
                frontRight.setPower(allPower[2]);
                rearRight.setPower(allPower[0]);
            }

            if(gamepad1.right_bumper) {
                intake.setPower(1);
            } else {
                intake.setPower(0);
            }

            telemetry.addData(">", "Powers 3: " + (allPower[3]) + "");
            telemetry.update();
        }
    }
}