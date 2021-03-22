package shiftreg

import chisel3._
import chisel3.util._

// A Basic shift register. It should:
// - hold a value of 0 on reset
// - output the current value every clock cycle (on io.out)
// - shift the most significant (left-most) bit
//   off every cycle
// - shift in the current input value (io.in) every cycle
class ShiftReg(n: Int) extends Module {
  val io = IO(new Bundle {
      val en  = Input(Bool())
      val in  = Input(UInt(1.W))
      val out = Output(UInt(n.W))
  })



  // TODO: fill me in
  // HINT: 
  // - consider using the Chisel-provided Cat() function 
  // - It's a shift "register"...
  

  // TODO: change this. Output something real
  io.out := DontCare


}
