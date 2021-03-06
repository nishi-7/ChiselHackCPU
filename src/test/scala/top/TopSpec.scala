package top

import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import chisel3._
import chiseltest.WriteVcdAnnotation

class TopSpec extends AnyFlatSpec with ChiselScalatestTester with Matchers {
  val words = 2048
  behavior of "Hack Core"
  it should "push constants" in {
    test(new Top("./hack/tests/Const/vm.hack", "./hack/init.bin", words))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.step(500)
        c.io.debug.expect(15.asUInt)
      }
  }

  it should "add (8 + 8 = 16)" in {
    test(new Top("./hack/tests/Add/vm.hack", "./hack/init.bin", words))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.step(500)
        c.io.debug.expect(16.asUInt)
      }
  }

  it should "sub (8 - 7 = 1)" in {
    test(new Top("./hack/tests/Sub/vm.hack", "./hack/init.bin", words))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.step(500)
        c.io.debug.expect(1.asUInt)
      }
  }

  it should "do fib(2) = 1 (fib(0) = 0, fib(1) = 1)" in {
    test(new Top("./hack/tests/Fib2/vm.hack", "./hack/init.bin", words))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.clock.step(2000)
        c.io.debug.expect(1.asUInt)
      }
  }

  it should "do fib(6) = 8 (fib(0) = 0, fib(1) = 1)" in {
    test(new Top("./hack/tests/Fib6/vm.hack", "./hack/init.bin", words))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.clock.step(8000)
        c.io.debug.expect(8.asUInt)
      }
  }

  it should "switch instruction's memory from EBRAM to SPRAM" in {
    test(new Top("./hack/tests/SPRAM1/vm.hack", "./hack/init.bin", words))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.clock.step(5000)
        c.io.debug.expect(1.asUInt)

      }
  }

  behavior of "Uart Rx(12 MHz, 115200 bps)"
  it should "recieve 0b01010101, and write a mem[1024] = 0b01010101" in {
    test(new Top("./hack/tests/Uart1/vm.hack", "./hack/init.bin", words))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.io.cts.poke(true.B)
        c.io.rx.poke(true.B)
        c.clock.step(100)
        c.io.rx.poke(false.B) // start bit
        c.clock.step(104)
        c.io.rx.poke(1.B) // 1 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 2 bit 0
        c.clock.step(104)
        c.io.rx.poke(1.B) // 3 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 4 bit 0
        c.clock.step(104)
        c.io.rx.poke(1.B) // 5 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 6 bit 0
        c.clock.step(104)
        c.io.rx.poke(1.B) // 7 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 8 bit 0
        c.clock.step(104)
        c.io.rx.poke(true.B) // stop bit
        c.clock.step(104)
        // c.io.rxdebug.expect(0x55.asUInt)

        // wait here
        c.clock.step(2000)
        c.io.debug.expect(0x55.asUInt)
      }
  }

  it should "recieve 0b01010101, and status and control register = b00000000000010" in {
    test(new Top("./hack/tests/Uart2/vm.hack", "./hack/init.bin", words))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.io.cts.poke(true.B)
        c.io.rx.poke(true.B)
        c.clock.step(100)
        c.io.rx.poke(false.B) // start bit
        c.clock.step(104)
        c.io.rx.poke(1.B) // 1 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 2 bit 0
        c.clock.step(104)
        c.io.rx.poke(1.B) // 3 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 4 bit 0
        c.clock.step(104)
        c.io.rx.poke(1.B) // 5 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 6 bit 0
        c.clock.step(104)
        c.io.rx.poke(1.B) // 7 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 8 bit 0
        c.clock.step(104)
        c.io.rx.poke(true.B) // stop bit
        c.clock.step(104)
        // c.io.rxdebug.expect(0x55.asUInt)

        // wait here
        c.clock.step(2000)
        c.io.debug.expect(0x2.asUInt)
      }
  }

  it should "recieve 0b01010101, and status and control register = b00000000000001" in {
    test(new Top("./hack/tests/Uart3/vm.hack", "./hack/init.bin", words))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.io.cts.poke(true.B)
        c.io.rx.poke(true.B)
        c.clock.step(100)
        c.io.rx.poke(false.B) // start bit
        c.clock.step(104)
        c.io.rx.poke(1.B) // 1 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 2 bit 0
        c.clock.step(104)
        c.io.rx.poke(1.B) // 3 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 4 bit 0
        c.clock.step(104)
        c.io.rx.poke(1.B) // 5 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 6 bit 0
        c.clock.step(104)
        c.io.rx.poke(1.B) // 7 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 8 bit 0
        c.clock.step(104)
        c.io.rx.poke(true.B) // stop bit
        c.clock.step(104)
        // c.io.rxdebug.expect(0x55.asUInt)

        // wait here
        c.clock.step(1000)
        c.io.debug.expect(0x01.asUInt)
      }
  }

  it should "recieve 0b01010101, and clear buffer" in {
    test(new Top("./hack/tests/Uart4/vm.hack", "./hack/init.bin", words))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.io.cts.poke(true.B)
        c.io.rx.poke(true.B)
        c.clock.step(100)
        c.io.rx.poke(false.B) // start bit
        c.clock.step(104)
        c.io.rx.poke(1.B) // 1 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 2 bit 0
        c.clock.step(104)
        c.io.rx.poke(1.B) // 3 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 4 bit 0
        c.clock.step(104)
        c.io.rx.poke(1.B) // 5 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 6 bit 0
        c.clock.step(104)
        c.io.rx.poke(1.B) // 7 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 8 bit 0
        c.clock.step(104)
        c.io.rx.poke(true.B) // stop bit
        c.clock.step(104)
        // c.io.rxdebug.expect(0x55.asUInt)

        // wait here
        c.clock.step(1000)
        c.io.debug.expect(0x00.asUInt)
      }
  }

  behavior of "Uart Tx(12 MHz, 115200 bps)"
  it should "send 0b01010101" in {
    test(new Top("./hack/tests/Uart5/vm.hack", "./hack/init.bin", words))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.io.cts.poke(true.B)
        c.clock.step(5000)
        c.io.debug.expect(256.asUInt)
      }
  }

  it should "send 0b01010101, reset" in {
    test(new Top("./hack/tests/Uart5/vm.hack", "./hack/init.bin", words))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.io.cts.poke(true.B)
        c.clock.step(5000)
        c.reset.poke(true.B)
        c.clock.step(50)
        c.reset.poke(false.B)
        c.io.cts.poke(true.B)
        c.clock.step(5000)
        c.io.debug.expect(256.asUInt)
      }
  }

}
