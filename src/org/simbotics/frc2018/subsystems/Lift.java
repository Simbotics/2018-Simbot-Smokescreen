package org.simbotics.frc2018.subsystems;

import org.simbotics.frc2018.RobotConstants;
import org.simbotics.frc2018.io.Dashboard;
import org.simbotics.frc2018.io.RobotOutput;
import org.simbotics.frc2018.io.SensorInput;
import org.simbotics.frc2018.subsystems.Intake.IntakeState;
import org.simbotics.frc2018.util.SimLib;
import org.simbotics.frc2018.util.SimPID;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Lift extends Subsystem {

	private static Lift instance;

	public static final double SHOOTING_DEGREES = 45;

	public enum WristPosition {
		SHOOTING, STANDBY, LEVEL, SHOOTING_GROUND, CLIMBING, SHOOTING_SCALE_UP, MOVING_TO_TARGET,STANDBY_TOP,
	}

	public enum ElevatorPosition {
		SWITCH, SCALE, SCALE_BETWEEN, SCALE_UP, PRE_HANGING, GROUND, GROUND_HAS_CUBE, FLOATING,
	}

	private RobotOutput robotOut;
	private SensorInput sensorIn;

	private SimPID wristPID;
	private SimPID wristCubePID;

	// set initially by button presses or autoncommand params
	private WristPosition desiredWristPosition;
	private ElevatorPosition desiredElevatorPosition;
	private ElevatorPosition prevDesiredElevatorPosition = ElevatorPosition.FLOATING;

	// intermediate necessary states depending on state machine
	private ElevatorPosition transientElevatorPosition;
	private WristPosition transientWristPosition;

	private IntakeState intakeState = IntakeState.OFF;

	private boolean isFirstCycle = true;
	private double elevatorOffset = 0;
	private boolean ignoreWrist = false;
	
	private double wristPositionOffset = 0;
	private boolean safeToLower = false;
	public static final double ELEVATOR_EPSILON = 0.2;
	private boolean wristInPositionFlag = false;
	private boolean isClimbing = false;
	

	public static Lift getInstance() {
		if (instance == null) {
			instance = new Lift();
		}
		return instance;
	}

	private Lift() {
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();
	}

	public void firstCycle() {
		this.robotOut.configureUpElevatorPID(RobotConstants.getElevatorPIDUp());
		this.robotOut.configureDownElevatorPID(RobotConstants.getElevatorPIDDown());
		this.wristPID = new SimPID(RobotConstants.getIntakePID());
		this.wristCubePID = new SimPID(RobotConstants.getIntakeCubePID());
		//this.wristCubePID.enableDebug();
		this.wristCubePID.disableIReset();
		//this.wristPID.enableDebug();
		this.wristPID.disableIReset();
	}

	public void setIntakeState(IntakeState state) {
		this.intakeState = state;
	}
	
	public void setClimbing(boolean climbing) {
		this.isClimbing = climbing;
	}

	public double getElevatorHeight(ElevatorPosition position) {
		switch (position) {
		case GROUND:
			return RobotConstants.ELEVATOR_GROUND_PRESET;
		case GROUND_HAS_CUBE:
			return RobotConstants.ELEVATOR_HAS_CUBE_PRESET;
		case SCALE:
			return RobotConstants.ELEVATOR_SCALE_PRESET + this.elevatorOffset;
		case SCALE_BETWEEN:
			return RobotConstants.ELEVATOR_SCALE_BETWEEN_PRESET;
		case SCALE_UP:
			return RobotConstants.ELEVATOR_SCALE_UP_PRESET + this.elevatorOffset;
		case SWITCH:
			return RobotConstants.ELEVATOR_SWITCH_PRESET + this.elevatorOffset;
		case FLOATING:
			return this.sensorIn.getElevatorHeight();
		default:
			return 0;
		}
	}

	public double getWristOffset() {
		return this.wristPositionOffset;
	}
	
	public double getWristPosition(WristPosition position) {
		if (position == WristPosition.LEVEL) {
			return RobotConstants.INTAKE_GROUND_PRESET;
		} else if (position == WristPosition.SHOOTING) {
			return RobotConstants.INTAKE_SHOOTING_PRESET;
		} else if (position == WristPosition.SHOOTING_GROUND) {
			return RobotConstants.INTAKE_SHOOTING_GROUND;
		} else if (position == WristPosition.STANDBY) {
			return RobotConstants.INTAKE_STANDBY;
		} else if (position == WristPosition.STANDBY_TOP) {
			return RobotConstants.INTAKE_STANDBY_TOP;
		}  else if (position == WristPosition.CLIMBING) {
			return RobotConstants.INTAKE_CLIMBING_PRESET;
		} else if (position == WristPosition.SHOOTING_SCALE_UP) {
			return RobotConstants.INTAKE_SHOOTING_SCALE_UP;
		}
		return 0;
	}

	public void pushToDashboard() {
		if(this.desiredElevatorPosition != null) {
			SmartDashboard.putString("ElevatorPositionState: ", this.desiredElevatorPosition.toString());
			
		}
		
	}

	public void setTarget(ElevatorPosition elevatorPosition, WristPosition wristPosition) {
		this.setTarget(elevatorPosition, wristPosition, 0, 0);
	}

	public void setTarget(ElevatorPosition elevatorPosition, WristPosition wristPosition, double wristPositionOffset,
			double elevatorOffset) {
		if(this.desiredElevatorPosition != elevatorPosition || this.desiredWristPosition != wristPosition) { // position has changed
			this.wristInPositionFlag = false;
		}
		this.desiredElevatorPosition = elevatorPosition;
		this.desiredWristPosition = wristPosition;
		this.elevatorOffset = elevatorOffset;
		this.wristPositionOffset = wristPositionOffset;
	}
	
	public void ignoreWrist(boolean ignore) {
		this.ignoreWrist = ignore;
	}

	public void calculate(boolean auto) {

		if (((this.desiredElevatorPosition == ElevatorPosition.SWITCH && Math.abs(this.getElevatorHeight(this.desiredElevatorPosition) - this.sensorIn.getElevatorHeight()) >= 2) 
				|| Math.abs(this.getElevatorHeight(this.desiredElevatorPosition) - this.sensorIn.getElevatorHeight()) >= 4) && !auto  &&!this.ignoreWrist) { 
			
			if(this.desiredElevatorPosition == ElevatorPosition.SCALE_UP) {
				this.transientWristPosition = WristPosition.STANDBY_TOP;
			} else {
				this.transientWristPosition = WristPosition.STANDBY;
			}
			
			
			if (this.wristInPositionFlag || this.wristInPosition() || auto) { // wrist is in standby
				this.transientElevatorPosition = this.desiredElevatorPosition;
				this.wristInPositionFlag = true;
			} else {
				this.transientElevatorPosition = ElevatorPosition.FLOATING;
			}

		} else {
			this.transientElevatorPosition = this.desiredElevatorPosition;
			this.transientWristPosition = WristPosition.MOVING_TO_TARGET;
		}

		if(!auto) {
			if (this.getElevatorHeight(this.desiredElevatorPosition) > this.sensorIn.getElevatorHeight()) { // going up
				if (Math.abs(
						this.getElevatorHeight(this.desiredElevatorPosition) - this.sensorIn.getElevatorHeight()) >= 1) {
					this.robotOut.setElevatorRampRate(Dashboard.getInstance().getElevatorRampRate());
				} else {
					this.robotOut.setElevatorRampRate(0);
				}
			} else {
				if (Math.abs(
						this.getElevatorHeight(this.desiredElevatorPosition) - this.sensorIn.getElevatorHeight()) >= 2) {
					this.robotOut.setElevatorRampRate(Dashboard.getInstance().getElevatorRampRate());
				} else {
					this.robotOut.setElevatorRampRate(0);
				}
			}
		} else {
			this.robotOut.setElevatorRampRate(0.05);
		}

		this.robotOut.setElevatorHeight(this.getElevatorHeight(this.transientElevatorPosition));

		if (this.transientWristPosition == WristPosition.MOVING_TO_TARGET) {
			double target = this.getWristPosition(this.transientWristPosition) + wristPositionOffset;
			double currentTarget;
			if(this.desiredElevatorPosition == ElevatorPosition.SCALE_UP) {
				currentTarget = 2 * target
						+ (this.distFromTarget() * (this.getWristPosition(WristPosition.STANDBY_TOP) - target) / 12.0);
				currentTarget = SimLib.limitValue(currentTarget, this.getWristPosition(WristPosition.STANDBY_TOP) + wristPositionOffset,
						this.getWristPosition(this.desiredWristPosition) + wristPositionOffset);
			} else {
				currentTarget = 2 * target
						+ (this.distFromTarget() * (this.getWristPosition(WristPosition.STANDBY) - target) / 3.0);
				currentTarget = SimLib.limitValue(currentTarget, this.getWristPosition(WristPosition.STANDBY) + wristPositionOffset,
						this.getWristPosition(this.desiredWristPosition) + wristPositionOffset);
			}
			
			if (this.distFromTarget() < 0.1) {
				this.transientWristPosition = this.desiredWristPosition;
			}
			this.wristPID.setDesiredValue(currentTarget);
			this.wristCubePID.setDesiredValue(currentTarget);
		} else {
			this.wristPID.setDesiredValue(this.getWristPosition(this.transientWristPosition) + wristPositionOffset);
			this.wristCubePID.setDesiredValue(this.getWristPosition(this.transientWristPosition) + wristPositionOffset);
		}

		double gravityValue = SmartDashboard.getNumber("Wrist G", 0.35);
		if (this.intakeState == IntakeState.HAS_CUBE || this.intakeState == IntakeState.DROPPING_CUBE
				|| this.intakeState == IntakeState.DROPPING_CUBE_SWITCH) {
			gravityValue = SmartDashboard.getNumber("Wrist G Cube", 0.35);
		}

		double gravityOffset = Math.cos(Math.toRadians(this.sensorIn.getIntakeDegrees())) * gravityValue;
		double accelerationFeedForward = this.sensorIn.getElevatorAcceleration()
				* RobotConstants.INTAKE_ACCELERATION_FEEDFORWARD;

		double output;
		
		if(this.isWristMovingUp() && (this.intakeState == IntakeState.HAS_CUBE || this.intakeState == IntakeState.DROPPING_CUBE
				|| this.intakeState == IntakeState.DROPPING_CUBE_SWITCH)) { // go harder
			
			output = this.wristCubePID.calcPID(this.sensorIn.getIntakeDegrees()) + gravityOffset
					+ accelerationFeedForward;
		} else {
			output = this.wristPID.calcPID(this.sensorIn.getIntakeDegrees()) + gravityOffset
					+ accelerationFeedForward;
		}
		
		
		if(this.desiredWristPosition != WristPosition.CLIMBING && this.sensorIn.getElevatorHeight() > 2.0 
				&& (this.desiredElevatorPosition == ElevatorPosition.GROUND || this.desiredElevatorPosition == ElevatorPosition.SWITCH)) {
			if(this.sensorIn.getIntakeDegrees() > 55) {
				output = -1.0;
			}
		}

		if (this.sensorIn.getIntakeDegrees() > 188 && output > 0.0) { // already at hardstop
			this.robotOut.setIntakeWrist(0);
			this.wristPID.resetErrorSum();
			this.wristCubePID.resetErrorSum();
		} else if (this.sensorIn.getIntakeDegrees() < 2 && output < 0.1) {
			this.robotOut.setIntakeWrist(0);
			this.wristPID.resetErrorSum();
			this.wristCubePID.resetErrorSum();
		} else {
			if(!this.ignoreWrist) {
				this.robotOut.setIntakeWrist(output);
			}
		}

		this.pushToDashboard();
		this.prevDesiredElevatorPosition = desiredElevatorPosition;

	}

	public boolean isDone() {
		if(!this.ignoreWrist) {
			if (Math.abs(this.sensorIn.getElevatorHeight() - this.getElevatorHeight(this.desiredElevatorPosition)) < 0.5
					&& Math.abs(this.sensorIn.getIntakeDegrees() - this.getWristPosition(this.desiredWristPosition)) < 5) {
				return true;
			} else {
				return false;
			}
		} else {
			return (Math.abs(this.sensorIn.getElevatorHeight() - this.getElevatorHeight(this.desiredElevatorPosition)) < 0.5);
		}
		
	}

	public double distFromTarget() {
		return Math.abs(this.sensorIn.getElevatorHeight() - this.getElevatorHeight(transientElevatorPosition));
	}

	public boolean isDesiredHigherThanElevator() {
		return this.sensorIn.getElevatorHeight() < this.getElevatorHeight(this.desiredElevatorPosition);
	}
	
	public boolean isWristMovingUp() {
		if(this.transientWristPosition == WristPosition.MOVING_TO_TARGET) {
			return false;
		} else {
			return this.sensorIn.getIntakeDegrees() < (this.getWristPosition(this.transientWristPosition) + wristPositionOffset);
		}
		
	}

	public boolean wristInPosition() {
		if(this.desiredElevatorPosition == ElevatorPosition.GROUND) {
			return Math.abs(this.sensorIn.getIntakeDegrees() - (this.getWristPosition(this.transientWristPosition) + this.wristPositionOffset)) <= 25;
		} else {
			return Math.abs(this.sensorIn.getIntakeDegrees() - (this.getWristPosition(this.transientWristPosition) + this.wristPositionOffset)) <= 25;
		}
		
	}

	public boolean elevatorInPosition() {
		return Math.abs(this.sensorIn.getElevatorHeight() - this.getElevatorHeight(this.transientElevatorPosition)) <= 0.2;
	}
	

	public WristPosition getDesiredWristPosition() {
		return this.desiredWristPosition;
	}

	public ElevatorPosition getDesiredElevatorPosition() {
		return this.desiredElevatorPosition;
	}

	@Override
	public void disable() {
		this.robotOut.setIntakeWrist(0.0);
		this.robotOut.setElevator(0.0);
	}

	@Override
	public void calculate() {
		calculate(false);
	}

}