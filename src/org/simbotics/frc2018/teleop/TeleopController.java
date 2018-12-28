package org.simbotics.frc2018.teleop;

import org.simbotics.frc2018.RobotConstants;
import org.simbotics.frc2018.io.DriverInput;
import org.simbotics.frc2018.io.RobotOutput;
import org.simbotics.frc2018.io.SensorInput;
import org.simbotics.frc2018.subsystems.Drive;
import org.simbotics.frc2018.subsystems.Intake;
import org.simbotics.frc2018.subsystems.Lift;
import org.simbotics.frc2018.subsystems.Intake.IntakeState;
import org.simbotics.frc2018.subsystems.Lift.ElevatorPosition;
import org.simbotics.frc2018.subsystems.Lift.WristPosition;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TeleopController extends TeleopComponent {

	private static TeleopController instance;
	private DriverInput driverIn;
	private SensorInput sensorIn;
	private RobotOutput robotOut;
	private Drive drive;
	private Intake intake;
	private Lift lift;

	private boolean intakeManual;
	
	private ElevatorPosition desiredElevatorPosition = ElevatorPosition.FLOATING;
	private WristPosition desiredWristPosition = WristPosition.STANDBY;
	private OperatorManual liftManual = OperatorManual.AUTO;
	private boolean manualWasPressedFlag = false;
	private double wristOffset = 0;
	private double elevatorOffset = 0;
	private int wristIntakeWaitCycles = 5;
	private int wristIntakeWaitCount = 0;
	private boolean isClimbing = false;
	
	private int ledOnCount = 0;
	private int ledOffCount = 0;
	private boolean ledSignalFlag = false;
	private int ledSignalCycleCount = 0;
	
	
	public enum OperatorManual {
		AUTO, AUTO_NO_WRIST, MANUAL,
	}
	

	public static TeleopController getInstance() {
		if (instance == null) {
			instance = new TeleopController();
		}
		return instance;
	}

	private TeleopController() {
		this.drive = Drive.getInstance();
		this.intake = Intake.getInstance();
		this.lift = Lift.getInstance();
		this.sensorIn = SensorInput.getInstance();
		this.driverIn = DriverInput.getInstance();
		this.robotOut = RobotOutput.getInstance();
	}

	@Override
	public void firstCycle() {
		// TODO Auto-generated method stub
		this.drive.firstCycle();
		this.lift.firstCycle();
		this.intake.firstCycle();
		this.intake.setUsingSensors(true, true, false);
		this.ledOffCount = 0;
		this.ledOnCount = 0;
		this.ledSignalCycleCount = 0;
		this.ledSignalFlag = false;
		if(SmartDashboard.getBoolean("USING WRIST", true)) {
			this.liftManual = OperatorManual.AUTO;
		} else {
			this.liftManual = OperatorManual.AUTO_NO_WRIST;
		}
		
	}

	@Override
	public void calculate() {

		//////////////////////////////
		///////// DRIVE CODE /////////
		//////////////////////////////

		double x = this.driverIn.getDriverX();
		double y = this.driverIn.getDriverY();

		if (Math.abs(x) < 0.05) {
			x = 0;
		}

		if (Math.abs(y) < 0.1) {
			y = 0;
		}

		// Drive forward while dropping in switch
		if (this.intake.getIntakeState() == IntakeState.OUTTAKE
				|| this.intake.getIntakeState() == IntakeState.DROPPING_CUBE) {
			if (this.lift.getDesiredElevatorPosition() == ElevatorPosition.SWITCH) {
				y += 0.2;
			}
		}
		
		// Set ramp rate
		if (this.sensorIn.getElevatorHeight() < 4.0 || this.liftManual == OperatorManual.MANUAL) {
			this.drive.setRampRate(0);
		} else {
			this.drive.setRampRate(this.sensorIn.getElevatorHeight() * 0.12);
		}

		// Set output to the drive
		this.drive.setOutput(y, x);
		this.drive.calculate();

		//////////////////////////////
		//////// INTAKE CODE /////////
		//////////////////////////////

		if (this.driverIn.getDriverManualOnButton()) {
			this.intakeManual = true;
		} else if (this.driverIn.getDriverManualOffButton()) {
			this.intakeManual = false;
		}

		if (this.intakeManual) {
			// Manual control
			if (this.driverIn.getIntakeTrigger() > 0.2) {
				this.intake.setManual(this.driverIn.getIntakeTrigger());
			} else if (this.driverIn.getOuttakeTrigger() > 0.2) {
				this.intake.setManual(-this.driverIn.getOuttakeTrigger());
			} else {
				this.intake.setManual(0.0);
			}

			if (this.driverIn.getIntakeOpenBumper()) {
				this.intake.openClaw();
			} else if (this.driverIn.getIntakeCloseBumper()) {
				this.intake.closeClaw();
			}

			if (this.driverIn.getIntakeOpenPancakeButton()) {
				this.intake.openPancake();
			} else if (this.driverIn.getIntakeClosePancakeButton()) {
				this.intake.closePancake();
			}
			if(this.driverIn.getHumanPlayerSignalButton()) {
				this.robotOut.setLed(true);
			} else {
				this.robotOut.setLed(false);
			}

		} else {
			if (this.driverIn.getIntakeTrigger() > 0.2) { // trying to intake
				this.intake.setAutoOutput(this.driverIn.getIntakeTrigger());
				this.intake.setDesiredState(IntakeState.INTAKE);
				if(this.intake.getIntakeState() != IntakeState.HAS_CUBE) {
					if(this.sensorIn.getIntakeLeftBumpSensor() || this.sensorIn.getIntakeRightBumpSensor() 
							|| this.sensorIn.getIntakeLightSensor()) { // we have a cube
						this.ledSignalFlag = false; // stop signaling
						this.robotOut.setLed(true);
					} else {
						if(!this.ledSignalFlag) {
							this.robotOut.setLed(false);
						}
					}
				}
			} else if (this.driverIn.getOuttakeTrigger() > 0.2) { // trying to outtake
				this.intake.setAutoOutput(this.driverIn.getOuttakeTrigger());
				this.intake.setDesiredState(IntakeState.OUTTAKE);
			} else if (this.driverIn.getIntakeOpenBumper()) { // dropping cube
				if (this.lift.getDesiredElevatorPosition() == ElevatorPosition.SWITCH) {
					this.intake.setDesiredState(IntakeState.DROPPING_CUBE_SWITCH);
				} else {
					this.intake.setDesiredState(IntakeState.DROPPING_CUBE);
				}
			} else if (this.intake.getDesiredState() == IntakeState.INTAKE 
					&& this.intake.getIntakeState() != IntakeState.HAS_CUBE 
					&& (this.sensorIn.getIntakeLeftBumpSensor() || this.sensorIn.getIntakeRightBumpSensor() 
							|| this.sensorIn.getIntakeLightSensor())) { // we were trying to intake but didnt finish but are close
					this.intake.setCurrentState(IntakeState.HAS_CUBE);
			} else { // Stop doing stuff
				this.intake.setDesiredState(IntakeState.OFF);
			}

		}
		
		

		this.intake.calculate();
		
		
		//////////////////////////////
		////////// LED CODE //////////
		//////////////////////////////
		
		if(this.driverIn.getHumanPlayerSignalButton()) {
			this.ledSignalFlag = true;
			this.ledSignalCycleCount = 0;
		} else if(this.intake.getIntakeState() == IntakeState.HAS_CUBE) {
			this.ledSignalFlag = false;
		}
		
		
		if(!this.intakeManual) {
			if(this.isClimbing) { // lifting up
				int newCycles = (int)this.sensorIn.getMatchTimeLeft();
				if(this.ledOnCount < newCycles) {
					this.robotOut.setLed(true);
					this.ledOnCount++;
				} else if(this.ledOffCount < newCycles) {
					this.robotOut.setLed(false);
					this.ledOffCount++;
				} else {
					this.ledOnCount = 0;
					this.ledOffCount = 0;
				}
			} else if(this.desiredWristPosition == WristPosition.CLIMBING) { // allignment stage
				if(this.ledOnCount < RobotConstants.LED_BLINK_SLOW_ON_CYCLES) {
					this.robotOut.setLed(true);
					this.ledOnCount++;
				} else if(this.ledOffCount < RobotConstants.LED_BLINK_SLOW_OFF_CYCLES) {
					this.robotOut.setLed(false);
					this.ledOffCount++;
				} else {
					this.ledOnCount = 0;
					this.ledOffCount = 0;
				}
			} else if(this.ledSignalFlag) {
				if(this.ledSignalCycleCount < RobotConstants.LED_BLINK_HP_CYCLES) {
					if(this.ledOnCount < RobotConstants.LED_BLINK_FAST_ON_CYCLES) {
						this.robotOut.setLed(true);
						this.ledOnCount++;
					} else if(this.ledOffCount < RobotConstants.LED_BLINK_FAST_OFF_CYCLES) {
						this.robotOut.setLed(false);
						this.ledOffCount++;
					} else {
						this.ledOnCount = 0;
						this.ledOffCount = 0;
						this.ledSignalCycleCount++;
					}
				} else {
					this.ledSignalFlag = false;
				}
			}
		}
		

		//////////////////////////////
		///////// LIFT CODE //////////
		//////////////////////////////

		if (this.driverIn.getOperatorManualOnButton() && !this.manualWasPressedFlag) {
			if(this.liftManual == OperatorManual.AUTO) {
				this.liftManual = OperatorManual.AUTO_NO_WRIST;
			} else if(this.liftManual == OperatorManual.AUTO_NO_WRIST) {
				this.liftManual = OperatorManual.MANUAL;
			}
			this.manualWasPressedFlag = true;
		
		} else if (this.driverIn.getOperatorManualOffButton() && !this.manualWasPressedFlag) {
			if(this.liftManual == OperatorManual.MANUAL) {
				this.liftManual = OperatorManual.AUTO_NO_WRIST;
			} else if(this.liftManual == OperatorManual.AUTO_NO_WRIST) {
				this.liftManual = OperatorManual.AUTO;
			}
			this.manualWasPressedFlag = true;
		} else {
			if(!this.driverIn.getOperatorManualOffButton() && !this.driverIn.getOperatorManualOnButton()) {
				this.manualWasPressedFlag = false;
			}
			
		}

		if (this.liftManual == OperatorManual.MANUAL) {
			this.isClimbing = false;
			double output = this.driverIn.getElevatorStick();
			if (Math.abs(output) < 0.05) {
				output = 0;
			}
			output += (RobotConstants.ELEVATOR_GRAVITY_OFFSET + 0.05);

			this.robotOut.setElevatorManual(output);
			this.robotOut.setIntakeWrist(this.driverIn.getWristAdjustmentStick() + 0.1);

		} else {
			
			// Setting desired elevator states
			if (this.driverIn.getGroundButton()) {
				this.desiredElevatorPosition = ElevatorPosition.GROUND;
				if (this.lift.getDesiredWristPosition() == WristPosition.CLIMBING) {
					this.isClimbing = true;
				} else if (this.intake.getDesiredState() != IntakeState.INTAKE) {
					this.desiredWristPosition = WristPosition.STANDBY;
					this.isClimbing = false;
					this.elevatorOffset = 0;
				} else {
					this.elevatorOffset = 0;
				}

				if (this.intake.getIntakeState() == IntakeState.DROPPED_CUBE) {
					this.intake.setCurrentState(IntakeState.OFF); // closes the claw and allows for intaking
				}
			} else if (this.driverIn.getSwitchButton()) {
				this.elevatorOffset = 0;
				this.desiredElevatorPosition = ElevatorPosition.SWITCH;
				this.desiredWristPosition = WristPosition.LEVEL;
				this.isClimbing = false;
			} else if (this.driverIn.getScaleButton()) {
				this.elevatorOffset = 0;
				if (this.desiredElevatorPosition != ElevatorPosition.SCALE
						|| this.desiredElevatorPosition != ElevatorPosition.SCALE_UP) {
					this.desiredWristPosition = WristPosition.LEVEL; // dont change wrist if going from scale to scale
																		// heights
				}
				this.desiredElevatorPosition = ElevatorPosition.SCALE;
				this.isClimbing = false;

			} else if (this.driverIn.getScaleUpButton()) {
				this.elevatorOffset = 0;
				if (this.desiredElevatorPosition != ElevatorPosition.SCALE
						|| this.desiredElevatorPosition != ElevatorPosition.SCALE_UP) {
					this.desiredWristPosition = WristPosition.LEVEL; // dont change wrist if going from scale to scale
																		// heights
				}
				this.desiredElevatorPosition = ElevatorPosition.SCALE_UP;
				this.isClimbing = false;
			}

			// Setting wrist states
			if (this.driverIn.getIntakeWristStraightButton()) {
				this.desiredWristPosition = WristPosition.LEVEL;
				this.wristOffset = 0;
			} else if (this.driverIn.getIntakeWristShootingButton()) {
				if (this.desiredElevatorPosition == ElevatorPosition.GROUND) {
					this.desiredWristPosition = WristPosition.SHOOTING_GROUND;
				} else if (this.desiredElevatorPosition == ElevatorPosition.SCALE_UP) {
					this.desiredWristPosition = WristPosition.SHOOTING_SCALE_UP;
					this.wristOffset = 0;
				} else {
					this.desiredWristPosition = WristPosition.SHOOTING;
					this.wristOffset = 0;
				}
			} else if (this.driverIn.getWristClimbButton()) {
				this.desiredWristPosition = WristPosition.CLIMBING;
			}

			if (this.desiredElevatorPosition == ElevatorPosition.GROUND
					|| this.desiredElevatorPosition == ElevatorPosition.FLOATING) { // only move wrist when intaking on
																					// ground
				if (this.driverIn.getIntakeTrigger() > 0.2 && this.intake.getIntakeState() != IntakeState.HAS_CUBE) {
					this.wristIntakeWaitCount++;
					if (this.wristIntakeWaitCount >= this.wristIntakeWaitCycles) { // wait for pistons to open intake
																					// first
						this.desiredWristPosition = WristPosition.LEVEL;
					}
				} else if (this.driverIn.getOuttakeTrigger() > 0.1) { // trying to score into the exchange
					if(this.desiredWristPosition != WristPosition.SHOOTING_GROUND) {
						this.desiredWristPosition = WristPosition.LEVEL;
					}
					this.wristIntakeWaitCount = 0;
				} else if (this.intake.getIntakeState() == IntakeState.HAS_CUBE
						|| this.intake.getIntakeState() == IntakeState.OFF) {
					if(this.desiredWristPosition == WristPosition.LEVEL && !this.driverIn.getIntakeWristStraightButton()) {
						this.desiredWristPosition = WristPosition.STANDBY; 
					}
					this.wristIntakeWaitCount = 0;
				}
			} else {
				this.wristIntakeWaitCount = 0;
			}

			if (this.intake.getIntakeState() == IntakeState.DROPPED_CUBE
					&& this.desiredWristPosition != WristPosition.STANDBY) { // Once we have dropped the cube move the
																				// wrist
				
				if(this.sensorIn.getElevatorHeight() <= RobotConstants.ELEVATOR_SCALE_UP_PRESET -0.5) {
					this.desiredWristPosition = WristPosition.STANDBY;
					
				} else if(this.lift.getWristPosition(this.desiredWristPosition) + this.lift.getWristOffset() < 30) {
				
					this.desiredWristPosition = WristPosition.STANDBY_TOP;
					
				}
				
				if (this.lift.getDesiredElevatorPosition() == ElevatorPosition.SWITCH) {
					this.elevatorOffset += 0.0;
				} else if (this.lift.getDesiredElevatorPosition() == ElevatorPosition.SCALE) { // don't do it on scale
																								// up position
					this.elevatorOffset += 0.0;
				}
			}
			
			if (this.desiredElevatorPosition == ElevatorPosition.SCALE
					|| this.desiredElevatorPosition == ElevatorPosition.SCALE_UP
					|| this.desiredElevatorPosition == ElevatorPosition.SWITCH) {
				double elevatorStick = this.driverIn.getElevatorStick();
				double wristStick = this.driverIn.getWristAdjustmentStick();
				
				if (Math.abs(elevatorStick) > 0.15) { // adjustment stick
					this.elevatorOffset += elevatorStick / 10.0;
				}
				
				if(Math.abs(wristStick) > 0.15) {
					this.wristOffset += wristStick * 1.5;
				}
				
				
				
				if(this.desiredWristPosition == WristPosition.LEVEL) {
					if(this.wristOffset > 70) {
						this.wristOffset = 70;
					} else if(this.wristOffset < 0) {
						this.wristOffset = 0;
					}
				} else if(this.desiredWristPosition == WristPosition.SHOOTING) {
					if(this.wristOffset > 25) {
						this.wristOffset = 25;
					} else if(this.wristOffset < -45) {
						this.wristOffset = -45;
					}
				} else if(this.desiredWristPosition == WristPosition.SHOOTING_SCALE_UP) {
					if(this.wristOffset > 15) {
						this.wristOffset = 15;
					} else if(this.wristOffset < -55) {
						this.wristOffset = -55;
					}
				} 
				
				//System.out.println("WRIST OFFSET" + this.wristOffset);
				
				if (this.desiredElevatorPosition == ElevatorPosition.SCALE) {
					this.elevatorOffset = Math.max(-0.8, elevatorOffset);
					this.elevatorOffset = Math.min(
							RobotConstants.ELEVATOR_SCALE_UP_PRESET - (RobotConstants.ELEVATOR_SCALE_PRESET),
							this.elevatorOffset);
				} else if (this.desiredElevatorPosition == ElevatorPosition.SCALE_UP) {
					this.elevatorOffset = Math.min(0, elevatorOffset);
					this.elevatorOffset = Math.max(
							(RobotConstants.ELEVATOR_SCALE_PRESET - 0.8) - RobotConstants.ELEVATOR_SCALE_UP_PRESET,
							this.elevatorOffset);
				} else if (this.desiredElevatorPosition == ElevatorPosition.SWITCH) {
					this.elevatorOffset = Math.min(1.0, elevatorOffset); // can't be > than 1.0
					this.elevatorOffset = Math.max(0, elevatorOffset); // will always be > 0
				}

			} else {
				if(!this.isClimbing) {
					this.elevatorOffset = 0;
					this.wristOffset = 0;
				}
			}

			if (this.isClimbing) { // limit output when climbing
				if (this.sensorIn.getElevatorHeight() > 0.75) {
					this.robotOut.setElevatorMaxOutput(1.0);
				} else {
					this.robotOut.setElevatorMaxOutput(0.3); // prevents motors from trying to lift another team and
																// dying
					this.desiredWristPosition = WristPosition.STANDBY;
				}
			} else {
				this.robotOut.setElevatorMaxOutput(1.0);
			}
			
			
			if(this.liftManual == OperatorManual.AUTO) {
				this.lift.ignoreWrist(false);
			} else {
				this.lift.ignoreWrist(true);
				this.robotOut.setIntakeWrist(this.driverIn.getWristAdjustmentStick() + 0.1);
			}
			
			
			
			this.lift.setIntakeState(this.intake.getIntakeState());
			this.lift.setTarget(this.desiredElevatorPosition, this.desiredWristPosition, this.wristOffset, this.elevatorOffset);
			this.lift.calculate(false);
			
			
			
		}
		SmartDashboard.putString("OPERATOR MANUAL", this.liftManual.toString());

	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		this.drive.disable();
		this.intake.disable();
		this.lift.disable();
	}
}
