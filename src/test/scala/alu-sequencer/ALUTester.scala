package aluseq

import chisel3._
import chiseltest._
import chiseltest.experimental.TestOptionBuilder._
import chiseltest.internal.VerilatorBackendAnnotation
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


// TODO: other features

/**
  * This is a trivial example of how to run this Specification
  * From within sbt use:
  * {{{
  * testOnly aluseq.ALUTester
  * }}}
  * From a terminal shell use:
  * {{{
  * sbt 'testOnly aluseq.ALUTester'
  * }}}
  */
class ALUTester extends AnyFlatSpec with ChiselScalatestTester with Matchers {

  behavior of "ALU"

  val annos = Seq(VerilatorBackendAnnotation)

  it should "adds values properly" in {
    test(new ALU()).withAnnotations(annos) { c => 
      for (i <- 0 to 255) {
        for (j <- 0 to 255) {
          //println(s"A: $i B: $j (ADD)")
          c.io.op.poke(0.U)
          c.io.in_a.poke(i.U)
          c.io.in_b.poke(j.U)
          val sum = (i+j) % 256
          c.io.out.expect(sum.U)
        }
      }
    }
  }

}
