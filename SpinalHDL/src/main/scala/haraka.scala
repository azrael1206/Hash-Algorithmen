

import spinal.core._
import spinal.lib.fsm._
import spinal.lib.slave
import vexriscv.demo.{SimpleBus, SimpleBusConfig}

class haraka(simpleBusConfig : SimpleBusConfig) extends Component {

  val io = new Bundle {
    val go = in Bool
    val reset = in Bool
    val ready = out Bool
    val valid = out Bool
    val bus = slave(SimpleBus(simpleBusConfig))
  }


  val haraka512 = Vec(Reg(Bits()) init 0, 16)
  val haraka512temp = Vec(Reg(Bits ()) init 0, 8)
  val harakaout256 = Vec(Reg(Bits()) init 0, 8)
  val counter = Reg(UInt (4 bits)) init 0
  val counter2 = Reg(UInt(1 bits)) init 0
  val go = Reg(Bool) init False
  val aes1 = new aes_round()
  val aes2 = new aes_round()
  val ready = Reg(Bool) init True
  val valid = Reg(Bool) init False
  val start = Reg(Bool) init False
  val data = Reg(Bits(32 bits)) init 0

  io.ready := ready
  io.bus.cmd.ready := ready
  io.bus.rsp.data := data
  io.bus.rsp.valid := valid
  valid := False
  io.valid := start
  for (i <- 0 until counter.getBitsWidth) {
    aes1.io.box1(i) := haraka512(i)
    aes1.io.box2(i) := haraka512(i + 4)
    aes2.io.box1(i) := haraka512(i + 8)
    aes2.io.box2(i) := haraka512(i + 12)

  }
  aes1.io.round1 := (counter * 8 + counter2 * 4)(5 downto 0)
  aes1.io.round2 := (counter * 8 + 1 + counter2 * 4)(5 downto 0)
  aes2.io.round1 := (counter * 8 + 2 + counter2 * 4)(5 downto 0)
  aes2.io.round2 := (counter * 8 + 3 + counter2 * 4)(5 downto 0)

  when(io.go) {
    go := True
    ready := False
    haraka512temp(0) := haraka512(2)
    haraka512temp(1) := haraka512(3)
    haraka512temp(2) := haraka512(6)
    haraka512temp(3) := haraka512(7)
    haraka512temp(4) := haraka512(8)
    haraka512temp(5) := haraka512(9)
    haraka512temp(6) := haraka512(12)
    haraka512temp(7) := haraka512(13)


  }.elsewhen (io.reset) {
    ready := True
    go := False
    counter.clearAll()
    counter2.clearAll()


  }.elsewhen(go) {

    when(counter === 5) {
      harakaout256(0):= haraka512(2) ^ haraka512temp(0)
      harakaout256(1):= haraka512(3) ^ haraka512temp(1)
      harakaout256(2):= haraka512(6) ^ haraka512temp(2)
      harakaout256(3):= haraka512(7) ^ haraka512temp(3)
      harakaout256(4):= haraka512(8) ^ haraka512temp(4)
      harakaout256(5):= haraka512(9) ^ haraka512temp(5)
      harakaout256(6):= haraka512(12) ^ haraka512temp(6)
      harakaout256(7):= haraka512(13) ^ haraka512temp(7)
      ready := True
      go := False
      counter2 := 0
      counter := 0

    } otherwise {

      when(counter2 === 0) {
        for (i <- 0 to 3) {
          haraka512(i) := aes1.io.box1out(i)
          haraka512(i + 4) := aes1.io.box2out(i)
          haraka512(i + 8) := aes2.io.box1out(i)
          haraka512(i + 12) := aes2.io.box2out(i)

        }
        counter2 := 1
      } otherwise {
        haraka512(0) := aes1.io.box1out(3)
        haraka512(1) := aes2.io.box1out(3)
        haraka512(2) := aes1.io.box2out(3)
        haraka512(3) := aes2.io.box2out(3)
        haraka512(4) := aes2.io.box1out(0)
        haraka512(5) := aes1.io.box1out(0)
        haraka512(6) := aes2.io.box2out(0)
        haraka512(7) := aes1.io.box2out(0)
        haraka512(8) := aes2.io.box1out(1)
        haraka512(9) := aes1.io.box1out(1)
        haraka512(10) := aes2.io.box2out(1)
        haraka512(11) := aes1.io.box2out(1)
        haraka512(12) := aes1.io.box1out(2)
        haraka512(13) := aes2.io.box1out(2)
        haraka512(14) := aes1.io.box2out(2)
        haraka512(15) := aes2.io.box2out(2)

        counter := counter + 1
        counter2 := 0

      }
    }
  }elsewhen(io.bus.cmd.valid & io.bus.cmd.wr) {
    switch(io.bus.cmd.address(log2Up(16 * 4) downto 0)) {
      for (i <- 0 until 16) {
        is(i * 4) {
          haraka512(i) := io.bus.cmd.data
        }
      }
      is(16 * 4) {
        start := !start
      }
      is(17 * 4) {
        go := True
        ready := False
      }
      is(18 * 4) {
        counter := 0
        go := False
        ready := True
      }
    }


  }elsewhen(io.bus.cmd.valid) {
    switch(io.bus.cmd.address(log2Up(8 * 4) downto 0)) {
      for (i <- 0 until 8) {
        is(i * 4) {
          data := harakaout256(i)
        }
      }
    }
    valid := True


  } otherwise {

  }

}


object haraka {
  def main(args: Array[String]): Unit = {
    SpinalVhdl(new haraka(SimpleBusConfig(32, 32))).printPruned()
  }
}