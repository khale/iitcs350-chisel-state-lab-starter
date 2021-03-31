package aluseq
import chisel3._

class RAMHelper() extends BlackBox {
  val io = IO(new Bundle {
    val clk   = Input(Clock())
    val rIdx  = Input(UInt(6.W))
    val rdata = Output(UInt(32.W))
    val wIdx  = Input(UInt(6.W))
    val wdata = Input(UInt(32.W))
    val wen   = Input(Bool())
  })
}

// AluSequencer
// =========================================
//
// This module simulates a very simple 
// computer---much simpler than the LC-3. All it does is fetches an instruction
// from memory, executes the instruction, and outputs
// the address that it fetched from and the output
// of executing the instruction. It relies on the ALU
// module to execute instructions (which you will also implement). 
// There are only "operate" instructions, there are only immediate operands (literals),
// are instructions are fixed-length, 
// 32-bits long, and have the same instruction encoding, adhering to the
// following format:
//
//  3 bits      8 bits       8 bits     13 bits
// -----------------------------------------------
// | opcode |  operand 1 | operand 2 |  unused   |
// -----------------------------------------------
// 31 (MSB)                   ...                0 (LSB)
//
// 
// You are given a 6-bit, word-addressable memory. Remember that
// that means it:
// * accepts 6-bit addresses
// * outputs word-sized (in this case 32-bit) chunks of data
//
// In a single clock, your implementation should:
// * fetch an instruction from memory starting at address 0
// * decode the instruction
// * send the operands to the ALU and grab the result
// * output the result from the ALU in io.out
// * output the address you most recently fetched from on io.addr
// * increment the address
//
// On reset, your module should start fetching from 0 again (Hint: look at RegInit)
// Once you reach the final address (63), you should not increment any further. In
// that case you can output whatever you'd like.
//
class Top extends Module {
  val io = IO(new Bundle {
    val out  = Output(UInt(8.W)) 
    val addr = Output(UInt(6.W))
  })

  // You should go implement and test this module first (ALU.scala)
  val alu = Module(new ALU)

  // This memory is simulated from C++
  // via a Verilog adapter
  val mem = Module(new RAMHelper)

  // we wire up the memory here.
  // HINTS: 
  // * you'll need to wire up mem.io.rIdx to the address you want to fetch from
  // * You'll have to use the memory's output read port (mem.io.rdata) somewhere
  mem.io.clk   := clock
  mem.io.wIdx  := DontCare
  mem.io.wdata := DontCare
  mem.io.wen   := false.B


  // TODO: Fill me in
  // Hints:
  // - Consider an instruction register (IR)
  // - Make sure you know how to use RegInit
  // - Make sure you're using mem.io.rdata
  // - make sure you've got somewhere to store the address you're going
  //   fetch from
  // - learn how to use bit extraction in Chisel for your instruction decoding (thing(10,8) means give me
  //   bits 10, 9, and 8)


  // TODO: change these! You need to output something useful
  io.out  := DontCare
  io.addr := DontCare

}

object SimMain extends App {
  Driver.execute(args, () => new Top)
}

