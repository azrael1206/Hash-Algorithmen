



import spinal.core._
import spinal.lib.slave

class KeccakPer2() extends Component{

  val io = new Bundle {
    val round = slave(KeccakPer())
  }
  noIoPrefix()

  //Signale die gebraucht werden
  val thetain = Vec(Vec(Bits (64 bits), 5), 5)
  val thetaout = Vec(Vec(Bits (64 bits), 5), 5)
  val piin = Vec(Vec(Bits (64 bits), 5), 5)
  val piout = Vec(Vec(Bits (64 bits), 5), 5)
  val rhoin = Vec(Vec(Bits (64 bits), 5), 5)
  val rhoout = Vec(Vec(Bits (64 bits), 5), 5)
  val chiin = Vec(Vec(Bits (64 bits), 5), 5)
  val chiout = Vec(Vec(Bits (64 bits), 5), 5)
  val iotain = Vec(Vec(Bits (64 bits), 5), 5)
  val iotaout = Vec(Vec(Bits (64 bits), 5), 5)
  val sumsheet = Vec(Bits (64 bits), 5)


  //Verknüpfung der einzelnen Stufen
  thetain := io.round.round_in
  piin := rhoout
  rhoin := thetaout
  chiin := piout
  iotain := chiout
  io.round.round_out := iotaout

  //Chi Berechnung
  for (y <- 0 to 4) {
    for (x <- 0 to 2) {
      chiout(y)(x) := chiin(y)(x) ^ (~chiin(y)(x+1) & chiin(y)(x+2))
    }
    chiout(y)(3) := chiin(y)(3) ^ (~chiin(y)(4) & chiin(y)(0))
    chiout(y)(4) := chiin(y)(4) ^ (~chiin(y)(0) & chiin(y)(1))
  }

  //Theta Berechnung
  for (x <- 0 to 4) {
      sumsheet(x) := thetain(0)(x) ^ thetain(1)(x) ^ thetain(2)(x) ^ thetain(3)(x) ^ thetain(4)(x)
  }

  for (y <- 0 to 4) {
    for (x <- 1 to 3) {
      thetaout(y)(x) := (thetain(y)(x)(63 downto 1) ^ sumsheet(x-1)(63 downto 1) ^ sumsheet(x+1)(62 downto 0)) ## (thetain(y)(x)(0) ^ sumsheet(x-1)(0) ^ sumsheet(x+1)(63))
    }
    thetaout(y)(0) := (thetain(y)(0)(63 downto 1) ^ sumsheet(4)(63 downto 1) ^ sumsheet(1)(62 downto 0)) ## (thetain(y)(0)(0) ^ sumsheet(4)(0) ^ sumsheet(1)(63))
    thetaout(y)(4) := (thetain(y)(4)(63 downto 1) ^ sumsheet(3)(63 downto 1) ^ sumsheet (0)(62 downto 0)) ## (thetain(y)(4)(0) ^ sumsheet(3)(0) ^ sumsheet(0)(63))
  }

  //Pi Berechnung

  for (y <- 0 to 4) {
    for (x <- 0 to 4) {
      piout((2 * x + 3 * y) % 5)(y) := piin(y)(x)
    }
  }

  //Rho Berechnung
  rhoout(0)(0) := rhoin(0)(0)
  rhoout(0)(1) := rhoin(0)(1)(62 downto 0) ## rhoin(0)(1)(63)
  rhoout(0)(2) := rhoin(0)(2)(1 downto 0) ## rhoin(0)(2)(63 downto 2)
  rhoout(0)(3) := rhoin(0)(3)(35 downto 0) ## rhoin(0)(3)(63 downto 36)
  rhoout(0)(4) := rhoin(0)(4)(36 downto 0) ## rhoin(0)(4)(63 downto 37)
  rhoout(1)(0) := rhoin(1)(0)(27 downto 0) ## rhoin(1)(0)(63 downto 28)
  rhoout(1)(1) := rhoin(1)(1)(19 downto 0) ## rhoin(1)(1)(63 downto 20)
  rhoout(1)(2) := rhoin(1)(2)(57 downto 0) ## rhoin(1)(2)(63 downto 58)
  rhoout(1)(3) := rhoin(1)(3)(8 downto 0) ## rhoin(1)(3)(63 downto 9)
  rhoout(1)(4) := rhoin(1)(4)(43 downto 0) ## rhoin(1)(4)(63 downto 44)
  rhoout(2)(0) := rhoin(2)(0)(60 downto 0) ## rhoin(2)(0)(63 downto 61)
  rhoout(2)(1) := rhoin(2)(1)(53 downto 0) ## rhoin(2)(1)(63 downto 54)
  rhoout(2)(2) := rhoin(2)(2)(20 downto 0) ## rhoin(2)(2)(63 downto 21)
  rhoout(2)(3) := rhoin(2)(3)(38 downto 0) ## rhoin(2)(3)(63 downto 39)
  rhoout(2)(4) := rhoin(2)(4)(24 downto 0) ## rhoin(2)(4)(63 downto 25)
  rhoout(3)(0) := rhoin(3)(0)(22 downto 0) ## rhoin(3)(0)(63 downto 23)
  rhoout(3)(1) := rhoin(3)(1)(18 downto 0) ## rhoin(3)(1)(63 downto 19)
  rhoout(3)(2) := rhoin(3)(2)(48 downto 0) ## rhoin(3)(2)(63 downto 49)
  rhoout(3)(3) := rhoin(3)(3)(42 downto 0) ## rhoin(3)(3)(63 downto 43)
  rhoout(3)(4) := rhoin(3)(4)(55 downto 0) ## rhoin(3)(4)(63 downto 56)
  rhoout(4)(0) := rhoin(4)(0)(45 downto 0) ## rhoin(4)(0)(63 downto 46)
  rhoout(4)(1) := rhoin(4)(1)(61 downto 0) ## rhoin(4)(1)(63 downto 62)
  rhoout(4)(2) := rhoin(4)(2)(2 downto 0) ## rhoin(4)(2)(63 downto 3)
  rhoout(4)(3) := rhoin(4)(3)(7 downto 0) ## rhoin(4)(3)(63 downto 8)
  rhoout(4)(4) := rhoin(4)(4)(49 downto 0) ## rhoin(4)(4)(63 downto 50)


  //Iota Berechnung
  for (y <- 1 to 4) {
    for (x <- 0 to 4) {
      iotaout(y)(x):= iotain(y)(x)
    }
  }
  for (x <- 1 to 4) {
    iotaout(0)(x) := iotain(0)(x)
  }
  iotaout(0)(0) := iotain(0)(0) ^ io.round.round_constant_signal

}

