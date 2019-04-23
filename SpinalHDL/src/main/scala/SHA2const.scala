

import spinal.core._

class SHA2const extends Component {

  val io = new Bundle{
    val selector = in UInt(6 bits)
    val output = out Bits(32 bits)

  }

  val const = Mem(Bits(32 bits), Array (B"x428a2f98", B"x71374491", B"xb5c0fbcf", B"xe9b5dba5",
    B"x3956c25b", B"x59f111f1", B"x923f82a4", B"xab1c5ed5",
    B"xd807aa98", B"x12835b01", B"x243185be", B"x550c7dc3",
    B"x72be5d74", B"x80deb1fe", B"x9bdc06a7", B"xc19bf174",
    B"xe49b69c1", B"xefbe4786", B"x0fc19dc6", B"x240ca1cc",
    B"x2de92c6f", B"x4a7484aa", B"x5cb0a9dc", B"x76f988da",
    B"x983e5152", B"xa831c66d", B"xb00327c8", B"xbf597fc7",
    B"xc6e00bf3", B"xd5a79147", B"x06ca6351", B"x14292967",
    B"x27b70a85", B"x2e1b2138", B"x4d2c6dfc", B"x53380d13",
    B"x650a7354", B"x766a0abb", B"x81c2c92e", B"x92722c85",
    B"xa2bfe8a1", B"xa81a664b", B"xc24b8b70", B"xc76c51a3",
    B"xd192e819", B"xd6990624", B"xf40e3585", B"x106aa070",
    B"x19a4c116", B"x1e376c08", B"x2748774c", B"x34b0bcb5",
    B"x391c0cb3", B"x4ed8aa4a", B"x5b9cca4f", B"x682e6ff3",
    B"x748f82ee", B"x78a5636f", B"x84c87814", B"x8cc70208",
    B"x90befffa", B"xa4506ceb", B"xbef9a3f7", B"xc67178f2"))


    io.output := const(io.selector)

}
