



import spinal.core._
import spinal.lib._
import vexriscv.demo.{SimpleBus, SimpleBusConfig}


//Schnittstelle und Logik

class KeccakInt(simpleBusConfig : SimpleBusConfig) extends Component {


  //Schnittstellen Definition für die Kommunikation mit der Aussenwelt
  val io = new Bundle {
    val go = in Bool
    val reset = in Bool
    val ready = out Bool
    val valid = out Bool
    val bus = slave(SimpleBus(simpleBusConfig))

  }

  //Objekt erzeugen mit der Permutation
  val keccak = new KeccakPer2()

  //Hier werden die 1600 Bits gespeichert
  val state = Reg (Bits(1600 bits)) init 0
  //Zähler für die Runden
  val counter = Reg (UInt(5 bits)) init 0
  val counter2 = Reg(UInt(log2Up(34) bits)) init 0
  val data = Reg(Bits(32 bits)) init 0
  val valid = Reg(Bool) init False
  val start = Reg(Bool) init False
  val ready = Reg(Bool) init True

  keccak.io.round.round_constant_signal := Globals.globals(counter.asBits)
  io.bus.cmd.ready := ready
  io.bus.rsp.data := data
  io.ready := ready
  io.bus.rsp.valid := valid
  valid := False
  when(io.go) {
    ready := False
  }



  //Logik für den Schreib und Lesezugriff
  when(io.reset) {
    counter := 0
    state.clearAll()
  } elsewhen(!ready) {
    when (counter <= 23) {
      counter := counter + 1
      //Umwandeln von einem 3 Dimensionales Array in ein 1 Dimensionales Array
      for (y <- 0 to 4) {
        for (x <- 0 to 4) {
          state((y * 320) + (x * 64) + 31 downto (y * 320) + (x * 64)) := keccak.io.round.round_out(y)(x)(31 downto 0)
          state((y * 320) + (x * 64) + 63 downto (y * 320) + (x * 64) + 32) := keccak.io.round.round_out(y)(x)(63 downto 32)
        }
      }


    } elsewhen(counter === 24) {
      ready := True
      counter := 0
    }


  } elsewhen (io.bus.cmd.valid & io.bus.cmd.wr) {
    switch(io.bus.cmd.address(log2Up(34 * 4) downto 0)) {
      for (i <- 0 until 34) {
        is(i * 4) {
          state(i * 32 + 31 downto i * 32) := io.bus.cmd.data
        }
      }
      is(34 * 4) {
        start := !start
      }
      is(35 * 4) {
        ready := False
      }
      is(36 * 4) {
        counter := 0
        state.clearAll()
      }
    }
    counter2 := counter2 + 1
  }elsewhen(io.bus.cmd.valid) {
    switch(io.bus.cmd.address(log2Up(8 * 4) downto 0)) {
      for (i <- 0 until 8) {
        is(i * 4) {
          data := state(i * 32 + 31 downto i * 32)
        }
      }
    }
    valid := True
  }
  //Umwandeln von einem 1 Dimensionales Array in ein 3 Dimensionales Array
  for (y <- 0 to 4) {
    for (x <- 0 to 4) {
      for (i <- 0 to 3) {
        keccak.io.round.round_in(y)(x)(31 downto 0) := state((y * 320) + (x * 64) + 31 downto (y * 320) + (x * 64))
        keccak.io.round.round_in(y)(x)(63 downto 32) := state((y * 320) + (x * 64) + 63 downto (y * 320) + (x * 64) + 32)
      }
    }
  }

}



