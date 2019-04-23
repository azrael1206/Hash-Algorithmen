

import spinal.core._
import spinal.lib._
import vexriscv.demo.{SimpleBus, SimpleBusConfig}


class HashComputation(simpleBusConfig : SimpleBusConfig) extends Component{

  //IO for the Haraka hash Algoritmus
  val io = new Bundle {
    val ready = out Bool
    val go = in Bool
    val reset = in Bool
    val valid = out Bool
    val bus = slave(SimpleBus(simpleBusConfig))
  }

  //This store the 512 Bit value
  val MReg = Vec (Reg (Bits (32 bits)) init 0, 16)
  //this store the final hash Value
  val hashReg = Vec (Reg (Bits (32 bits)) init 0, 8)
  //a Flag that say the hash can be startet
  val go = Reg (Bool) init False
  //
  val ms = new MessageSchedule
  //counter for the rounds
  val counter = Reg (UInt(7 bits)) init 0
  //stores the sha2 const
  val const = new SHA2const
  //Stores the working values
  val working = Vec( Reg (Bits (32 bits)), 8 )
  // a temporarie Variable
  val temp = Vec ( Bits (32 bits), 2)
  //This say if the hash calculation is ready
  val ready = Reg(Bool) init True
  val valid = Reg(Bool) init False
  val start = Reg(Bool) init False
  val data = Reg(Bits(32 bits)) init 0

  //Set values for the signals
  ms.io.M := MReg
  ms.io.start := go
  ms.io.round := counter
  const.io.selector := counter.resized
  ms.io.reset := False
  io.bus.cmd.ready := ready
  io.bus.rsp.data := data
  //io.ready := ready
  //Set the output with the values stored in hashReg

  //Set the ready signal with the value stored in ready
  io.bus.rsp.valid := valid
  //Clear the temp signal
  temp(0).clearAll()
  temp(1).clearAll()

  valid := False
  //io.valid := start
  //When the go signal is true set the register go to true
 /* when(io.go) {
    go := True
    ready := False
  }

  //When the reset signal is true set the used register to standard values
  when (io.reset) {

    hashReg(0) := B"x6a09e667"
    hashReg(1) := B"xbb67ae85"
    hashReg(2) := B"x3c6ef372"
    hashReg(3) := B"xa54ff53a"
    hashReg(4) := B"x510e527f"
    hashReg(5) := B"x9b05688c"
    hashReg(6) := B"x1f83d9ab"
    hashReg(7) := B"x5be0cd19"
    counter := 0
    go := False
    ready := True

    //this calculate the sha2 hash
  }
   */when (go) {

    //when the counter reached 64 the go goes to false and the hash value is set to hashReg
    when (counter === 64) {
      go := False
      ready := True
      hashReg(0) := (hashReg(0).asUInt + working(0).asUInt).asBits
      hashReg(1) := (hashReg(1).asUInt + working(1).asUInt).asBits
      hashReg(2) := (hashReg(2).asUInt + working(2).asUInt).asBits
      hashReg(3) := (hashReg(3).asUInt + working(3).asUInt).asBits
      hashReg(4) := (hashReg(4).asUInt + working(4).asUInt).asBits
      hashReg(5) := (hashReg(5).asUInt + working(5).asUInt).asBits
      hashReg(6) := (hashReg(6).asUInt + working(6).asUInt).asBits
      hashReg(7) := (hashReg(7).asUInt + working(7).asUInt).asBits
      counter := 0

    } otherwise {
      temp(0) := (working(7).asUInt + (working(4).rotateRight(6) ^ working(4).rotateRight(11) ^ working(4).rotateRight(25)).asUInt +
        ((working(4) & working(5)) ^ (~working(4) & working(6))).asUInt + const.io.output.asUInt + ms.io.output.asUInt).asBits
      temp(1) := ((working(0).rotateRight(2) ^ working(0).rotateRight(13) ^ working(0).rotateRight(22)).asUInt +
        ((working(0) & working(1)) ^ (working(0) & working (2)) ^ (working(1) & working(2))).asUInt).asBits
      working(0) := (temp(0).asUInt + temp(1).asUInt).asBits
      working(1) := working(0)
      working(2) := working(1)
      working(3) := working(2)
      working(4) := (working(3).asUInt + temp(0).asUInt).asBits
      working(5) := working(4)
      working(6) := working(5)
      working(7) := working(6)
      counter := counter + 1

    }

  }elsewhen(io.bus.cmd.valid & io.bus.cmd.wr) {
    switch(io.bus.cmd.address(log2Up(19 * 4) downto 0)) {
      for (i <- 0 until 16) {
        is(i * 4) {
          MReg(i) := io.bus.cmd.data
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
        hashReg(0) := B"x6a09e667"
        hashReg(1) := B"xbb67ae85"
        hashReg(2) := B"x3c6ef372"
        hashReg(3) := B"xa54ff53a"
        hashReg(4) := B"x510e527f"
        hashReg(5) := B"x9b05688c"
        hashReg(6) := B"x1f83d9ab"
        hashReg(7) := B"x5be0cd19"
        counter := 0
        go := False
        ready := True
        ms.io.reset := True

      }
    }


  }elsewhen(io.bus.cmd.valid){
    switch(io.bus.cmd.address(log2Up(8 * 4) downto 0)) {
      for (i <- 0 until 8) {
        is(i * 4) {
         data := hashReg(i)
        }
      }
    }
    valid := True
  } otherwise {
    working(0) := hashReg(0)
    working(1) := hashReg(1)
    working(2) := hashReg(2)
    working(3) := hashReg(3)
    working(4) := hashReg(4)
    working(5) := hashReg(5)
    working(6) := hashReg(6)
    working(7) := hashReg(7)

  }


}
