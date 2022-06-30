const float PACKED_FLOAT = 0x310;

// @canPayloadStruct CAN_PEDAL_POS = CanPedalPosStruct
const uint16_t CAN_PEDAL_POS = 0x282;

struct CanPedalPosStruct {
  uint8_t accelPos;
  uint8_t brakePos;
  uint8_t reserved1Pos;
  uint8_t reserved2Pos;
};

// @canPayloadStruct CAN_TRITIUM_VELOCITY = CanTritiumVelocityStruct
const uint16_t CAN_TRITIUM_VELOCITY = 0x402;

struct CanTritiumVelocityStruct {
  float rpm;
  float mps;
};

