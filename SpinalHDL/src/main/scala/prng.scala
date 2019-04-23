

import spinal.core._

class prng() extends Component {

  val io = new Bundle {
    val next = in Bool
    val output = out Bits(256 bits)
    val input = in Bits(256 bits)
    val write = in Bool
  }

  val random = Reg(Bits (256 bits)) init B"xa9dfcef8049594b314b744fa00d4d83974ee96ddbd4f972bab5795cf0ed5085f"
  val temp = Bits(256 bits)
  io.output := random

  temp := random ^ (random |<<64) ^ (random |>> 28) ^ (random |<< 120) ^ (random |>> 23)

  when(io.next) {
    random := temp
    io.output := temp
  } .elsewhen(io.write) {
    random := io.input
  }

}
