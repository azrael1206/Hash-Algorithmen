

import spinal.core._


case class aes_round () extends Component {

  val io = new Bundle{
    val round1 = in UInt()
    val round2 = in UInt()
    val box1 = in Vec(Bits(), 4)
    val box2 = in Vec(Bits(), 4)
    val box1out = out Vec(Bits(), 4)
    val box2out = out Vec(Bits(), 4)
  }

  val sB01 = new sBox()
  val sB02 = new sBox()
  val sB03 = new sBox()
  val sB04 = new sBox()

  val hconst = new harakaConst()

  val box1s = Vec(Bits(), 4)
  val box2s = Vec(Bits(), 4)
  val box1ss = Vec(Bits(), 4)
  val box2ss= Vec(Bits(), 4)
  val box1ssm = Vec(Bits(), 4)
  val box2ssm = Vec(Bits(), 4)
  val temp1 = Vec(Bits(), 4)
  val temp2 = Vec(Bits(), 4)

  val mix11 = new MixColum()
  val mix21 = new MixColum()
  val mix31 = new MixColum()
  val mix41 = new MixColum()

  val mix12 = new MixColum()
  val mix22 = new MixColum()
  val mix32 = new MixColum()
  val mix42 = new MixColum()


  sB01.io.address1 := io.box1(0).asUInt
  sB01.io.address2 := io.box2(0).asUInt
  box1s(0) := sB01.io.output1
  box2s(0) := sB01.io.output2
  sB02.io.address1 := io.box1(1).asUInt
  sB02.io.address2 := io.box2(1).asUInt
  box1s(1) := sB02.io.output1
  box2s(1) := sB02.io.output2
  sB03.io.address1 := io.box1(2).asUInt
  sB03.io.address2 := io.box2(2).asUInt
  box1s(2) := sB03.io.output1
  box2s(2) := sB03.io.output2
  sB04.io.address1 := io.box1(3).asUInt
  sB04.io.address2 := io.box2(3).asUInt
  box1s(3) := sB04.io.output1
  box2s(3) := sB04.io.output2


  box1ss(0) := box1s(0)(31 downto 24) ## box1s(1)(23 downto 16) ## box1s(2)(15 downto 8) ## box1s(3)(7 downto 0)
  box1ss(1) := box1s(1)(31 downto 24) ## box1s(2)(23 downto 16) ## box1s(3)(15 downto 8) ## box1s(0)(7 downto 0)
  box1ss(2) := box1s(2)(31 downto 24) ## box1s(3)(23 downto 16) ## box1s(0)(15 downto 8) ## box1s(1)(7 downto 0)
  box1ss(3) := box1s(3)(31 downto 24) ## box1s(0)(23 downto 16) ## box1s(1)(15 downto 8) ## box1s(2)(7 downto 0)
  box2ss(0) := box2s(0)(31 downto 24) ## box2s(1)(23 downto 16) ## box2s(2)(15 downto 8) ## box2s(3)(7 downto 0)
  box2ss(1) := box2s(1)(31 downto 24) ## box2s(2)(23 downto 16) ## box2s(3)(15 downto 8) ## box2s(0)(7 downto 0)
  box2ss(2) := box2s(2)(31 downto 24) ## box2s(3)(23 downto 16) ## box2s(0)(15 downto 8) ## box2s(1)(7 downto 0)
  box2ss(3) := box2s(3)(31 downto 24) ## box2s(0)(23 downto 16) ## box2s(1)(15 downto 8) ## box2s(2)(7 downto 0)


  mix11.io.input := box1ss(0)
  box1ssm(0) := mix11.io.output
  mix21.io.input := box1ss(1)
  box1ssm(1) := mix21.io.output
  mix31.io.input := box1ss(2)
  box1ssm(2) := mix31.io.output
  mix41.io.input := box1ss(3)
  box1ssm(3) := mix41.io.output
  mix12.io.input := box2ss(0)
  box2ssm(0) := mix12.io.output
  mix22.io.input := box2ss(1)
  box2ssm(1) := mix22.io.output
  mix32.io.input := box2ss(2)
  box2ssm(2) := mix32.io.output
  mix42.io.input := box2ss(3)
  box2ssm(3) := mix42.io.output

  hconst.io.address1 := io.round1
  hconst.io.address2 := io.round2

  for (i <- 0 to 3) {
    temp1(i) := hconst.io.output1(127 - i * 32 downto 127 - i * 32 - 31)
    io.box1out(i) := box1ssm(i) ^ temp1(i)
    temp2(i) := hconst.io.output2(127 - i * 32 downto 127 - i * 32 - 31)
    io.box2out(i) := box2ssm(i) ^ temp2(i)
  }
}

case class MixColum () extends Component {

  val io = new Bundle {
    val input = in Bits(32 bits)
    val output = out Bits(32 bits)
  }

  val w1 = Bits(8 bits)
  val w2 = Bits(8 bits)
  val w3 = Bits(8 bits)
  val w4 = Bits(8 bits)

  w1 := io.input(31 downto 24)
  w2 := io.input(23 downto 16)
  w3 := io.input(15 downto 8)
  w4 := io.input(7 downto 0)

  io.output(31 downto 24) := ((w1(6 downto 0) ## B"1") ^ (B"x1b" & B(8 bits, default -> w1(7)))) ^
                            (((w2(6 downto 0) ## B"1") ^ (B"x1b" & B(8 bits, default -> w2(7)))) ^ w2) ^ w3  ^ w4
  io.output(23 downto 16) := w1 ^ ((w2(6 downto 0) ## B"1") ^ (B"x1b" & B(8 bits, default -> w2(7)))) ^
                            (((w3(6 downto 0) ## B"1") ^ (B"x1b" & B(8 bits, default -> w3(7)))) ^ w3) ^ w4
  io.output(15 downto 8) := w1 ^ w2 ^ ((w3(6 downto 0) ## B"1") ^ (B"x1b" & B(8 bits, default -> w3(7)))) ^
                              (((w4(6 downto 0) ## B"1") ^ (B"x1b" & B(8 bits, default -> w4(7)))) ^ w4)
  io.output(7 downto 0) := (((w1(6 downto 0) ## B"1") ^ (B"x1b" & B(8 bits, default -> w1(7)))) ^ w1) ^ w2 ^ w3 ^
                             ((w4(6 downto 0) ## B"1") ^ (B"x1b" & B(8 bits, default -> w4(7))))



}


object aes_round {
  def main(args: Array[String]): Unit = {
    SpinalVhdl(new aes_round).printPruned()
  }
}