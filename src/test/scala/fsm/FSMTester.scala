package fsm

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
  * testOnly fsm.FSMTester
  * }}}
  * From a terminal shell use:
  * {{{
  * sbt 'testOnly fsm.FSMTester'
  * }}}
  */
class FSMTester extends AnyFlatSpec with ChiselScalatestTester with Matchers {

  behavior of "FSM"

  it should "accept '1010'" in {
    test(new FSM()) { c => 
      c.io.en.poke(true.B) // enable it
      val input = List(1,0,1,0)
      for (i <- input) {
        c.io.input.poke(i.asUInt(1.W))
        c.io.accept.expect(false.B)
        c.clock.step(1)
      }
      c.io.accept.expect(true.B)
    }
  }
  it should "reject '1111'" in {
    test(new FSM()) { c => 
      c.io.en.poke(true.B) // enable it
      val input = List(1,1,1,1)
      for (i <- input) {
        c.io.input.poke(i.asUInt(1.W))
        c.io.accept.expect(false.B)
        c.clock.step(1)
      }
      c.io.accept.expect(false.B)
    }
  }
  it should "accept '0010101'" in {
    test(new FSM()) { c => 
      c.io.en.poke(true.B) // enable it
      val input = List(0,0,1,0,1,0,1)
      for (i <- input) {
        c.io.input.poke(i.asUInt(1.W))
        c.clock.step(1)
      }
      c.io.accept.expect(true.B)
    }
  }
  it should "reject '1011'" in {
    test(new FSM()) { c => 
      c.io.en.poke(true.B) // enable it
      val input = List(1,0,1,1)
      for (i <- input) {
        c.io.input.poke(i.asUInt(1.W))
        c.io.accept.expect(false.B)
        c.clock.step(1)
      }
      c.io.accept.expect(false.B)
    }
  }
  it should "accept '10110101'" in {
    test(new FSM()) { c => 
      c.io.en.poke(true.B) // enable it
      val input = List(1,0,1,1,0,1,0,1)
      for (i <- input) {
        c.io.input.poke(i.asUInt(1.W))
        c.clock.step(1)
      }
      c.io.accept.expect(true.B)
    }
  }
}
