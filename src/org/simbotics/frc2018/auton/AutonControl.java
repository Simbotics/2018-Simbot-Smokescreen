package org.simbotics.frc2018.auton;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.simbotics.frc2018.auton.mode.AutonBuilder;
import org.simbotics.frc2018.auton.mode.AutonMode;
import org.simbotics.frc2018.auton.mode.DefaultMode;
import org.simbotics.frc2018.auton.mode.DriveBackAuto;
import org.simbotics.frc2018.auton.mode.DriveStraightAuto;
import org.simbotics.frc2018.auton.mode.left.LeftSide1FarRightScale;
import org.simbotics.frc2018.auton.mode.left.LeftSide2LeftSwitch;
import org.simbotics.frc2018.auton.mode.left.LeftSide2RightScale;
import org.simbotics.frc2018.auton.mode.left.LeftSide2RightSwitch;
import org.simbotics.frc2018.auton.mode.left.LeftSide3LeftScale;
import org.simbotics.frc2018.auton.mode.left.LeftSideDriveToCenter;
import org.simbotics.frc2018.auton.mode.middle.MiddleSwitchLeft1ScaleLeft1;
import org.simbotics.frc2018.auton.mode.middle.MiddleSwitchLeft1ScaleRight1;
import org.simbotics.frc2018.auton.mode.middle.MiddleSwitchLeft4Cubes;
import org.simbotics.frc2018.auton.mode.middle.MiddleSwitchRight1ScaleLeft1;
import org.simbotics.frc2018.auton.mode.middle.MiddleSwitchRight1ScaleRight1;
import org.simbotics.frc2018.auton.mode.middle.MiddleSwitchRight4Cubes;
import org.simbotics.frc2018.auton.mode.right.RightSide1FarLeftScale;
import org.simbotics.frc2018.auton.mode.right.RightSide2LeftScale;
import org.simbotics.frc2018.auton.mode.right.RightSide2LeftSwitch;
import org.simbotics.frc2018.auton.mode.right.RightSide2RightSwitch;
import org.simbotics.frc2018.auton.mode.right.RightSide3RightScale;
import org.simbotics.frc2018.auton.mode.right.RightSideDriveToCenter;
import org.simbotics.frc2018.io.Dashboard;
import org.simbotics.frc2018.io.DriverInput;
import org.simbotics.frc2018.io.RobotOutput;
import org.simbotics.frc2018.util.Debugger;

/**
 *
 * @author Programmers
 */
public class AutonControl {

	private static AutonControl instance;

	public static final int NUM_ARRAY_MODE_STEPS = 4;

	private int autonDelay;
	private long autonStartTime;

	private boolean running;

	private int selectedModeSet = 0;
	private int selectedModeOverride = 0;
	private AutonModeSet selectedModes;

	private final AutonMode[] rrModes;
	private final AutonMode[] llModes;
	private final AutonMode[] rlModes;
	private final AutonMode[] lrModes;
	private final AutonModeSet[] autonModes;

	private int currIndex;
	private AutonCommand[] commands;

	private String autoSelectError = "NO ERROR";

	public static AutonControl getInstance() {
		if (instance == null) {
			instance = new AutonControl();
		}
		return instance;
	}

	private AutonControl() {
		this.autonDelay = 0;
		this.currIndex = 0;

		// GOTCHA: remember to put all auton modes here
		this.rrModes = new AutonMode[] { new DefaultMode(), new DriveStraightAuto(), new DriveBackAuto(),
				new MiddleSwitchRight4Cubes(), new RightSide3RightScale(), new LeftSide2RightScale(),
				new RightSide2RightSwitch(), new RightSideDriveToCenter(), new LeftSide2RightSwitch(),
				new LeftSideDriveToCenter(), new MiddleSwitchRight1ScaleRight1(), new LeftSide1FarRightScale(),
				new RightSide2LeftScale() };

		this.rlModes = new AutonMode[] { new DefaultMode(), new DriveStraightAuto(), new DriveBackAuto(),
				new MiddleSwitchRight4Cubes(), new RightSide3RightScale(), new RightSide2LeftScale(),
				new RightSide2RightSwitch(), new LeftSide3LeftScale(), new RightSideDriveToCenter(),
				new LeftSideDriveToCenter(), new LeftSide2RightSwitch(), new RightSide2RightSwitch(),
				new MiddleSwitchRight1ScaleLeft1(), new RightSide1FarLeftScale()};

		this.lrModes = new AutonMode[] { new DefaultMode(), new DriveStraightAuto(), new DriveBackAuto(),
				new RightSide3RightScale(), new RightSideDriveToCenter(),
				new RightSide2LeftSwitch(), new LeftSide2LeftSwitch(), new LeftSide2RightScale(),
				new LeftSideDriveToCenter(), new MiddleSwitchLeft1ScaleRight1(), new LeftSide1FarRightScale(),
				new RightSide2LeftScale() };

		this.llModes = new AutonMode[] { new DefaultMode(), new DriveStraightAuto(), new DriveBackAuto(),
				new RightSide2LeftScale(), new RightSide2LeftSwitch(),
				new RightSideDriveToCenter(), new LeftSide2LeftSwitch(), new RightSide3RightScale(),
				new LeftSide3LeftScale(), new LeftSideDriveToCenter(), new MiddleSwitchLeft1ScaleLeft1(),
				new RightSide1FarLeftScale()};

		this.autonModes = new AutonModeSet[] {
				// rr, rl, lr, ll is the order.
				new AutonModeSet("Default Set", new DefaultMode(), new DefaultMode(), new DefaultMode(),
						new DefaultMode()),

				new AutonModeSet("Drive Straight", new DriveStraightAuto(), new DriveStraightAuto(),
						new DriveStraightAuto(), new DriveStraightAuto()),

				new AutonModeSet("Drive Back", new DriveBackAuto(), new DriveBackAuto(), new DriveBackAuto(),
						new DriveBackAuto()),

				new AutonModeSet("Middle 3.5 Switch", new MiddleSwitchRight4Cubes(),
						new MiddleSwitchRight4Cubes(), new MiddleSwitchLeft4Cubes(),
						new MiddleSwitchLeft4Cubes()),

				new AutonModeSet("Only Scale Right Side", new RightSide3RightScale(), new RightSide2LeftScale(),
						new RightSide3RightScale(), new RightSide2LeftScale()),

				new AutonModeSet("Only Scale Left Side", new LeftSide2RightScale(), new LeftSide3LeftScale(),
						new LeftSide2RightScale(), new LeftSide3LeftScale()),
						
				new AutonModeSet("Middle 1 Switch 1 Scale", new MiddleSwitchRight1ScaleRight1(),
						new MiddleSwitchRight1ScaleLeft1(), new MiddleSwitchLeft1ScaleRight1(),
						new MiddleSwitchLeft1ScaleLeft1()),
						
				new AutonModeSet("Right Side Elims do switch", new RightSide3RightScale(),
						new RightSide2RightSwitch(), new RightSide3RightScale(),
						new RightSide2LeftSwitch()),
						
				new AutonModeSet("Left Side Elims do switch", new LeftSide2RightSwitch(),
						new LeftSide3LeftScale(), new LeftSide2LeftSwitch(), new LeftSide3LeftScale()),
				
				new AutonModeSet("Right Side Elims w/switch partner", new RightSide3RightScale(),
						new RightSide2RightSwitch(), new RightSide3RightScale(), new RightSideDriveToCenter()),
						
				new AutonModeSet("Left Side Elims w/switch partner", new LeftSideDriveToCenter(),
						new LeftSide3LeftScale(), new LeftSide2LeftSwitch(), new LeftSide3LeftScale()),

				new AutonModeSet("Right Side Elims 1 far scale", new RightSide3RightScale(),
						new RightSide1FarLeftScale(), new RightSide3RightScale(), new RightSide1FarLeftScale()),

				new AutonModeSet("Left Side Elims 1 far scale", new LeftSide1FarRightScale(), new LeftSide3LeftScale(),
						new LeftSide1FarRightScale(), new LeftSide3LeftScale()),

				// rr, rl, lr, ll is the order.

		};
		this.selectedModes = this.autonModes[0];
	}

	public void initialize() {
		Debugger.println("START AUTO");

		this.currIndex = 0;
		this.running = true;

		// initialize auton in runCycle
		AutonBuilder ab = new AutonBuilder();

		selectedModes.getMode().addToMode(ab);

		// get the full auton mode
		this.commands = ab.getAutonList();

		this.autonStartTime = System.currentTimeMillis();

		// clear out each components "run seat"
		AutonCommand.reset();
	}

	public void runCycle() {
		// haven't initialized list yet
		long timeElapsed = System.currentTimeMillis() - this.autonStartTime;
		if (timeElapsed > this.getAutonDelayLength() && this.running) {

			// start waiting commands
			while (this.currIndex < this.commands.length && this.commands[this.currIndex].checkAndRun()) {
				this.currIndex++;
			}
			// calculate call for all running commands
			AutonCommand.execute();
		} else {
			RobotOutput.getInstance().stopAll();
		}

	}

	public void stop() {
		this.running = false;
	}

	public long getAutonDelayLength() {
		return this.autonDelay * 500;
	}

	public void updateModes() {
		DriverInput driverIn = DriverInput.getInstance();

		boolean updatingAutoMode = false;

		try {

			int val = 0;
			if (driverIn.getAutonModeIncrease()) {
				val = 1;
			} else if (driverIn.getAutonModeIncreaseBy10()) {
				val = 10;
			} else if (driverIn.getAutonModeDecrease()) {
				val = -1;
			} else if (driverIn.getAutonModeDecreaseBy10()) {
				val = -10;
			}

			if (val != 0) {
				updatingAutoMode = true;

				if (driverIn.getAutonModifyLL()) {

					this.selectedModeOverride = this.limit(this.selectedModeOverride + val, this.llModes.length);
					this.selectedModes.setLLMode(llModes[selectedModeOverride]);

				} else if (driverIn.getAutonModifyRR()) {

					this.selectedModeOverride = this.limit(this.selectedModeOverride + val, this.rrModes.length);
					this.selectedModes.setRRMode(rrModes[selectedModeOverride]);

				} else if (driverIn.getAutonModifyLR()) {

					this.selectedModeOverride = this.limit(this.selectedModeOverride + val, this.lrModes.length);
					this.selectedModes.setLRMode(lrModes[selectedModeOverride]);

				} else if (driverIn.getAutonModifyRL()) {

					this.selectedModeOverride = this.limit(this.selectedModeOverride + val, this.rlModes.length);
					this.selectedModes.setRLMode(rlModes[selectedModeOverride]);

				} else {

					this.selectedModeSet = this.limit(this.selectedModeSet + val, this.autonModes.length);
					this.selectedModes = new AutonModeSet(this.autonModes[this.selectedModeSet]);

				}

				Dashboard.getInstance().printAutoModes(selectedModes,
						!this.selectedModes.equals(this.autonModes[this.selectedModeSet]));

			} else if (driverIn.getAutonSetDelayButton()) {

				this.autonDelay = (int) ((driverIn.getAutonDelayStick() + 1) * 5.0);
				if (this.autonDelay < 0) {
					this.autonDelay = 0;
				}
				Dashboard.getInstance().printAutoDelay(this.autonDelay);
			}

		} catch (Exception e) {
			// this.autonMode = 0;
			// TODO: some kind of error catching

			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));

			this.autoSelectError = sw.toString();

		}

		Dashboard.getInstance().updateAutoModes(selectedModes,
				!this.selectedModes.equals(this.autonModes[this.selectedModeSet]));

		// delay
		String delayAmt = "";
		if (this.autonDelay < 10) {
			// pad in a blank space for single digit delay
			delayAmt = " " + this.autonDelay;
		} else {
			delayAmt = "" + this.autonDelay;
		}

		Dashboard.getInstance().updateAutoDelay(this.autonDelay);
	}

	private int limit(int val, int limit) {
		if (val >= limit)
			return limit - 1;
		if (val < 0)
			return 0;
		return val;
	}
}
