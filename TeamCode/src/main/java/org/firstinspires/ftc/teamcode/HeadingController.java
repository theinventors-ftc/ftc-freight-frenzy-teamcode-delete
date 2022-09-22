package org.firstinspires.ftc.teamcode;

public class HeadingController {
    public enum Type {
        GYRO, CAMERA
    };

    private Type fType;

    private Gyro gyro;
    // private Camera camera;

    private double target = 0;
    private double kp = 0.02;

    private boolean enabled = false;
    private boolean findClosestTarget;

    private Button enabledToggle;
    private Button north;
    private Button east;
    private Button west;
    private Button south;

    public HeadingController(ExtendedGamepad gamepad1) {
        north = gamepad1.dpad_up;
        east = gamepad1.dpad_right;
        west = gamepad1.dpad_left;
        south = gamepad1.dpad_down;

        enabledToggle = gamepad1.right_stick_button;
    }

    public HeadingController(ExtendedGamepad gamepad, Gyro gyro) {
        this(gamepad);
        this.gyro = gyro;
        fType = Type.GYRO;
        kp = 0.02;
    }

    // public HeadingController(Gamepad gamepad, Camera camera) {
    //     this(gamepad);
    //     this.camera = camera;
    //     fType = Type.CAMERA;
    //     kp = 0.6;
    // }

    public void update() {
        updateState();
        if (fType == Type.GYRO)
            updateGyroTarget();
    }

    public double calculateTurn() {
        double curValue;
        if (fType == Type.CAMERA) {
            // curValue = camera.getObject_x();
            curValue = 0;
        } else {
            if (findClosestTarget) {
                target = gyro.findClosestOrientationTarget();
                findClosestTarget = false;
            }
            curValue = gyro.getValue();
        }

        return (target - curValue) * kp;
    }

    public boolean isEnabled() {
        return enabled;
    }

    private void updateGyroTarget() {
        double gyroValue = gyro.getValue();
        double dist, minDist;
        int minDistIdx, maxIdx;

        double targetOrient = target;
        boolean isBumped = false;
        if (north.isBumped()) {
            targetOrient = 0;
            isBumped = true;
        } else if (east.isBumped()) {
            targetOrient = 90;
            isBumped = true;
        } else if (west.isBumped()) {
            targetOrient = -90;
            isBumped = true;
        } else if (south.isBumped()) {
            targetOrient = 180;
            isBumped = true;
        }

        minDistIdx = 0;
        if (isBumped) {
            minDist = Math.abs(targetOrient - gyroValue);
            maxIdx = (int) Math.ceil(Math.abs(gyroValue) / 360);
            for (int i = -maxIdx; i <= maxIdx; i++) {
                dist = Math.abs(i * 360 + targetOrient - gyroValue);
                if (dist < minDist) {
                    minDistIdx = i;
                    minDist = dist;
                }
            }
        }

        target = minDistIdx * 360 + targetOrient;
    }

    private void updateState() {
        if (enabledToggle.isBumped()) {
            enabled = !enabled;
            findClosestTarget = enabled ? true : findClosestTarget;
        }
    }
}
