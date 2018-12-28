package org.simbotics.frc2018.util;

import org.simbotics.frc2018.io.SensorInput;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveControl {

	public static enum Mode {
		ARCADE, AUTO_ROTATE, ABSOLUTE,
	}

	private static DriveControl instance;

	private SensorInput sensorIn;

	private double leftDriveOutput;
	private double rightDriveOutput;
	private Mode currentMode;

	private DriveControl() {
		this.sensorIn = SensorInput.getInstance();
		this.setMode(Mode.ARCADE);
	}

	public static DriveControl getInstance() {
		if (instance == null) {
			instance = new DriveControl();
		}
		return instance;
	}

	public void setMode(Mode mode) {
		this.currentMode = mode;
	}

	public Mode getMode() {
		return this.currentMode;
	}

	public double getRightDrive() {
		return this.rightDriveOutput;
	}

	public double getLeftDrive() {
		return this.leftDriveOutput;
	}

	public void calculate(double x, double y) {

		int modeNum = Math.max(0, (int) SmartDashboard.getNumber("123_Drive Control Mode: ", 0));
		modeNum = (modeNum < Mode.values().length) ? modeNum : 0;
		this.currentMode = Mode.values()[modeNum];

		double angle = this.sensorIn.getAngle() % 360;

		switch (this.currentMode) {
		case ARCADE:
			this.leftDriveOutput = y + x;
			this.rightDriveOutput = y - x;
			break;
		case AUTO_ROTATE:
			if (angle > 180 || angle < 0) {
				this.leftDriveOutput = y + x;
				this.rightDriveOutput = y - x;
			} else {
				this.leftDriveOutput = -y + x;
				this.rightDriveOutput = -y - x;
			}
			break;
		case ABSOLUTE:
			angle = Math.toRadians(angle);
			double x_ = x * Math.cos(angle) - y * Math.sin(angle);
			double y_ = x * Math.sin(angle) + y * Math.cos(angle);

			this.leftDriveOutput = y_ + x_;
			this.rightDriveOutput = y_ - x_;
			break;
		default:
			System.out.println("Ya done goofed!");
			this.currentMode = Mode.ARCADE;
			break;
		}
	}
}
