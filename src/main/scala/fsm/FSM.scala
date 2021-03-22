package fsm
import chisel3._
import chisel3.util._
import chisel3.experimental.ChiselEnum

// A FSM that accepts all binary strings 
// containing "1010". See the tester (src/test/scala/fsm/FSMTester.scala) for examples
class FSM extends Module {
  val io = IO(new Bundle {
    val en     = Input(Bool())
    val input  = Input(UInt(1.W))
    val accept = Output(Bool())
  })

  // TODO: Fill me in!
  // Hints:
  // - you can use enums for state names using this syntax:
  // object State extends ChiselEnum {
  //   val StateOne, StateTwo, StateThree = Value
  // } 
  // - You can use this as follows: somewireorreg := State.SeenOne
  // - Make sure to consider all input possibilities
  //

  // TODO: change this
  io.accept := false.B
}
