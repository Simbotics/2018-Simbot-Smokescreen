package org.simbotics.frc2018.subsystems;

import org.simbotics.frc2018.RobotConstants;
import org.simbotics.frc2018.io.RobotOutput;
import org.simbotics.frc2018.io.SensorInput;

public class Intake extends Subsystem {

	public enum IntakeState {
		JAMMED_RIGHT, JAMMED_LEFT, INTAKE, OUTTAKE, OFF, HAS_CUBE, POST_JAM, 
		LEFT_BUMP, RIGHT_BUMP, DROPPED_CUBE, DROPPING_CUBE, DROPPING_CUBE_SWITCH,
	}

	private static Intake instance;

	private RobotOutput robotOut;
	private SensorInput sensorIn;
	private IntakeState currentIntakeState;
	private IntakeState desiredIntakeState;

	private boolean manual = false;
	private int jamCycles = 0;
	private int postJamCycles = 0;
	private int lightSensorCycles = 0;
	private int bumpSensorCycles = 0;
	private int oneSideBumpCycles = 0;
	private int droppingCycles = 0;
	private boolean intakeWasPressedFlag = false;
	private boolean usingBump = true;
	private boolean usingLightSensor = true;
	private boolean stayClosed = false;
	private int ledBlinkCount = 0;
	private int ledOnCount = 0;
	private int ledOffCount = 0;
	private boolean ledBlinkCubeFlag = false;
	private boolean ledBlinkDroppingFlag = false;
	
	private double manualOutput;
	private double autoOutput;

	public static Intake getInstance() {
		if (instance == null) {
			instance = new Intake();
		}
		return instance;
	}

	private Intake() {
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();
		this.currentIntakeState = IntakeState.OFF;
	}

	@Override
	public void firstCycle() {
		this.jamCycles = 0;
		this.postJamCycles = 0;
		this.lightSensorCycles = 0;
		this.bumpSensorCycles = 0;
		this.droppingCycles = 0;
		this.ledBlinkCount = 0;
		this.ledOnCount = 0;
		this.ledOffCount = 0;
		this.ledBlinkCubeFlag = false;
		this.intakeWasPressedFlag = false;
	}

	public void setUsingSensors(boolean usingBump, boolean usingLight, boolean stayClosed) {
		this.usingBump = usingBump;
		this.usingLightSensor = usingLight;
		this.stayClosed = stayClosed;
	}

	public void setManual(double power) {
		this.manualOutput = power;
		this.manual = true;
	}

	public void openClaw() {
		this.robotOut.setIntakeOpen(true);
	}

	public void closeClaw() {
		this.robotOut.setIntakeOpen(false);
	}

	public void openPancake() {
		this.robotOut.setIntakePancakeOpen(true);
	}

	public void closePancake() {
		this.robotOut.setIntakePancakeOpen(false);
	}

	public void setDesiredState(IntakeState state) {
		this.desiredIntakeState = state;
	}

	public void setCurrentState(IntakeState state) {
		this.currentIntakeState = state;
	}

	public void setAutoOutput(double power) {
		this.autoOutput = power;
		this.manual = false;
	}
	
	
	

	@Override
	public void calculate() {
		if (this.manual) {
			this.robotOut.setIntakeLeft(this.manualOutput);
			this.robotOut.setIntakeRight(this.manualOutput);
		} else {
			
			if (this.desiredIntakeState == IntakeState.DROPPING_CUBE
					&& this.currentIntakeState != IntakeState.DROPPED_CUBE) {
				this.currentIntakeState = IntakeState.DROPPING_CUBE;
			} else if (this.desiredIntakeState == IntakeState.DROPPING_CUBE_SWITCH
					&& this.currentIntakeState != IntakeState.DROPPED_CUBE) {
				this.currentIntakeState = IntakeState.DROPPING_CUBE_SWITCH;
			} else if (this.desiredIntakeState == IntakeState.INTAKE) {
				if (this.currentIntakeState == IntakeState.OFF) {
					this.currentIntakeState = IntakeState.INTAKE;
				}
			} else if (this.desiredIntakeState == IntakeState.OUTTAKE) {
				this.currentIntakeState = IntakeState.OUTTAKE;
			} else {
				if (this.currentIntakeState != IntakeState.HAS_CUBE
						&& this.currentIntakeState != IntakeState.DROPPED_CUBE
						&& this.currentIntakeState != IntakeState.DROPPING_CUBE
						&& this.currentIntakeState != IntakeState.DROPPING_CUBE_SWITCH) { // if we don't have a cube

					if (this.currentIntakeState == IntakeState.LEFT_BUMP
							|| this.currentIntakeState == IntakeState.RIGHT_BUMP) {
						this.currentIntakeState = IntakeState.HAS_CUBE;
					} else {
						this.currentIntakeState = IntakeState.OFF; // turn the intake off
					}

				}

			}
			

			if (this.currentIntakeState == IntakeState.INTAKE) { // wont become jammed if in post jam
				if (this.sensorIn.isLeftIntakeJammed()) {
					this.currentIntakeState = IntakeState.JAMMED_LEFT;
				} else if (this.sensorIn.isRightIntakeJammed()) {
					this.currentIntakeState = IntakeState.JAMMED_RIGHT;
				}
			}

			if (this.currentIntakeState == IntakeState.INTAKE || this.currentIntakeState == IntakeState.JAMMED_LEFT
					|| this.currentIntakeState == IntakeState.JAMMED_RIGHT
					|| this.currentIntakeState == IntakeState.POST_JAM
					|| this.currentIntakeState == IntakeState.LEFT_BUMP
					|| this.currentIntakeState == IntakeState.RIGHT_BUMP) {
				if (this.sensorIn.getIntakeLightSensor()) {
					this.lightSensorCycles++;
					if (this.lightSensorCycles > 0) {
						if (this.stayClosed) {
							this.robotOut.setIntakeOpen(false);
						} else {
							this.robotOut.setIntakeOpen(false);
						}

					}

					if (!this.usingBump) {
						if (this.lightSensorCycles > 10) {
							this.currentIntakeState = IntakeState.HAS_CUBE;
						}
					}

				} else {
					if (this.stayClosed) {
						this.robotOut.setIntakeOpen(false);
					} else {
						this.robotOut.setIntakeOpen(true);
					}
					this.lightSensorCycles = 0;
				}
				

			}

			if (this.currentIntakeState == IntakeState.INTAKE || this.currentIntakeState == IntakeState.JAMMED_LEFT
					|| this.currentIntakeState == IntakeState.JAMMED_RIGHT
					|| this.currentIntakeState == IntakeState.POST_JAM
					|| this.currentIntakeState == IntakeState.LEFT_BUMP
					|| this.currentIntakeState == IntakeState.RIGHT_BUMP) { // if we are trying to intake
				if (this.sensorIn.getIntakeBumpSensors()) { // we now have a cube so stop
					bumpSensorCycles++;
					if (bumpSensorCycles > 1) {
						this.currentIntakeState = IntakeState.HAS_CUBE;
					}
				} else {
					bumpSensorCycles = 0;
				}
			} else {
				bumpSensorCycles = 0;
			}

			if (this.currentIntakeState != IntakeState.OUTTAKE && this.currentIntakeState != IntakeState.DROPPED_CUBE
					&& this.currentIntakeState != IntakeState.HAS_CUBE
					&& this.currentIntakeState != IntakeState.POST_JAM
					&& this.currentIntakeState != IntakeState.DROPPING_CUBE
					&& this.currentIntakeState != IntakeState.DROPPING_CUBE_SWITCH) {
				if (this.sensorIn.getIntakeLeftBumpSensor() && !this.sensorIn.getIntakeRightBumpSensor()) {
					this.currentIntakeState = IntakeState.LEFT_BUMP;
				} else if (this.sensorIn.getIntakeRightBumpSensor() && !this.sensorIn.getIntakeLeftBumpSensor()) {
					this.currentIntakeState = IntakeState.RIGHT_BUMP;
				}
			}

			if (this.currentIntakeState == IntakeState.INTAKE) {
				this.postJamCycles = 0;
				this.jamCycles = 0;
				this.oneSideBumpCycles = 0;
				this.droppingCycles = 0;
				this.robotOut.setIntakeLeft(this.autoOutput);
				this.robotOut.setIntakeRight(this.autoOutput);
				this.robotOut.setIntakePancakeOpen(true);
				this.ledBlinkCubeFlag = false;
				this.ledBlinkDroppingFlag= false;
			} else if (this.currentIntakeState == IntakeState.OUTTAKE) {
				this.postJamCycles = 0;
				this.jamCycles = 0;
				this.oneSideBumpCycles = 0;
				this.droppingCycles = 0;
				double val = -this.autoOutput;
				val = Math.pow(val, 3);
				this.robotOut.setIntakeLeft(val);
				this.robotOut.setIntakeRight(val);
				this.robotOut.setIntakeOpen(false);
				this.robotOut.setIntakePancakeOpen(true);
				this.robotOut.setLed(false);
				this.ledBlinkCubeFlag = false;
				this.ledBlinkDroppingFlag= false;
			} else if (this.currentIntakeState == IntakeState.JAMMED_LEFT) {
				this.postJamCycles = 0;
				this.oneSideBumpCycles = 0;
				this.droppingCycles = 0;
				if (this.jamCycles < 5) { // run anti jam for 5 cycles
					this.robotOut.setIntakeLeft(-0.65);
					this.robotOut.setIntakeRight(1.0);
					this.jamCycles++;
				} else { // has anti jammed
					this.currentIntakeState = IntakeState.POST_JAM;
					this.jamCycles = 0;
				}
			} else if (this.currentIntakeState == IntakeState.JAMMED_RIGHT) {
				this.postJamCycles = 0;
				this.oneSideBumpCycles = 0;
				this.droppingCycles = 0;
				if (this.jamCycles < 5) { // run anti jam for 5 cycles
					this.robotOut.setIntakeLeft(1.0);
					this.robotOut.setIntakeRight(-0.65);
					this.jamCycles++;
				} else { // has anti jammed
					this.currentIntakeState = IntakeState.POST_JAM;
					this.jamCycles = 0;
				}
			} else if (this.currentIntakeState == IntakeState.POST_JAM) {
				this.jamCycles = 0;
				this.oneSideBumpCycles = 0;
				this.droppingCycles = 0;
				if (this.postJamCycles < 10) {
					this.robotOut.setIntakeLeft(this.autoOutput);
					this.robotOut.setIntakeRight(this.autoOutput);
					this.postJamCycles++;
				} else {
					this.currentIntakeState = IntakeState.INTAKE;
					this.postJamCycles = 0;
				}
			} else if (this.currentIntakeState == IntakeState.HAS_CUBE) {
				this.postJamCycles = 0;
				this.jamCycles = 0;
				this.oneSideBumpCycles = 0;
				this.droppingCycles = 0;
				this.robotOut.setIntakeLeft(0.0);
				this.robotOut.setIntakeRight(0.0);
				this.robotOut.setIntakeOpen(false);
				this.robotOut.setIntakePancakeOpen(false);
				
				if(!this.ledBlinkCubeFlag) { // if we haven't blinked yet
					if(this.ledBlinkCount < RobotConstants.LED_BLINK_HAS_CUBE_CYCLES) {
						if(this.ledOnCount < RobotConstants.LED_BLINK_FAST_ON_CYCLES) {
							this.robotOut.setLed(true);
							this.ledOnCount++;
						} else if(this.ledOffCount < RobotConstants.LED_BLINK_FAST_OFF_CYCLES) {
							this.robotOut.setLed(false);
							this.ledOffCount++;
						} else {
							this.ledBlinkCount++;
							this.ledOffCount = 0;
							this.ledOnCount = 0;
						}
					} else {
						this.ledBlinkCount = 0;
						this.ledOffCount = 0;
						this.ledOnCount = 0;
						this.ledBlinkCubeFlag = true;
					}
				} else { // keep it on
					this.robotOut.setLed(true);
				}
				
			} else if (this.currentIntakeState == IntakeState.DROPPING_CUBE) {
				this.droppingCycles++;

				this.robotOut.setIntakeLeft(-0.5);
				this.robotOut.setIntakeRight(-0.5);
				this.robotOut.setIntakeOpen(false);
				this.robotOut.setIntakePancakeOpen(true);
				this.ledBlinkCubeFlag = false;

				if (this.droppingCycles > 10) {
					this.currentIntakeState = IntakeState.DROPPED_CUBE;
				}
				
				if(!this.ledBlinkDroppingFlag) { // if we haven't blinked yet
					if(this.ledBlinkCount < RobotConstants.LED_BLINK_DROPPING_CYCLES) {
						if(this.ledOnCount < RobotConstants.LED_BLINK_MED_ON_CYCLES) {
							this.robotOut.setLed(false);
							this.ledOnCount++;
						} else if(this.ledOffCount < RobotConstants.LED_BLINK_MED_OFF_CYCLES) {
							this.robotOut.setLed(true);
							this.ledOffCount++;
						} else {
							this.ledBlinkCount++;
							this.ledOffCount = 0;
							this.ledOnCount = 0;
						}
					} else {
						this.ledBlinkCount = 0;
						this.ledOffCount = 0;
						this.ledOnCount = 0;
						this.ledBlinkDroppingFlag = true;
					}
				} else { // keep it off
					this.robotOut.setLed(false);
				}

			} else if (this.currentIntakeState == IntakeState.DROPPING_CUBE_SWITCH) {
				this.droppingCycles++;

				this.robotOut.setIntakeLeft(-0.5);
				this.robotOut.setIntakeRight(-0.5);
				this.robotOut.setIntakeOpen(false);
				this.robotOut.setIntakePancakeOpen(true);
				this.ledBlinkCubeFlag = false;

				if (this.droppingCycles > 10) {
					this.currentIntakeState = IntakeState.DROPPED_CUBE;
				}
				
				if(!this.ledBlinkDroppingFlag) { // if we haven't blinked yet
					if(this.ledBlinkCount < RobotConstants.LED_BLINK_DROPPING_SWITCH_CYCLES) {
						if(this.ledOnCount < RobotConstants.LED_BLINK_MED_ON_CYCLES) {
							this.robotOut.setLed(true);
							this.ledOnCount++;
						} else if(this.ledOffCount < RobotConstants.LED_BLINK_MED_OFF_CYCLES) {
							this.robotOut.setLed(false);
							this.ledOffCount++;
						} else {
							this.ledBlinkCount++;
							this.ledOffCount = 0;
							this.ledOnCount = 0;
						}
					} else {
						this.ledBlinkCount = 0;
						this.ledOffCount = 0;
						this.ledOnCount = 0;
						this.ledBlinkDroppingFlag = true;
					}
				} else { // keep it off
					this.robotOut.setLed(false);
				}

			} else if (this.currentIntakeState == IntakeState.DROPPED_CUBE) {

				this.robotOut.setIntakeLeft(0.0);
				this.robotOut.setIntakeRight(0.0);
				this.robotOut.setIntakeOpen(true);
				this.robotOut.setIntakePancakeOpen(true);
				this.robotOut.setLed(false);
				this.ledBlinkDroppingFlag= false;
			} else if (this.currentIntakeState == IntakeState.OFF) {
				this.postJamCycles = 0;
				this.jamCycles = 0;
				this.oneSideBumpCycles = 0;
				this.droppingCycles = 0;
				this.robotOut.setIntakeLeft(0.0);
				this.robotOut.setIntakeRight(0.0);
				this.robotOut.setIntakeOpen(false);
				// this.robotOut.setIntakePancakeOpen(false);
			} else if (this.currentIntakeState == IntakeState.LEFT_BUMP) {
				if (this.oneSideBumpCycles < 5) {
					this.oneSideBumpCycles++;
					this.robotOut.setIntakeLeft(0.2);
					this.robotOut.setIntakeRight(1.0);
					this.robotOut.setIntakeOpen(false);
					this.robotOut.setIntakePancakeOpen(true);
				} else {
					this.currentIntakeState = IntakeState.POST_JAM;
				}

			} else if (this.currentIntakeState == IntakeState.RIGHT_BUMP) {
				if (this.oneSideBumpCycles < 5) {
					this.oneSideBumpCycles++;
					this.robotOut.setIntakeLeft(1.0);
					this.robotOut.setIntakeRight(0.2);
					this.robotOut.setIntakeOpen(false);
					this.robotOut.setIntakePancakeOpen(true);
				} else {
					this.currentIntakeState = IntakeState.POST_JAM;
				}
			}

		}

	}

	public IntakeState getIntakeState() {
		return this.currentIntakeState;
	}

	public IntakeState getDesiredState() {
		return this.desiredIntakeState;
	}

	public boolean getManual() {
		return this.manual;
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		this.robotOut.setIntakeLeft(0.0);
		this.robotOut.setIntakeRight(0.0);
		this.robotOut.setLed(false);
	}
}
