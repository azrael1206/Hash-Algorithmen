



import spinal.core._
import spinal.lib.IMasterSlave

case class KeccakPer() extends Bundle with IMasterSlave {

  val round_in = Vec(Vec(Bits(64 bits), 5), 5)
  val round_out = Vec(Vec(Bits(64 bits), 5), 5)
  val round_constant_signal = Bits(64 bits)

  override def asMaster(): Unit = {
    out(round_in, round_constant_signal)
    in(round_out)
  }
}

  case class KeccakRsp() extends Bundle with IMasterSlave {

    val valid = Bool
    val data = Bits (width = 32 bits)

    override def asMaster(): Unit = {

      out (valid, data)

    }
  }

  case class KeccakCmd() extends Bundle with IMasterSlave {

    val valid = Bool
    val data = Bits(width = 32 bits)
    val ready = Bool
    val address = UInt(width = 32 bits)
    val wr = Bool
    val mask = Bits(width = 4 bits)


    override def asMaster(): Unit = {

      in(valid, data, address, wr, mask)
      out(ready)

    }

  }

object Globals {
  def globals(a: Bits): Bits = {
    val returnv = Bits(64 bits)
    val const = Mem(Bits(64 bits), Array(B"x0000000000000001", B"x0000000000008082", B"x800000000000808A",
      B"x8000000080008000", B"x000000000000808B", B"x0000000080000001", B"x8000000080008081",
      B"x8000000000008009", B"x000000000000008A", B"x0000000000000088", B"x0000000080008009",
      B"x000000008000000A", B"x000000008000808B", B"x800000000000008B", B"x8000000000008089",
      B"x8000000000008003", B"x8000000000008002", B"x8000000000000080", B"x000000000000800A",
      B"x800000008000000A", B"x8000000080008081", B"x8000000000008080", B"x0000000080000001",
      B"x8000000080008008"))

    returnv := const(a.asUInt)

    return returnv
  }
}