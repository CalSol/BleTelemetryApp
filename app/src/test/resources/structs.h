//
// Created by Michael C. on 8/30/22.
//

#ifndef BOTTOM_NAV_STRUCTS_H
#define BOTTOM_NAV_STRUCTS_H

// @canPayloadStruct CAN_PEDAL_POS = CanPedalPosStruct
const uint16_t CAN_PEDAL_POS = 0x282;

struct CanPedalPosStruct {
  uint8_t brakePos1;
  int8_t brakePos2;
  uint16_t reserved1Pos;
  int16_t reserved2Pos;
  uint32_t reserved3Pos;
  int32_t reserved4Pos;
  int accelPos;
};

// @canPayloadStruct STEERING_POS = SteeringStruct
const uint16_t STEERING_POS = 0x300;

struct SteeringStruct {
  int positionDegree;
  struct Signal {
    int impulseResponse;
    int frequencyResponse;
  };
  struct Pressure {
    int pascal;
  };
};

#endif //BOTTOM_NAV_STRUCTS_H
