package org.simbotics.frc2018.io;

import org.simbotics.frc2018.util.LogitechF310Gamepad;

public class DriverInput {

	private static DriverInput instance;

	private LogitechF310Gamepad driver;
	private LogitechF310Gamepad operator;

	private boolean autonIncreaseStepWasPressed = false;
	private boolean autonDecreaseStepWasPressed = false;

	private boolean autonIncreaseModeWasPressed = false;
	private boolean autonDecreaseModeWasPressed = false;

	private boolean autonIncreaseMode10WasPressed = false;
	private boolean autonDecreaseMode10WasPressed = false;

	private DriverInput() {
		this.driver = new LogitechF310Gamepad(0);
		this.operator = new LogitechF310Gamepad(1);
	}

	public static DriverInput getInstance() {
		if (instance == null) {
			instance = new DriverInput();
		}
		return instance;
	}

	// MISCELLANEOUS
	public boolean getDriverManualOnButton() {
		return this.driver.getStartButton();
	}

	public boolean getDriverManualOffButton() {
		return this.driver.getBackButton();
	}

	public boolean getOperatorManualOnButton() {
		return this.operator.getStartButton();
	}

	public boolean getOperatorManualOffButton() {
		return this.operator.getBackButton();
	}

	public void setDriverRumble(double rumble) {
		this.driver.setRumble(rumble); // i cry
	}

	/*****************************
	 * DRIVER CONTROLS
	 *****************************/

	// DRIVE
	public double getDriverX() {
		return this.driver.getRightX();
	}

	public double getDriverY() {
		return this.driver.getLeftY();
	}

	// INTAKE
	public double getOuttakeTrigger() {
		if (this.driver.getLeftTrigger() > 0.2) {
			return this.driver.getLeftTrigger();
		} else {
			return 0;
		}
	}

	public double getIntakeTrigger() {
		if (this.driver.getRightTrigger() > 0.2) {
			return this.driver.getRightTrigger();
		} else {
			return 0;
		}
	}

	public boolean getNoIntakeCorrectionButton() {
		return this.driver.getRedButton();
	}

	public boolean getHumanPlayerSignalButton() {
		return this.driver.getGreenButton();
	}

	public boolean getIntakeOpenBumper() {
		return this.driver.getLeftBumper();
	}

	public boolean getIntakeCloseBumper() {
		return this.driver.getRightBumper();
	}

	public boolean getIntakeOpenPancakeButton() {
		return this.driver.getRedButton();
	}

	public boolean getIntakeClosePancakeButton() {
		return this.driver.getYellowButton();
	}

	/*****************************
	 * OPERATOR CONTROLS
	 *****************************/

	public double getElevatorStick() {
		return this.operator.getLeftY();
	}

	public boolean getGroundButton() {
		return this.operator.getGreenButton();
	}

	public boolean getSwitchButton() {
		return this.operator.getRedButton();
	}

	public boolean getScaleButton() {
		return this.operator.getBlueButton();
	}

	public boolean getScaleUpButton() {
		return this.operator.getYellowButton();
	}

	public boolean getPyramid2Button() {
		return this.operator.getPOVRight();
	}

	public boolean getPyramid3Button() {
		return this.operator.getPOVLeft();
	}

	public boolean getStartingIntakeWristButton() {
		return this.operator.getRightStickClick();
	}

	public boolean getHangButton() {
		return this.operator.getPOVDown();
	}

	public boolean getHangPrepButton() {
		return this.operator.getPOVUp();
	}

	public boolean getVaultButton() {
		return this.operator.getRightStickClick();
	}

	public boolean getIntakeWristStraightButton() {

		return this.operator.getRightTrigger() > 0.15;
	}

	public boolean getIntakeWristShootingButton() {
		return this.operator.getRightBumper();
	}

	public double getWristAdjustmentStick() {
		return this.operator.getRightY();
	}

	public boolean getWristClimbButton() {
		return this.operator.getLeftBumper();
	}

	public boolean getWrist180Trigger() {
		return this.operator.getLeftTrigger() > 0.15;
	}

	public boolean getClimbingLimitTrigger() {
		return this.operator.getLeftTrigger() > 0.15;
	}

	// ********************************
	// AUTO SELECTION CONTROLS
	// ********************************
	public boolean getAutonSetDelayButton() {
		return this.driver.getRightTrigger() > 0.3;
	}

	public double getAutonDelayStick() {
		return this.driver.getLeftY();
	}

	public boolean getAutonStepIncrease() {
		// only returns true on rising edge
		boolean result = this.driver.getRightBumper() && !this.autonIncreaseStepWasPressed;
		this.autonIncreaseStepWasPressed = this.driver.getRightBumper();
		return result;

	}

	public boolean getAutonStepDecrease() {
		// only returns true on rising edge
		boolean result = this.driver.getLeftBumper() && !this.autonDecreaseStepWasPressed;
		this.autonDecreaseStepWasPressed = this.driver.getLeftBumper();
		return result;

	}

	public boolean getAutonModeIncrease() {
		// only returns true on rising edge
		boolean result = this.driver.getRedButton() && !this.autonIncreaseModeWasPressed;
		this.autonIncreaseModeWasPressed = this.driver.getRedButton();
		return result;

	}

	public boolean getAutonModeDecrease() {
		// only returns true on rising edge
		boolean result = this.driver.getGreenButton() && !this.autonDecreaseModeWasPressed;
		this.autonDecreaseModeWasPressed = this.driver.getGreenButton();
		return result;

	}

	public boolean getAutonModeIncreaseBy10() {
		// only returns true on rising edge
		boolean result = this.driver.getYellowButton() && !this.autonIncreaseMode10WasPressed;
		this.autonIncreaseMode10WasPressed = this.driver.getYellowButton();
		return result;

	}

	public boolean getAutonModeDecreaseBy10() {
		// only returns true on rising edge
		boolean result = this.driver.getBlueButton() && !this.autonDecreaseMode10WasPressed;
		this.autonDecreaseMode10WasPressed = this.driver.getBlueButton();
		return result;

	}

	public boolean getAutonModifyLL() {
		return this.driver.getPOVLeft();
	}

	public boolean getAutonModifyRR() {
		return this.driver.getPOVRight();
	}

	public boolean getAutonModifyLR() {
		return this.driver.getPOVUp();
	}

	public boolean getAutonModifyRL() {
		return this.driver.getPOVDown();
	}
}