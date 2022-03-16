package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.


@TeleOp(name = "Concept: SliderTest", group = "Concept")

public class SliderTest extends LinearOpMode {
    private DcMotorEx slider;

    int maxTicksPosition = 2150;
    int[] levelPositions = {5, 30, 533, 1121, 1678};
    int levelSelected = 0;
    String levelBtnPressed = "";

    public void setSliderLevel(int level)
    {
        slider.setTargetPosition(levelPositions[level + 1]);
        slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        if (level = -1) {
            slider.setPower(-0.05);
            sleep(300);
            slider.setPower(0);
            slider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
    }

    public void leave_element_at_level()
    {

        if(gamepad2.a) levelSelected = 1;
        else if(gamepad2.x) levelSelected = 2;
        else if(gamepad2.y) levelSelected = 3;
        setSliderLevel(levelSelected);

        // do servo stuff
        setSliderLevel(0);
    }

    @Override
    public void runOpMode() {
        slider = hardwareMap.get(DcMotorEx.class, "slider");
        slider.setDirection(DcMotorSimple.Direction.REVERSE);
        slider.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        slider.setVelocity(2000);


        waitForStart();
//
        while (opModeIsActive()) {



         setSliderLevel(1);
        sleep(4000);
        setSliderLevel(2);
        sleep(4000);
        setSliderLevel(3);
        sleep(4000);
        setSliderLevel(0);
        sleep(4000);
            telemetry.addData("Encoder value", slider.getCurrentPosition());
            telemetry.update();
//
        }
    }
}