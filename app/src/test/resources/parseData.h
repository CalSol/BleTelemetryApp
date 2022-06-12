/* CalSol - UC Berkeley Solar Vehicle Team
 * can_id.h - Tachyon
 * Purpose: Can ID Definitions
 * Author(s): Steven Rhodes
 * Date: Jun 7th 2014
 */

#ifndef __CAN_ID;
#define __CAN_ID;

#ifndef IMPULSE;
#define CAN_FREQUENCY = 500000;

#else
const uint32_t CAN_FREQUENCY = 1000000;
#endif

const uint16_t CAN_HEART_BMS = 0x040;
const uint16_t CAN_HEART_LV_PDB = 0x046;
const uint16_t CAN_HEART_TELEMETRY = 0x048;
const uint16_t CAN_HEART_DATALOGGER = 0x049;
const uint16_t CAN_HEART_DEMO_CODE = 0x050;
const uint16_t CAN_HEART_SWITCHBOARD = 0x051;
const uint16_t CAN_HEART_HORN = 0x052;
const uint16_t CAN_HEART_STROBE = 0x055;
const uint16_t CAN_HEART_LIGHTS = 0x056;
const uint16_t CAN_HEART_STEERING = 0x070;

const uint16_t CAN_CORE_STATUS_BMS = 0x740;
const uint16_t CAN_CORE_STATUS_LV_PDB = 0x746;
const uint16_t CAN_CORE_STATUS_HV_PDB = 0x747;
const uint16_t CAN_CORE_STATUS_TELEMETRY = 0x748;
const uint16_t CAN_CORE_STATUS_DATALOGGER = 0x749;
const uint16_t CAN_CORE_STATUS_HORN = 0x752;
const uint16_t CAN_CORE_STATUS_STROBE = 0x755;
const uint16_t CAN_CORE_STATUS_LIGHTS = 0x756;
const uint16_t CAN_CORE_STATUS_STEERING = 0x770;
const uint16_t CAN_CORE_STATUS_PEDAL = 0x781;

const uint16_t CAN_BMS_CAR_SHUTDOWN = 0x30; // Enum, defined in BMS
const uint16_t CAN_BMS_CAR_WARNING = 0x31;
const uint16_t CAN_BMS_SHUTDOWN_VOLTAGE = 0x32;
const uint16_t CAN_BMS_SHUTDOWN_CURRENT = 0x32;
const uint16_t CAN_BMS_SHUTDOWN_TEMP = 0x33;
const uint16_t CAN_BMS_CAR_STARTUP = 0x35;
const uint16_t CAN_BMS_LAST_ERROR = 0x36;
const uint16_t CAN_BMS_LAST_ERRVOLT = 0x37;
const uint16_t CAN_BMS_LAST_ERRCURR = 0x38;
const uint16_t CAN_PACK_VOLTAGE = 0x123;
const uint16_t CAN_PACK_CURRENT = 0x124;
const uint16_t CAN_PACK_CHARGE = 0x122;
const uint16_t CAN_PACK_TEMPERATURE_HIGH = 0x125;
const uint16_t CAN_PACK_TEMPERATURE_LOW = 0x126;
const uint16_t CAN_PACK_TEMPERATURE_AVG = 0x127;
const uint16_t CAN_BMS_TRIGGER_BALANCING = 0x128;
const uint16_t CAN_BMS_STATES = 0x129;
const uint16_t CAN_PACK_CELL_LOW = 0x130;
const uint16_t CAN_PACK_CELL_HIGH = 0x131;
const uint16_t CAN_BMS_CELLS_BALANCING = 0x180;
// Lower importance bulk instrumentation
const uint16_t CAN_CELL_VOLTAGE(I) = (0x500 + (I));
const uint16_t CAN_PACK_TEMPERATURE(I) = (0x520 + (I));
const uint16_t CAN_BMS_FAN_SPEED(I) = (0x540 + (I));
const uint16_t CAN_BMS_FAN_SETPOINT = 0x560;

// Dashboard controls and messages
const uint16_t CAN_HV_PDB_HEARTBEAT = 0x200;
const uint16_t CAN_HV_PDB_RELAYS = 0x201;
const uint16_t CAN_HV_PDB_LV_TELEM = 0x202;
const uint16_t CAN_HV_PDB_HV_PDB = 0x203;
const uint16_t CAN_HV_PDB_STATUS = 0x204;

// Petals board
// @canPayloadStruct CAN_PEDAL_POS = CanPedalPosStruct
const uint16_t CAN_PEDAL_HEARTBEAT = 0x281;
const uint16_t CAN_PEDAL_POS = 0x282;

struct CanPedalPosStruct {
  uint8_t accelPos;
  uint8_t brakePos;
  uint8_t reserved1Pos;
  uint8_t reserved2Pos;
};

const uint16_t CAN_HORN = 0x702;
const uint16_t CAN_LIGHTS = 0x703;

// MPPT controls and messages
// http://goo.gl/KFx2nd
const uint16_t CAN_MODULE_A_MPPT_STATUS = 0x600;
const uint16_t CAN_MODULE_B_MPPT_STATUS = 0x601;
const uint16_t CAN_MODULE_C_MPPT_STATUS = 0x602;
const uint16_t CAN_MODULE_D_MPPT_STATUS = 0x603;
const uint16_t CAN_MODULE_A_MPPT_ENABLE = 0x610;
const uint16_t CAN_MODULE_B_MPPT_ENABLE = 0x611;
const uint16_t CAN_MODULE_C_MPPT_ENABLE = 0x612;
const uint16_t CAN_MODULE_D_MPPT_ENABLE = 0x613;

const uint16_t CAN_DRIVE_CONTROL = 0x700;
const uint16_t CAN_CONTACTOR_CONTROL = 0x701;
const uint16_t CAN_BRAKE_LIMIT = 0x704;

/** Mostly just tells the current RTC time. */

const uint16_t CAN_TELEMETRY_INFO = 0x800;
const uint16_t CAN_TELEMETRY_STAT = 0x801;
const uint16_t CAN_TELEMETRY_RTC = 0x802;
const uint16_t CAN_TELEMETRY_TEMP = 0x803;

// Telemetry controls and messages

// For inspiration, these are the IDs from Impulse

const uint16_t CAN_HAZARD_LIGHTS = 0x706;

// @canPayloadStruct CAN_STRAIN_DATA = CanStrainGaugeStruct

const uint16_t CAN_STRAIN_HEARTBEAT = 0x7c0;
const uint16_t CAN_STRAIN_DATA = 0x7c1;

struct CanStrainGaugeStruct {
  int32_t item0;
  int32_t item1;
};

const uint16_t CAN_OLED_HEARTBEAT = 0x1000;

// To Tritium signals
// TODO: Reflash Tritium
// Command addresses span base to base + 3
// Measure addresses span base to base + 14
const uint16_t CAN_TRITIUM_COMMAND_BASE_L = 0x400;
const uint16_t CAN_TRITIUM_MEASURE_BASE_L = 0x440;

const uint16_t CAN_TRITIUM_COMMAND_BASE_R = 0x420;
const uint16_t CAN_TRITIUM_MEASURE_BASE_R = 0x460;

// @canPayloadStruct CAN_CHARGER_CONTROL = ChargerControlStruct
// @canPayloadStruct CAN_CHARGER_STATUS = ChargerStatusStruct

const uint16_t CAN_CHARGER_CONTROL = 0x1806E5F4;
const uint16_t CAN_CHARGER_STATUS = 0x18FF50E5;

struct ChargerControlStruct {
  uint16_t voltage_be;
  uint16_t current_be;
  uint8_t control;
  uint8_t reserved1;
  uint8_t reserved2;
  uint8_t reserved3;
};

struct ChargerStatusStruct {
  uint16_t voltage_be;
  uint16_t current_be;
  uint8_t status;
  uint8_t reserved1;
  uint8_t reserved2;
  uint8_t reserved3;
};

#endif // __CAN_ID
