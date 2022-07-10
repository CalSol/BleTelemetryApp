/* CalSol - UC Berkeley Solar Vehicle Team
 * can_id.h - Zephyr
 * Purpose: Can ID Definitions
 * Author(s): Steven Rhodes
 * Date: Jun 7th 2014
 */

#ifndef __CAN_ID
#define __CAN_ID

#ifndef IMPULSE
const uint32_t CAN_FREQUENCY = 500000;
#else
const uint32_t CAN_FREQUENCY = 1000000;
#endif

// Heartbeats
const uint16_t CAN_HEART_BMS = 0x040;
const uint16_t CAN_HEART_CUTOFF = 0x041;
const uint16_t CAN_HEART_MCC_LEFT = 0x042;
const uint16_t CAN_HEART_MCC_RIGHT = 0x043;
const uint16_t CAN_HEART_DASHBOARD = 0x044;
const uint16_t CAN_HEART_POWERHUB_BOTTOM = 0x045;
const uint16_t CAN_HEART_POWERHUB_TOP = 0x046;
const uint16_t CAN_HEART_TELEMETRY = 0x047;
const uint16_t CAN_HEART_LPCTELEMETRY = 0x048;
const uint16_t CAN_HEART_DATALOGGER = 0x049;
#define CAN_HEART_MPPT(I)           0x060 + I
const uint16_t CAN_HEART_DEMO_CODE = 0x050;
const uint16_t CAN_HEART_ACTIVE_BALANCING = 0x051;
const uint16_t CAN_HEART_LIGHTS_FRONT = 0x056;
const uint16_t CAN_HEART_LIGHTS_REAR = 0x057;

const uint16_t CAN_HEART_SOLARJET_L = 0x290;
const uint16_t CAN_HEART_SOLARJET_R = 0x2A0;

// BMS controls and messages
                                             // TODO the uint32_t is actually a flag array
const uint16_t CAN_BMS_CAR_SHUTDOWN = 0x30;  // @canPayloadStruct CAN_BMS_CAR_SHUTDOWN = uint32_t
const uint16_t CAN_BMS_CAR_WARNING = 0x31;  // @canPayloadStruct CAN_BMS_CAR_WARNING = uint32_t
const uint16_t CAN_BMS_SHUTDOWN_VOLTAGE = 0x32;  // @canPayloadStruct CAN_BMS_SHUTDOWN_VOLTAGE = float
const uint16_t CAN_BMS_SHUTDOWN_CURRENT = 0x32;  // @canPayloadStruct CAN_BMS_SHUTDOWN_CURRENT = float
const uint16_t CAN_BMS_SHUTDOWN_TEMP = 0x33;  // @canPayloadStruct CAN_BMS_SHUTDOWN_TEMP = int16_t

struct PackVoltage {
    float voltage;
};
const uint16_t CAN_PACK_VOLTAGE = 0x123;  // @canPayloadStruct CAN_PACK_VOLTAGE = PackVoltage

struct PackCurrent {
    float current;
};
const uint16_t CAN_PACK_CURRENT = 0x124;  // @canPayloadStruct CAN_PACK_CURRENT = PackCurrent
const uint16_t CAN_PACK_CHARGE = 0x122;  // @canPayloadStruct CAN_PACK_CHARGE = int64_t

struct CellVolts {
    uint16_t cell[4];
};
struct CellVoltsDecoder {  // TODO: the decoder doesn't understand array notation, so we have this unrolled one for now
    uint16_t cell0;
    uint16_t cell1;
    uint16_t cell2;
    uint16_t cell3;
};
#define CAN_CELL_VOLTAGE(I)			(0x130 + (I))
const uint16_t CAN_CELL_VOLTAGE_0 = 0x130;  // @canPayloadStruct CAN_CELL_VOLTAGE_0 = CellVoltsDecoder
const uint16_t CAN_CELL_VOLTAGE_4 = 0x131;  // @canPayloadStruct CAN_CELL_VOLTAGE_4 = CellVoltsDecoder
const uint16_t CAN_CELL_VOLTAGE_8 = 0x132;  // @canPayloadStruct CAN_CELL_VOLTAGE_8 = CellVoltsDecoder
const uint16_t CAN_CELL_VOLTAGE_12 = 0x133;  // @canPayloadStruct CAN_CELL_VOLTAGE_12 = CellVoltsDecoder
const uint16_t CAN_CELL_VOLTAGE_16 = 0x134;  // @canPayloadStruct CAN_CELL_VOLTAGE_16 = CellVoltsDecoder
const uint16_t CAN_CELL_VOLTAGE_20 = 0x135;  // @canPayloadStruct CAN_CELL_VOLTAGE_20 = CellVoltsDecoder
const uint16_t CAN_CELL_VOLTAGE_24 = 0x136;  // @canPayloadStruct CAN_CELL_VOLTAGE_24 = CellVoltsDecoder

struct Temp {
    uint16_t temp;
};
const uint16_t CAN_PACK_TEMPERATURE_HIGH = 0x125;  // @canPayloadStruct CAN_PACK_TEMPERATURE_HIGH = Temp
const uint16_t CAN_PACK_TEMPERATURE_LOW = 0x126;  // @canPayloadStruct CAN_PACK_TEMPERATURE_LOW = Temp
const uint16_t CAN_PACK_TEMPERATURE_AVG = 0x127;  // @canPayloadStruct CAN_PACK_TEMPERATURE_AVG = Temp

const uint16_t CAN_BMS_TRIGGER_BALANCING = 0x128;
                                        // TODO actually a flag array
const uint16_t CAN_BMS_STATES = 0x129;  // @canPayloadStruct CAN_BMS_STATES = uint32_t

#define CAN_PACK_TEMPERATURE(I)     (0x160 + (I))

struct FanSpeedDecoder {  // TODO: the decoder doesn't understand array notation, so we have this unrolled one for now
    uint16_t fan0;
    uint16_t fan1;
    uint16_t fan2;
    uint16_t fan3;
};
#define CAN_BMS_FAN_SPEED(I)		(0x190 + (I))
const uint16_t CAN_BMS_FAN_SPEED_0 = 0x190;  // @canPayloadStruct CAN_BMS_FAN_SPEED_0 = FanSpeedDecoder
const uint16_t CAN_BMS_FAN_SPEED_1 = 0x191;  // @canPayloadStruct CAN_BMS_FAN_SPEED_1 = FanSpeedDecoder
const uint16_t CAN_BMS_FAN_SPEED_2 = 0x192;  // @canPayloadStruct CAN_BMS_FAN_SPEED_2 = FanSpeedDecoder

// Cutoff board controls and messages
const uint16_t CAN_CUTOFF_TRIGGER = 0x260;
const uint16_t CAN_CUTOFF_AIN_VOLTAGES = 0x261;
const uint16_t CAN_CUTOFF_SPI_VOLTAGES = 0x262;

// Dashboard controls and messages
struct pedalPos {
    uint8_t accel;
    uint8_t brake;
    uint8_t mechBrake;
};
const uint16_t CAN_PEDAL_POS = 0x282;  // @canPayloadStruct CAN_PEDAL_POS = pedalPos
const uint16_t CAN_BRAKE_BUTTON = 0x283;


struct RPM {
    float rpm;
};

// MCC queries - left
const uint16_t CAN_MCC_LEFT_RPM = 0x310;  // @canPayloadStruct CAN_MCC_LEFT_RPM = float

// MCC queries - right
const uint16_t CAN_MCC_RIGHT_RPM = 0x311;  // @canPayloadStruct CAN_MCC_RIGHT_RPM = float

// MCC thermistors
const uint16_t CAN_MCC_LEFT_TEMP = 0x320;
const uint16_t CAN_MCC_RIGHT_TEMP = 0x321;

const uint16_t CAN_MOTOR_OVERHEAT_L = 0x322;  
const uint16_t CAN_MOTOR_OVERHEAT_R = 0x323;

// PowerHub controls and messages
//const uint16_t CAN_POWERHUBBOTTOM_TURNON = 0x500;
//const uint16_t CAN_POWERHUBBOTTOM_TURNOFF = 0x501;

//const uint16_t CAN_POWERHUBTOP_TURNON = 0x504;
//const uint16_t CAN_POWERHUBTOP_TURNOFF = 0x505;

// PowerHub current sensors
// #define CAN_POWERHUBBOTTOM_CURRENT(I) 0x520 + I

// #define CAN_POWERHUBTOP_CURRENT(I) 0x529 + I


// #define CAN_MPPT_PWR(I)    0x550 + I
// #define CAN_MPPT_VC(I)     0x560 + I
// #define CAN_MPPT_T(I)      0x570 + I
// #define CAN_MPPT_DATA(I)   0x580 + I

// MPPT controls and messages
// http://goo.gl/KFx2nd
const uint16_t CAN_FRONT_RIGHT_MPPT_STATUS = 0x600;
const uint16_t CAN_FRONT_LEFT_MPPT_STATUS = 0x601;
const uint16_t CAN_BACK_RIGHT_MPPT_STATUS = 0x602;
const uint16_t CAN_BACK_LEFT_MPPT_STATUS = 0x603;
const uint16_t CAN_FRONT_RIGHT_MPPT_ENABLE = 0x610;
const uint16_t CAN_FRONT_LEFT_MPPT_ENABLE = 0x611;
const uint16_t CAN_BACK_RIGHT_MPPT_ENABLE = 0x612;
const uint16_t CAN_BACK_LEFT_MPPT_ENABLE = 0x613;

const uint16_t CAN_DRIVE_CONTROL = 0x700;
const uint16_t CAN_CONTACTOR_CONTROL = 0x701;
const uint16_t CAN_HORN = 0x702;
const uint16_t CAN_LIGHTS = 0x703;
const uint16_t CAN_BRAKE_LIMIT = 0x704;  // @canPayloadStruct CAN_BRAKE_LIMIT = uint8_t

struct ActiveBalancingPwmDecoder {  // TODO: the decoder doesn't understand array notation, so we have this unrolled one for now
    uint16_t pwm0;
    uint16_t pwm1;
    uint16_t pwm2;
    uint16_t pwm3;
    uint16_t pwm4;
    uint16_t pwm5;
    uint16_t pwm6;
    uint16_t pwm7;
};
#define CAN_ACTIVE_BALANCING_PWM(I) 0x710 + I
const uint16_t CAN_ACTIVE_BALANCING_PWM_ARR = 0x700;  // @canMessageArray length=8
                                                      // @canPayloadStruct CAN_ACTIVE_BALANCING_PWM_ARR = ActiveBalancingPwmDecoder
                                                      // TODO: proposed notation, doesn't actually work yet
const uint16_t CAN_ACTIVE_BALANCING_PWM_0 = 0x710;  // @canPayloadStruct CAN_ACTIVE_BALANCING_PWM_0 = ActiveBalancingPwmDecoder
const uint16_t CAN_ACTIVE_BALANCING_PWM_1 = 0x711;  // @canPayloadStruct CAN_ACTIVE_BALANCING_PWM_1 = ActiveBalancingPwmDecoder
const uint16_t CAN_ACTIVE_BALANCING_PWM_2 = 0x712;  // @canPayloadStruct CAN_ACTIVE_BALANCING_PWM_2 = ActiveBalancingPwmDecoder
const uint16_t CAN_ACTIVE_BALANCING_PWM_3 = 0x713;  // @canPayloadStruct CAN_ACTIVE_BALANCING_PWM_3 = ActiveBalancingPwmDecoder
const uint16_t CAN_ACTIVE_BALANCING_PWM_4 = 0x714;  // @canPayloadStruct CAN_ACTIVE_BALANCING_PWM_4 = ActiveBalancingPwmDecoder
const uint16_t CAN_ACTIVE_BALANCING_PWM_5 = 0x715;  // @canPayloadStruct CAN_ACTIVE_BALANCING_PWM_5 = ActiveBalancingPwmDecoder
const uint16_t CAN_ACTIVE_BALANCING_PWM_6 = 0x716;  // @canPayloadStruct CAN_ACTIVE_BALANCING_PWM_6 = ActiveBalancingPwmDecoder
const uint16_t CAN_ACTIVE_BALANCING_PWM_7 = 0x717;  // @canPayloadStruct CAN_ACTIVE_BALANCING_PWM_7 = ActiveBalancingPwmDecoder

struct GPSCoords {
    uint32_t lat;
    uint32_t lon;
};
const uint16_t CAN_GPS = 0x750;  // @canPayloadStruct CAN_GPS = GPSCoords
struct GPSTime {
	uint32_t centiSeconds;
};
const uint16_t CAN_GPS_TIME = 0x751;  // @canPayloadStruct CAN_GPS_TIME = GPSTime
struct GPSMeta {
	uint8_t quality;
	uint8_t numSats;
	uint16_t deciHdop;  // units of 1/10 hdop
};
const uint16_t CAN_GPS_META = 0x752;  // @canPayloadStruct CAN_GPS_META = GPSMeta
struct GPSAltitude {
	uint32_t decimetersAltitude;
};
const uint16_t CAN_GPS_ALT = 0x753;  // @canPayloadStruct CAN_GPS_ALT = GPSAltitude

/** Mostly just tells the current RTC time. */
// const uint16_t CAN_TELEMETRY_INFO = 0x800;
// const uint16_t CAN_TELEMETRY_STAT = 0x801;
// const uint16_t CAN_TELEMETRY_RTC = 0x802;
// const uint16_t CAN_TELEMETRY_TEMP = 0x803;

// Telemetry controls and messages

// For inspiration, these are the IDs from Impulse

//// Emergency signals
//const uint16_t CAN_EMER_BPS = 0x021;
//const uint16_t CAN_EMER_CUTOFF = 0x022;
//const uint16_t CAN_EMER_DRIVER_IO = 0x023;
//const uint16_t CAN_EMER_DRIVER_CTL = 0x024;
//const uint16_t CAN_EMER_TELEMETRY = 0x025;
//const uint16_t CAN_EMER_OTHER1 = 0x026;
//const uint16_t CAN_EMER_OTHER2 = 0x027;
//const uint16_t CAN_EMER_OTHER3 = 0x028;
//
//// Heartbeats
//const uint16_t CAN_HEART_BPS = 0x041;
//const uint16_t CAN_HEART_CUTOFF = 0x042;
//const uint16_t CAN_HEART_DRIVER_IO = 0x043;
//const uint16_t CAN_HEART_DRIVER_CTL = 0x044;
//const uint16_t CAN_HEART_TELEMETRY = 0x045;
//const uint16_t CAN_HEART_DATALOGGER = 0x046;
//const uint16_t CAN_HEART_OTHER2 = 0x047;
//const uint16_t CAN_HEART_OTHER3 = 0x048;
//
//// Cutoff signals
//const uint16_t CAN_CUTOFF_VOLT = 0x523;
//const uint16_t CAN_CUTOFF_CURR = 0x524;
//const uint16_t CAN_CUTOFF_NORMAL_SHUTDOWN = 0x521;
//
//// BPS signals
//#define CAN_BPS_BASE            0x100  // BPS signal base
//#define CAN_BPS_MODULE_OFFSET   0x010  // Difference between modules
//#define CAN_BPS_TEMP_OFFSET     0x00C  // Offset in addition to module offset
//#define CAN_BPS_DIE_TEMP_OFFSET 0x00C  // Offset for LT die temperature
//
//// To Tritium signals
//const uint16_t CAN_TRITIUM_DRIVE = 0x501;
//const uint16_t CAN_TRITIUM_RESET = 0x503;
//
//// From Tritium signals
//const uint16_t CAN_TRITIUM_ID = 0x400;
//const uint16_t CAN_TRITIUM_STATUS = 0x401;
//const uint16_t CAN_TRITIUM_BUS = 0x402;
//const uint16_t CAN_TRITIUM_VELOCITY = 0x403;
//const uint16_t CAN_TRITIUM_PHASE_CURR = 0x404;
//const uint16_t CAN_TRITIUM_MOTOR_VOLT = 0x405;
//const uint16_t CAN_TRITIUM_MOTOR_CURR = 0x406;
//const uint16_t CAN_TRITIUM_MOTOR_BEMF = 0x407;
//const uint16_t CAN_TRITIUM_15V_RAIL = 0x408;
//const uint16_t CAN_TRITIUM_LV_RAIL = 0x409;
//const uint16_t CAN_TRITIUM_FAN_SPEED = 0x40A;
//const uint16_t CAN_TRITIUM_MOTOR_TEMP = 0x40B;
//const uint16_t CAN_TRITIUM_AIR_TEMP = 0x40C;
//const uint16_t CAN_TRITIUM_CAP_TEMP = 0x40D;
//const uint16_t CAN_TRITIUM_ODOMETER = 0x40E;
//
//// Dashboard Signals
//const uint16_t CAN_DASHBOARD_INPUTS = 0x481;

#endif // __CAN_ID
