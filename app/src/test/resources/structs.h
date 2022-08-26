//
// Created by Michael C. on 8/30/22.
//

#ifndef BOTTOM_NAV_STRUCTS_H
#define BOTTOM_NAV_STRUCTS_H


// @canPayloadStruct CAN_PEDAL_POS = CanPedalPosStruct
const uint16_t CAN_PEDAL_POS = 0x282;

struct CanPedalPosStruct {
  int accelPos;
  uint8_t brakePos1;
  int8_t brakePos2;
  uint16_t reserved1Pos;
  int16_t reserved2Pos;
  uint32_t reserved3Pos;
  int32_t reserved4Pos;
};


#endif //BOTTOM_NAV_STRUCTS_H
