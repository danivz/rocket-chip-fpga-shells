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

class LEDVC709PlacedOverlay(val shell: VC709Shell, name: String, val designInput: LEDDesignInput, val shellInput: LEDShellInput)
  extends LEDXilinxPlacedOverlay(name, designInput, shellInput, boardPin = Some(s"leds_8bits_tri_o_${shellInput.number}"))
class LEDVC709ShellPlacer(val shell: VC709Shell, val shellInput: LEDShellInput)(implicit val valName: ValName)
  extends LEDShellPlacer[VC709Shell] {
  def place(designInput: LEDDesignInput) = new LEDVC709PlacedOverlay(shell, valName.name, designInput, shellInput)
}

class SwitchVC709PlacedOverlay(val shell: VC709Shell, name: String, val designInput: SwitchDesignInput, val shellInput: SwitchShellInput)
  extends SwitchXilinxPlacedOverlay(name, designInput, shellInput, boardPin = Some(s"dip_switches_tri_i_${shellInput.number}"))
class SwitchVC709ShellPlacer(val shell: VC709Shell, val shellInput: SwitchShellInput)(implicit val valName: ValName)
  extends SwitchShellPlacer[VC709Shell] {
  def place(designInput: SwitchDesignInput) = new SwitchVC709PlacedOverlay(shell, valName.name, designInput, shellInput)
}

class ButtonVC709PlacedOverlay(val shell: VC709Shell, name: String, val designInput: ButtonDesignInput, val shellInput: ButtonShellInput)
  extends ButtonXilinxPlacedOverlay(name, designInput, shellInput, boardPin = Some(s"push_buttons_5bits_tri_i_${shellInput.number}"))
class ButtonVC709ShellPlacer(val shell: VC709Shell, val shellInput: ButtonShellInput)(implicit val valName: ValName)
  extends ButtonShellPlacer[VC709Shell] {
  def place(designInput: ButtonDesignInput) = new ButtonVC709PlacedOverlay(shell, valName.name, designInput, shellInput)
}

class PMBusVC709PlacedOverlay(val shell: VC709Shell, name: String, val designInput: PMBusDesignInput, val shellInput: PMBusShellInput)
  extends PMBusXilinxPlacedOverlay(name, designInput, shellInput)
{
  shell { InModuleBody {
    val packagePinsWithPackageIOs = Seq(("AW37", IOPin(io.clk)),  // PMBus clk  AW37
                                        ("AY39", IOPin(io.data))) // PMBus data AY39
                                                                  // Leave PMBus alert unconfigured

    packagePinsWithPackageIOs foreach { case (pin, io) => {
      shell.xdc.addPackagePin(io, pin)
      shell.xdc.addIOStandard(io, "LVCMOS18")
    } }
  } }
}

class PMBusVC709ShellPlacer(val shell: VC709Shell, val shellInput: PMBusShellInput)(implicit val valName: ValName)
  extends PMBusShellPlacer[VC709Shell] {
  def place(designInput: PMBusDesignInput) = new PMBusVC709PlacedOverlay(shell, valName.name, designInput, shellInput)
}

class JTAGDebugBScanVC709PlacedOverlay(val shell: VC709Shell, name: String, val designInput: JTAGDebugBScanDesignInput, val shellInput: JTAGDebugBScanShellInput)
  extends JTAGDebugBScanXilinxPlacedOverlay(name, designInput, shellInput)
class JTAGDebugBScanVC709ShellPlacer(val shell: VC709Shell, val shellInput: JTAGDebugBScanShellInput)(implicit val valName: ValName)
  extends JTAGDebugBScanShellPlacer[VC709Shell] {
  def place(designInput: JTAGDebugBScanDesignInput) = new JTAGDebugBScanVC709PlacedOverlay(shell, valName.name, designInput, shellInput)
}

case object VC7094GDDRSize extends Field[BigInt](0x40000000L * 4) // 4GB
class DDRVC709PlacedOverlay(val shell: VC709Shell, name: String, val designInput: DDRDesignInput, val shellInput: DDRShellInput)
  extends DDRPlacedOverlay[XilinxVC709MIGPads](name, designInput, shellInput)
{
  val size = p(VC7094GDDRSize)

  val migParams = XilinxVC709MIGParams(address = AddressSet.misaligned(di.baseAddress, size))
  val mig = LazyModule(new XilinxVC709MIG(migParams))
  val ioNode = BundleBridgeSource(() => mig.module.io.cloneType)
  val topIONode = shell { ioNode.makeSink() }
  val ddrUI     = shell { ClockSourceNode(freqMHz = 200) }
  val areset    = shell { ClockSinkNode(Seq(ClockSinkParameters())) }
  areset := designInput.wrangler := ddrUI

  def overlayOutput = DDROverlayOutput(ddr = mig.node)
  def ioFactory = new XilinxVC709MIGPads(size)

  InModuleBody { ioNode.bundle <> mig.module.io }

  shell { InModuleBody {
    require (shell.sys_clock.get.isDefined, "Use of DDRVC709PlacedOverlay depends on SysClockVC709PlacedOverlay")
    val (sys, _) = shell.sys_clock.get.get.overlayOutput.node.out(0)
    val (ui, _) = ddrUI.out(0)
    val (ar, _) = areset.in(0)
    val port = topIONode.bundle.port
    io <> port
    ui.clock := port.ui_clk
    ui.reset := !port.mmcm_locked || port.ui_clk_sync_rst
    port.sys_clk_i := sys.clock.asUInt
    port.sys_rst := sys.reset // pllReset
    port.aresetn := !ar.reset
  } }

  shell.sdc.addGroup(clocks = Seq("clk_pll_i"))
}

class DDRVC709ShellPlacer(val shell: VC709Shell, val shellInput: DDRShellInput)(implicit val valName: ValName)
  extends DDRShellPlacer[VC709Shell] {
  def place(designInput: DDRDesignInput) = new DDRVC709PlacedOverlay(shell, valName.name, designInput, shellInput)
}

abstract class VC709Shell()(implicit p: Parameters) extends Series7Shell
{
  // PLL reset causes
  val pllReset = InModuleBody { Wire(Bool()) }

  // Order matters; ddr depends on sys_clock
  val sys_clock = Overlay(ClockInputOverlayKey, new SysClockVC709ShellPlacer(this, ClockInputShellInput()))
  val led       = Seq.tabulate(8)(i => Overlay(LEDOverlayKey, new LEDVC709ShellPlacer(this, LEDShellInput(color = "red", number = i))(valName = ValName(s"led_$i"))))
  val switch    = Seq.tabulate(8)(i => Overlay(SwitchOverlayKey, new SwitchVC709ShellPlacer(this, SwitchShellInput(number = i))(valName = ValName(s"switch_$i"))))
  val button    = Seq.tabulate(5)(i => Overlay(ButtonOverlayKey, new ButtonVC709ShellPlacer(this, ButtonShellInput(number = i))(valName = ValName(s"button_$i"))))
  val ddr       = Overlay(DDROverlayKey, new DDRVC709ShellPlacer(this, DDRShellInput()))
  val pmbus     = Overlay(PMBusOverlayKey, new PMBusVC709ShellPlacer(this, PMBusShellInput(index = 0))(valName = ValName(s"pmbus")))
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

    pllReset :=
      reset_ibuf.io.O || powerOnReset
  }
}

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
