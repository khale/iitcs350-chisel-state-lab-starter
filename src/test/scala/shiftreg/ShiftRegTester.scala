package shiftreg

import chisel3._
import chiseltest._
import chiseltest.experimental.TestOptionBuilder._
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers



/**
  * This is a trivial example of how to run this Specification
  * From within sbt use:
  * {{{
  * testOnly shiftreg.ShiftRegTester
  * }}}
  * From a terminal shell use:
  * {{{
  * sbt 'testOnly shiftreg.ShiftRegTester'
  * }}}
  */
class ShiftRegTester extends AnyFlatSpec with ChiselScalatestTester with Matchers {

  behavior of "ShiftReg"

  it should "be able to shift in a single '1' from the left" in {
    test(new ShiftReg(2)) { c => 
      c.io.en.poke(true.B) // enable it
      c.io.in.poke(1.U(1.W))
      c.clock.step(1)
      c.io.out.expect("b01".U)
    }
  }

  it should "8-bit shifter should produce '10101010'" in {
    test(new ShiftReg(8)) { c => 
      c.io.en.poke(true.B) // enable it
      val input = List(1,0,1,0,1,0,1,0)
      for (i <- input) {
        c.io.in.poke(i.asUInt(1.W))
        c.clock.step(1)
      }
      c.io.out.expect("b1010_1010".U)
    }
  }

  it should "2-bit shifter should produce '11'" in {
    test(new ShiftReg(2)) { c => 
      c.io.en.poke(true.B) // enable it
      val input = List(1,1)
      for (i <- input) {
        c.io.in.poke(i.asUInt(1.W))
        c.clock.step(1)
      }
      c.io.out.expect("b11".U)
    }
  }

  it should "32-bit shifter producing all 0s should work" in {
    test(new ShiftReg(32)) { c => 
      c.io.en.poke(true.B) // enable it
      val input = List.fill(32)(0)
      for (i <- input) {
        c.io.in.poke(i.asUInt(1.W))
        c.clock.step(1)
      }
      c.io.out.expect("b0000_0000_0000_0000_0000_0000_0000_0000".U)
    }
  }
  
  it should "64-bit shifter producing all 1s should work" in {
    test(new ShiftReg(64)) { c => 
      c.io.en.poke(true.B) // enable it
      val input = List.fill(64)(1)
      for (i <- input) {
        c.io.in.poke(i.asUInt(1.W))
        c.clock.step(1)
      }
      c.io.out.expect("hffff_ffff_ffff_ffff".U)
    }
  }
}
