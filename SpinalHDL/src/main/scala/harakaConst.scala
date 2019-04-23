

import spinal.core._

case class sBox () extends Component {

  val io = new Bundle {
    val output1 = out Bits ()
    val address1 = in UInt ()
    val output2 = out Bits ()
    val address2 = in UInt ()

  }

  val s = Mem(Bits(8 bits), Array(B"x63", B"x7c",
    B"x77", B"x7b",
    B"xf2", B"x6b",
    B"x6f", B"xc5",
    B"x30", B"x01",
    B"x67", B"x2b",
    B"xfe", B"xd7",
    B"xab", B"x76",
    B"xca", B"x82",
    B"xc9", B"x7d",
    B"xfa", B"x59",
    B"x47", B"xf0",
    B"xad", B"xd4",
    B"xa2", B"xaf",
    B"x9c", B"xa4",
    B"x72", B"xc0",
    B"xb7", B"xfd",
    B"x93", B"x26",
    B"x36", B"x3f",
    B"xf7", B"xcc",
    B"x34", B"xa5",
    B"xe5", B"xf1",
    B"x71", B"xd8",
    B"x31", B"x15",
    B"x04", B"xc7",
    B"x23", B"xc3",
    B"x18", B"x96",
    B"x05", B"x9a",
    B"x07", B"x12",
    B"x80", B"xe2",
    B"xeb", B"x27",
    B"xb2", B"x75",
    B"x09", B"x83",
    B"x2c", B"x1a",
    B"x1b", B"x6e",
    B"x5a", B"xa0",
    B"x52", B"x3b",
    B"xd6", B"xb3",
    B"x29", B"xe3",
    B"x2f", B"x84",
    B"x53", B"xd1",
    B"x00", B"xed",
    B"x20", B"xfc",
    B"xb1", B"x5b",
    B"x6a", B"xcb",
    B"xbe", B"x39",
    B"x4a", B"x4c",
    B"x58", B"xcf",
    B"xd0", B"xef",
    B"xaa", B"xfb",
    B"x43", B"x4d",
    B"x33", B"x85",
    B"x45", B"xf9",
    B"x02", B"x7f",
    B"x50", B"x3c",
    B"x9f", B"xa8",
    B"x51", B"xa3",
    B"x40", B"x8f",
    B"x92", B"x9d",
    B"x38", B"xf5",
    B"xbc", B"xb6",
    B"xda", B"x21",
    B"x10", B"xff",
    B"xf3", B"xd2",
    B"xcd", B"x0c",
    B"x13", B"xec",
    B"x5f", B"x97",
    B"x44", B"x17",
    B"xc4", B"xa7",
    B"x7e", B"x3d",
    B"x64", B"x5d",
    B"x19", B"x73",
    B"x60", B"x81",
    B"x4f", B"xdc",
    B"x22", B"x2a",
    B"x90", B"x88",
    B"x46", B"xee",
    B"xb8", B"x14",
    B"xde", B"x5e",
    B"x0b", B"xdb",
    B"xe0", B"x32",
    B"x3a", B"x0a",
    B"x49", B"x06",
    B"x24", B"x5c",
    B"xc2", B"xd3",
    B"xac", B"x62",
    B"x91", B"x95",
    B"xe4", B"x79",
    B"xe7", B"xc8",
    B"x37", B"x6d",
    B"x8d", B"xd5",
    B"x4e", B"xa9",
    B"x6c", B"x56",
    B"xf4", B"xea",
    B"x65", B"x7a",
    B"xae", B"x08",
    B"xba", B"x78",
    B"x25", B"x2e",
    B"x1c", B"xa6",
    B"xb4", B"xc6",
    B"xe8", B"xdd",
    B"x74", B"x1f",
    B"x4b", B"xbd",
    B"x8b", B"x8a",
    B"x70", B"x3e",
    B"xb5", B"x66",
    B"x48", B"x03",
    B"xf6", B"x0e",
    B"x61", B"x35",
    B"x57", B"xb9",
    B"x86", B"xc1",
    B"x1d", B"x9e",
    B"xe1", B"xf8",
    B"x98", B"x11",
    B"x69", B"xd9",
    B"x8e", B"x94",
    B"x9b", B"x1e",
    B"x87", B"xe9",
    B"xce", B"x55",
    B"x28", B"xdf",
    B"x8c", B"xa1",
    B"x89", B"x0d",
    B"xbf", B"xe6",
    B"x42", B"x68",
    B"x41", B"x99",
    B"x2d", B"x0f",
    B"xb0", B"x54",
    B"xbb", B"x16"))


  for (i <- 0 until 4) {

    io.output1(i * 8 + 7 downto i * 8) := s(io.address1(i * 8 + 7 downto i * 8))
    io.output2(i * 8 + 7 downto i * 8) := s(io.address2(i * 8 + 7 downto i * 8))
  }


}

case class harakaConst () extends Component {

  val io = new Bundle {
    val address1 = in UInt()
    val address2 = in UInt()
    val output1 = out Bits()
    val output2 = out Bits()
  }
  val temp1 = Bits()
  val temp2 = Bits()

  val hconst = Mem(Bits(128 bits), Array(B"x0684704ce620c00ab2c5fef075817b9d",
  B"x8b66b4e188f3a06b640f6ba42f08f717",
  B"x3402de2d53f28498cf029d609f029114",
  B"x0ed6eae62e7b4f08bbf3bcaffd5b4f79",
  B"xcbcfb0cb4872448b79eecd1cbe397044",
  B"x7eeacdee6e9032b78d5335ed2b8a057b",
  B"x67c28f435e2e7cd0e2412761da4fef1b",
  B"x2924d9b0afcacc07675ffde21fc70b3b",
  B"xab4d63f1e6867fe9ecdb8fcab9d465ee",
  B"x1c30bf84d4b7cd645b2a404fad037e33",
  B"xb2cc0bb9941723bf69028b2e8df69800",
  B"xfa0478a6de6f55724aaa9ec85c9d2d8a",
  B"xdfb49f2b6b772a120efa4f2e29129fd4",
  B"x1ea10344f449a23632d611aebb6a12ee",
  B"xaf0449884b0500845f9600c99ca8eca6",
  B"x21025ed89d199c4f78a2c7e327e593ec",
  B"xbf3aaaf8a759c9b7b9282ecd82d40173",
  B"x6260700d6186b01737f2efd910307d6b",
  B"x5aca45c22130044381c29153f6fc9ac6",
  B"x9223973c226b68bb2caf92e836d1943a",
  B"xd3bf9238225886eb6cbab958e51071b4",
  B"xdb863ce5aef0c677933dfddd24e1128d",
  B"xbb606268ffeba09c83e48de3cb2212b1",
  B"x734bd3dce2e4d19c2db91a4ec72bf77d",
  B"x43bb47c361301b434b1415c42cb3924e",
  B"xdba775a8e707eff603b231dd16eb6899",
  B"x6df3614b3c7559778e5e23027eca472c",
  B"xcda75a17d6de7d776d1be5b9b88617f9",
  B"xec6b43f06ba8e9aa9d6c069da946ee5d",
  B"xcb1e6950f957332ba25311593bf327c1",
  B"x2cee0c7500da619ce4ed0353600ed0d9",
  B"xf0b1a5a196e90cab80bbbabc63a4a350",
  B"xae3db1025e962988ab0dde30938dca39",
  B"x17bb8f38d554a40b8814f3a82e75b442",
  B"x34bb8a5b5f427fd7aeb6b779360a16f6",
  B"x26f65241cbe5543843ce5918ffbaafde",
  B"x4ce99a54b9f3026aa2ca9cf7839ec978",
  B"xae51a51a1bdff7be40c06e2822901235",
  B"xa0c1613cba7ed22bc173bc0f48a659cf",
  B"x756acc03022882884ad6bdfde9c59da1"
  ))
  temp1 := hconst(io.address1)
  temp2 := hconst(io.address2)
  io.output1 := temp1(7 downto 0) ## temp1(15 downto 8) ## temp1(23 downto 16) ## temp1(31 downto 24) ## temp1(39 downto 32) ## temp1(47 downto 40) ##
                temp1(55 downto 48) ## temp1(63 downto 56) ## temp1(71 downto 64) ## temp1(79 downto 72) ## temp1(87 downto 80) ## temp1(95 downto 88) ##
                temp1(103 downto 96) ## temp1(111 downto 104) ## temp1(119 downto 112) ## temp1(127 downto 120)
  io.output2 := temp2(7 downto 0) ## temp2(15 downto 8) ## temp2(23 downto 16) ## temp2(31 downto 24) ## temp2(39 downto 32) ## temp2(47 downto 40) ##
    temp2(55 downto 48) ## temp2(63 downto 56) ## temp2(71 downto 64) ## temp2(79 downto 72) ## temp2(87 downto 80) ## temp2(95 downto 88) ##
    temp2(103 downto 96) ## temp2(111 downto 104) ## temp2(119 downto 112) ## temp2(127 downto 120)
}
