package sifive.fpgashells.shell.xilinx

import chisel3._
import chisel3.experimental.IO
import org.chipsalliance.cde.config._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.tilelink._
import freechips.rocketchip.util.SyncResetSynchronizerShiftReg
import sifive.fpgashells.clocks._
import sifive.fpgashells.shell._
import sifive.fpgashells.ip.xilinx._
import sifive.blocks.devices.chiplink._
import sifive.fpgashells.devices.xilinx.xilinxvc709mig._
// import sifive.fpgashells.devices.xilinx.xilinxvc709pciex1._

class SysClockVC709PlacedOverlay(val shell: VC709Shell, name: String, val designInput: ClockInputDesignInput, val shellInput: ClockInputShellInput)
  extends LVDSClockInputXilinxPlacedOverlay(name, designInput, shellInput)
{
  val node = shell { ClockSourceNode(freqMHz = 200, jitterPS = 50)(ValName(name)) }

  shell { InModuleBody {
    shell.xdc.addBoardPin(io.p, "clk_p")
    shell.xdc.addBoardPin(io.n, "clk_n")
  } }
}
class SysClockVC709ShellPlacer(val shell: VC709Shell, val shellInput: ClockInputShellInput)(implicit val valName: ValName)
  extends ClockInputShellPlacer[VC709Shell] {
  def place(designInput: ClockInputDesignInput) = new SysClockVC709PlacedOverlay(shell, valName.name, designInput, shellInput)
}

// class SDIOVC709PlacedOverlay(val shell: VC709Shell, name: String, val designInput: SPIDesignInput, val shellInput: SPIShellInput)
//   extends SDIOXilinxPlacedOverlay(name, designInput, shellInput)
// {
//   shell { InModuleBody {
//     val packagePinsWithPackageIOs = Seq(("AN30", IOPin(io.spi_clk)),
//                                         ("AP30", IOPin(io.spi_cs)),
//                                         ("AR30", IOPin(io.spi_dat(0))),
//                                         ("AU31", IOPin(io.spi_dat(1))),
//                                         ("AV31", IOPin(io.spi_dat(2))),
//                                         ("AT30", IOPin(io.spi_dat(3))))

//     packagePinsWithPackageIOs foreach { case (pin, io) => {
//       shell.xdc.addPackagePin(io, pin)
//       shell.xdc.addIOStandard(io, "LVCMOS18")
//       shell.xdc.addIOB(io)
//     } }
//     packagePinsWithPackageIOs drop 1 foreach { case (pin, io) => {
//       shell.xdc.addPullup(io)
//     } }
//   } }
// }
// class SDIOVC709ShellPlacer(val shell: VC709Shell, val shellInput: SPIShellInput)(implicit val valName: ValName)
//   extends SPIShellPlacer[VC709Shell] {
//   def place(designInput: SPIDesignInput) = new SDIOVC709PlacedOverlay(shell, valName.name, designInput, shellInput)
// }

class UARTVC709PlacedOverlay(val shell: VC709Shell, name: String, val designInput: UARTDesignInput, val shellInput: UARTShellInput)
  extends UARTXilinxPlacedOverlay(name, designInput, shellInput, true)
{
  shell { InModuleBody {
    val packagePinsWithPackageIOs = Seq(("AT32", IOPin(io.ctsn.get)),
                                        ("AR34", IOPin(io.rtsn.get)),
                                        ("AU33", IOPin(io.rxd)),
                                        ("AU36", IOPin(io.txd)))

    packagePinsWithPackageIOs foreach { case (pin, io) => {
      shell.xdc.addPackagePin(io, pin)
      shell.xdc.addIOStandard(io, "LVCMOS18")
      shell.xdc.addIOB(io)
    } }
  } }
}
class UARTVC709ShellPlacer(val shell: VC709Shell, val shellInput: UARTShellInput)(implicit val valName: ValName)
  extends UARTShellPlacer[VC709Shell] {
  def place(designInput: UARTDesignInput) = new UARTVC709PlacedOverlay(shell, valName.name, designInput, shellInput)
}

// class LEDVC709PlacedOverlay(val shell: VC709Shell, name: String, val designInput: LEDDesignInput, val shellInput: LEDShellInput)
//   extends LEDXilinxPlacedOverlay(name, designInput, shellInput, boardPin = Some(s"leds_8bits_tri_o_${shellInput.number}"))
// class LEDVC709ShellPlacer(val shell: VC709Shell, val shellInput: LEDShellInput)(implicit val valName: ValName)
//   extends LEDShellPlacer[VC709Shell] {
//   def place(designInput: LEDDesignInput) = new LEDVC709PlacedOverlay(shell, valName.name, designInput, shellInput)
// }

// class SwitchVC709PlacedOverlay(val shell: VC709Shell, name: String, val designInput: SwitchDesignInput, val shellInput: SwitchShellInput)
//   extends SwitchXilinxPlacedOverlay(name, designInput, shellInput, boardPin = Some(s"dip_switches_tri_i_${shellInput.number}"))
// class SwitchVC709ShellPlacer(val shell: VC709Shell, val shellInput: SwitchShellInput)(implicit val valName: ValName)
//   extends SwitchShellPlacer[VC709Shell] {
//   def place(designInput: SwitchDesignInput) = new SwitchVC709PlacedOverlay(shell, valName.name, designInput, shellInput)
// }

// class ButtonVC709PlacedOverlay(val shell: VC709Shell, name: String, val designInput: ButtonDesignInput, val shellInput: ButtonShellInput)
//   extends ButtonXilinxPlacedOverlay(name, designInput, shellInput, boardPin = Some(s"push_buttons_5bits_tri_i_${shellInput.number}"))
// class ButtonVC709ShellPlacer(val shell: VC709Shell, val shellInput: ButtonShellInput)(implicit val valName: ValName)
//   extends ButtonShellPlacer[VC709Shell] {
//   def place(designInput: ButtonDesignInput) = new ButtonVC709PlacedOverlay(shell, valName.name, designInput, shellInput)
// }

// class ChipLinkVC709PlacedOverlay(val shell: VC709Shell, name: String, val designInput: ChipLinkDesignInput, val shellInput: ChipLinkShellInput)
//   extends ChipLinkXilinxPlacedOverlay(name, designInput, shellInput, rxPhase=280, txPhase=220, rxMargin=0.3, txMargin=0.3)
// {
//   val ereset_n = shell { InModuleBody {
//     val ereset_n = IO(Input(Bool()))
//     ereset_n.suggestName("ereset_n")
//     shell.xdc.addPackagePin(ereset_n, "AF40")
//     shell.xdc.addIOStandard(ereset_n, "LVCMOS18")
//     shell.xdc.addTermination(ereset_n, "NONE")
//     ereset_n
//   } }

//   shell { InModuleBody {
//     val dir1 = Seq("AF39", "AJ41", "AJ40", /* clk, rst, send */
//                    "AD40", "AD41", "AF41", "AG41", "AK39", "AL39", "AJ42", "AK42",
//                    "AL41", "AL42", "AF42", "AG42", "AD38", "AE38", "AC40", "AC41",
//                    "AD42", "AE42", "AJ38", "AK38", "AB41", "AB42", "Y42",  "AA42",
//                    "Y39",  "AA39", "W40",  "Y40",  "AB38", "AB39", "AC38", "AC39")
//     val dir2 = Seq("U39", "R37", "T36", /* clk, rst, send */
//                    "U37", "U38", "U36", "T37", "U32", "U33", "V33", "V34",
//                    "P35", "P36", "W32", "W33", "R38", "R39", "U34", "T35",
//                    "R33", "R34", "N33", "N34", "P32", "P33", "V35", "V36",
//                    "W36", "W37", "T32", "R32", "V39", "V40", "P37", "P38")
//     (IOPin.of(io.b2c) zip dir1) foreach { case (io, pin) => shell.xdc.addPackagePin(io, pin) }
//     (IOPin.of(io.c2b) zip dir2) foreach { case (io, pin) => shell.xdc.addPackagePin(io, pin) }
//   } }
// }
// class ChipLinkVC709ShellPlacer(val shell: VC709Shell, val shellInput: ChipLinkShellInput)(implicit val valName: ValName)
//   extends ChipLinkShellPlacer[VC709Shell] {
//   def place(designInput: ChipLinkDesignInput) = new ChipLinkVC709PlacedOverlay(shell, valName.name, designInput, shellInput)
// }

// // TODO: JTAG is untested
// class JTAGDebugVC709PlacedOverlay(val shell: VC709Shell, name: String, val designInput: JTAGDebugDesignInput, val shellInput: JTAGDebugShellInput)
//   extends JTAGDebugXilinxPlacedOverlay(name, designInput, shellInput)
// {
//   shell { InModuleBody {
//     shell.sdc.addClock("JTCK", IOPin(io.jtag_TCK), 10)
//     shell.sdc.addGroup(clocks = Seq("JTCK"))
//     shell.xdc.clockDedicatedRouteFalse(IOPin(io.jtag_TCK))
// /* if old method
//     val packagePinsWithPackageIOs = Seq(("R32", IOPin(io.jtag_TCK)),
//                                         ("W36", IOPin(io.jtag_TMS)),
//                                         ("W37", IOPin(io.jtag_TDI)),
//                                         ("V40", IOPin(io.jtag_TDO)))
// */
//     /*
//            #Olimex Pin  Olimex Function LCD Pin LCD Function FPGA Pin
//            #1           VREF            14      5V
//            #3           TTRST_N         1       LCD_DB7       AN40
//            #5           TTDI            2       LCD_DB6       AR39
//            #7           TTMS            3       LCD_DB5       AR38
//            #9           TTCK            4       LCD_DB4       AT42
//            #11          TRTCK           NC      NC            NC
//            #13          TTDO            9       LCD_E         AT40
//            #15          TSRST_N         10      LCD_RW        AR42
//            #2           VREF            14      5V
//            #18          GND             13      GND
//      */
//     val packagePinsWithPackageIOs = Seq(("AT42", IOPin(io.jtag_TCK)),
//                                         ("AR38", IOPin(io.jtag_TMS)),
//                                         ("AR39", IOPin(io.jtag_TDI)),
//                                         ("AR42", IOPin(io.srst_n)),
//                                         ("AT40", IOPin(io.jtag_TDO)))
//     packagePinsWithPackageIOs foreach { case (pin, io) => {
//       shell.xdc.addPackagePin(io, pin)
//       shell.xdc.addIOStandard(io, "LVCMOS18")
//       shell.xdc.addPullup(io)
//     } }
//   } }
// }
// class JTAGDebugVC709ShellPlacer(val shell: VC709Shell, val shellInput: JTAGDebugShellInput)(implicit val valName: ValName)
//   extends JTAGDebugShellPlacer[VC709Shell] {
//   def place(designInput: JTAGDebugDesignInput) = new JTAGDebugVC709PlacedOverlay(shell, valName.name, designInput, shellInput)
// }

class PMBusVC709PlacedOverlay(val shell: VC709Shell, name: String, val designInput: I2CDesignInput, val shellInput: I2CShellInput)
  extends I2CXilinxPlacedOverlay(name, designInput, shellInput)
{
  shell { InModuleBody {
    val packagePinsWithPackageIOs = Seq(("AW37", IOPin(io.scl)),  // PMBus clk  AW37
                                        ("AY39", IOPin(io.sda)))  // PMBus data AY39
                                                                  // Leave PMBus alert unconfigured

    packagePinsWithPackageIOs foreach { case (pin, io) => {
      shell.xdc.addPackagePin(io, pin)
      shell.xdc.addIOStandard(io, "LVCMOS18")
    } }
  } }
}

class PMBusVC709ShellPlacer(val shell: VC709Shell, val shellInput: I2CShellInput)(implicit val valName: ValName)
  extends I2CShellPlacer[VC709Shell] {
  def place(designInput: I2CDesignInput) = new PMBusVC709PlacedOverlay(shell, valName.name, designInput, shellInput)
}

class JTAGDebugBScanVC709PlacedOverlay(val shell: VC709Shell, name: String, val designInput: JTAGDebugBScanDesignInput, val shellInput: JTAGDebugBScanShellInput)
  extends JTAGDebugBScanXilinxPlacedOverlay(name, designInput, shellInput)
class JTAGDebugBScanVC709ShellPlacer(val shell: VC709Shell, val shellInput: JTAGDebugBScanShellInput)(implicit val valName: ValName)
  extends JTAGDebugBScanShellPlacer[VC709Shell] {
  def place(designInput: JTAGDebugBScanDesignInput) = new JTAGDebugBScanVC709PlacedOverlay(shell, valName.name, designInput, shellInput)
}

// case object VC7094GDDRSize extends Field[BigInt](0x40000000L * 4) // 4GB
// class DDRVC709PlacedOverlay(val shell: VC709Shell, name: String, val designInput: DDRDesignInput, val shellInput: DDRShellInput)
//   extends DDRPlacedOverlay[XilinxVC709MIGPads](name, designInput, shellInput)
// {
//   val size = if (designInput.vc7094gbdimm) p(VC7094GDDRSize) else p(VC7091GDDRSize)

//   val migParams = XilinxVC709MIGParams(address = AddressSet.misaligned(di.baseAddress, size))
//   val mig = LazyModule(new XilinxVC709MIG(migParams))
//   val ioNode = BundleBridgeSource(() => mig.module.io.cloneType)
//   val topIONode = shell { ioNode.makeSink() }
//   val ddrUI     = shell { ClockSourceNode(freqMHz = 200) }
//   val areset    = shell { ClockSinkNode(Seq(ClockSinkParameters())) }
//   areset := designInput.wrangler := ddrUI

//   def overlayOutput = DDROverlayOutput(ddr = mig.node)
//   def ioFactory = new XilinxVC709MIGPads(size)

//   InModuleBody { ioNode.bundle <> mig.module.io }

//   shell { InModuleBody {
//     require (shell.sys_clock.get.isDefined, "Use of DDRVC709PlacedOverlay depends on SysClockVC709PlacedOverlay")
//     val (sys, _) = shell.sys_clock.get.get.overlayOutput.node.out(0)
//     val (ui, _) = ddrUI.out(0)
//     val (ar, _) = areset.in(0)
//     val port = topIONode.bundle.port
//     io <> port
//     ui.clock := port.ui_clk
//     ui.reset := !port.mmcm_locked || port.ui_clk_sync_rst
//     port.sys_clk_i := sys.clock.asUInt
//     port.sys_rst := sys.reset // pllReset
//     port.aresetn := !ar.reset
//   } }

//   shell.sdc.addGroup(clocks = Seq("clk_pll_i"))
// }
// class DDRVC709ShellPlacer(val shell: VC709Shell, val shellInput: DDRShellInput)(implicit val valName: ValName)
//   extends DDRShellPlacer[VC709Shell] {
//   def place(designInput: DDRDesignInput) = new DDRVC709PlacedOverlay(shell, valName.name, designInput, shellInput)
// }

// class PCIeVC709PlacedOverlay(val shell: VC709Shell, name: String, val designInput: PCIeDesignInput, val shellInput: PCIeShellInput)
//   extends PCIePlacedOverlay[XilinxVC709PCIeX1Pads](name, designInput, shellInput)
// {
//   val pcie = LazyModule(new XilinxVC709PCIeX1)
//   val ioNode = BundleBridgeSource(() => pcie.module.io.cloneType)
//   val topIONode = shell { ioNode.makeSink() }
//   val axiClk    = shell { ClockSourceNode(freqMHz = 125) }
//   val areset    = shell { ClockSinkNode(Seq(ClockSinkParameters())) }
//   areset := designInput.wrangler := axiClk

//   val slaveSide = TLIdentityNode()
//   pcie.crossTLIn(pcie.slave) := slaveSide
//   pcie.crossTLIn(pcie.control) := slaveSide
//   val node = NodeHandle(slaveSide, pcie.crossTLOut(pcie.master))
//   val intnode = pcie.crossIntOut(pcie.intnode)

//   def overlayOutput = PCIeOverlayOutput(node, intnode)
//   def ioFactory = new XilinxVC709PCIeX1Pads

//   InModuleBody { ioNode.bundle <> pcie.module.io }

//   shell { InModuleBody {
//     val (axi, _) = axiClk.out(0)
//     val (ar, _) = areset.in(0)
//     val port = topIONode.bundle.port
//     io <> port
//     axi.clock := port.axi_aclk_out
//     axi.reset := !port.mmcm_lock
//     port.axi_aresetn := !ar.reset
//     port.axi_ctl_aresetn := !ar.reset

//     shell.xdc.addPackagePin(io.REFCLK_rxp, "A10")
//     shell.xdc.addPackagePin(io.REFCLK_rxn, "A9")
//     shell.xdc.addPackagePin(io.pci_exp_txp, "H4")
//     shell.xdc.addPackagePin(io.pci_exp_txn, "H3")
//     shell.xdc.addPackagePin(io.pci_exp_rxp, "G6")
//     shell.xdc.addPackagePin(io.pci_exp_rxn, "G5")

//     shell.sdc.addClock(s"${name}_ref_clk", io.REFCLK_rxp, 100)
//   } }

//   shell.sdc.addGroup(clocks = Seq("txoutclk", "userclk1"))
// }
// class PCIeVC709ShellPlacer(val shell: VC709Shell, val shellInput: PCIeShellInput)(implicit val valName: ValName)
//   extends PCIeShellPlacer[VC709Shell] {
//   def place(designInput: PCIeDesignInput) = new PCIeVC709PlacedOverlay(shell, valName.name, designInput, shellInput)
// }

abstract class VC709Shell()(implicit p: Parameters) extends Series7Shell
{
  // PLL reset causes
  val pllReset = InModuleBody { Wire(Bool()) }

  // Order matters; ddr depends on sys_clock
  val sys_clock = Overlay(ClockInputOverlayKey, new SysClockVC709ShellPlacer(this, ClockInputShellInput()))
  // val led       = Seq.tabulate(8)(i => Overlay(LEDOverlayKey, new LEDVC709ShellPlacer(this, LEDShellInput(color = "red", number = i))(valName = ValName(s"led_$i"))))
  // val switch    = Seq.tabulate(8)(i => Overlay(SwitchOverlayKey, new SwitchVC709ShellPlacer(this, SwitchShellInput(number = i))(valName = ValName(s"switch_$i"))))
  // val button    = Seq.tabulate(5)(i => Overlay(ButtonOverlayKey, new ButtonVC709ShellPlacer(this, ButtonShellInput(number = i))(valName = ValName(s"button_$i"))))
  // val chiplink  = Overlay(ChipLinkOverlayKey, new ChipLinkVC709ShellPlacer(this, ChipLinkShellInput()))
  // val ddr       = Overlay(DDROverlayKey, new DDRVC709ShellPlacer(this, DDRShellInput()))
  // val sdio      = Overlay(SPIOverlayKey, new SDIOVC709ShellPlacer(this, SPIShellInput()))
  val pmbus     = Overlay(I2COverlayKey, new PMBusVC709ShellPlacer(this, I2CShellInput(index = 0))(valName = ValName(s"pmbus")))
  val jtagBScan = Overlay(JTAGDebugBScanOverlayKey, new JTAGDebugBScanVC709ShellPlacer(this, JTAGDebugBScanShellInput()))
}

class VC709BaseShell()(implicit p: Parameters) extends VC709Shell
{
  val uart      = Seq.tabulate(1)(i => Overlay(UARTOverlayKey, new UARTVC709ShellPlacer(this, UARTShellInput(index = 0))))
  val topDesign = LazyModule(p(DesignKey)(designParameters))

  // Place the sys_clock at the Shell if the user didn't ask for it
  p(ClockInputOverlayKey).foreach(_.place(ClockInputDesignInput()))

  override lazy val module = new LazyRawModuleImp(this) {
    val reset = IO(Input(Bool()))
    xdc.addBoardPin(reset, "reset")

    val reset_ibuf = Module(new IBUF)
    reset_ibuf.io.I := reset

    val sysclk: Clock = sys_clock.get() match {
      case Some(x: SysClockVC709PlacedOverlay) => x.clock
    }
    val powerOnReset = PowerOnResetFPGAOnly(sysclk)
    sdc.addAsyncPath(Seq(powerOnReset))

    // val ereset: Bool = chiplink.get() match {
    //   case Some(x: ChipLinkVCU118PlacedOverlay) => !x.ereset_n
    //   case _ => false.B
    // }
    pllReset :=
      reset_ibuf.io.O || powerOnReset // || ereset
  }
}

// class VC709PCIeShell()(implicit p: Parameters) extends VC709Shell
// {
//   val uart      = Seq.tabulate(1)(i => Overlay(UARTOverlayKey, new UARTVC709ShellPlacer(this, UARTShellInput(index = 0))))
//   // val pcie      = Overlay(PCIeOverlayKey, new PCIeVC709ShellPlacer(this, PCIeShellInput()))
//   val topDesign = LazyModule(p(DesignKey)(designParameters))

//   // Place the sys_clock at the Shell if the user didn't ask for it
//   p(ClockInputOverlayKey).foreach(_.place(ClockInputDesignInput()))

//   override lazy val module = new LazyRawModuleImp(this) {
//     val reset = IO(Input(Bool()))
//     xdc.addBoardPin(reset, "reset")

//     val reset_ibuf = Module(new IBUF)
//     reset_ibuf.io.I := reset
//     val sysclk: Clock = sys_clock.get() match {
//       case Some(x: SysClockVC709PlacedOverlay) => x.clock
//     }
//     val powerOnReset = PowerOnResetFPGAOnly(sysclk)
//     sdc.addAsyncPath(Seq(powerOnReset))
//     // val ereset: Bool = chiplink.get() match {
//     //   case Some(x: ChipLinkVCU118PlacedOverlay) => !x.ereset_n
//     //   case _ => false.B
//     // }
//     pllReset :=
//       reset_ibuf.io.O || powerOnReset // || ereset
//   }
// }

/*
   Copyright 2016 SiFive, Inc.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
