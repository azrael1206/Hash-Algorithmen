

import spinal.core._
import spinal.lib._

class MessageSchedule  extends Component {


  val io = new Bundle {
    val round = in UInt(7 bits)
    val output = out Bits(32 bits)
    val M = in Vec(Bits(32 bits), 16)
    val start = in Bool
    val reset = in Bool


  }

  val W = Vec(Reg(Bits(32 bits)) init 0, 16)
  val sigma = Bits(32 bits)

  io.output := (io.round <= 15) ? io.M(io.round(3 downto 0)) | sigma

  when(io.reset) {
    sigma.clearAll()

  for (i <- 0 to 15) {
      W(i).clearAll()
    }
  } elsewhen (io.start) {
      W(0) := io.output
      W(1) := W(2)
      W(2) := W(3)
      W(3) := W(4)
      W(4) := W(5)
      W(5) := W(6)
      W(6) := W(7)
      W(7) := W(8)
      W(8) := W(9)
      W(9) := W(10)
      W(10) := W(11)
      W(11) := W(12)
      W(12) := W(13)
      W(13) := W(14)
      W(14) := W(15)
      W(15) := W(0)

      sigma := ((W(15).rotateRight(17) ^ W(15).rotateRight(19) ^ (W(15) |>> 10)).asUInt + W(10).asUInt +
        (W(2).rotateRight(7) ^ W(2).rotateRight(18) ^ (W(2) |>> 3)).asUInt + W(1).asUInt).asBits
  } otherwise {
      sigma.clearAll()

  }



}
