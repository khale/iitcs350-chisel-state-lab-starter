package aluseq

import chisel3._
import chisel3.util._


object ALU {
  val width  = 3
  val OP_ADD = 0.U(width.W)
  val OP_AND = 1.U(width.W)
  val OP_OR  = 2.U(width.W)
  val OP_XOR = 3.U(width.W)
}

import ALU._
/**
  * Implements a simple 8-bit ALU
  * It only supports four binary operations: ADD, AND, OR, XOR
  */
class ALU extends Module {
  val io = IO(new Bundle {
    val op   = Input(UInt(width.W))
    val in_a = Input(UInt(8.W))
    val in_b = Input(UInt(8.W))
    val out  = Output(UInt(8.W))
  })

  io.out := DontCare

  // TODO: Fill me in!
  // Hints: 
  // - use the pre-defined opcodes above (OP_ADD etc)
  // - use Chisel switch or when statements

}
