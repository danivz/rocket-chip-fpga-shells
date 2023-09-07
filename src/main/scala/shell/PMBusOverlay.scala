package sifive.fpgashells.shell

import chisel3._
import chisel3.experimental.Analog

import org.chipsalliance.cde.config._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.subsystem.{BaseSubsystem, PeripheryBus, PeripheryBusKey}
import freechips.rocketchip.tilelink.TLBusWrapper
import freechips.rocketchip.interrupts.IntInwardNode


import powermonitor._

case class PMBusShellInput(index: Int = 0)
case class PMBusDesignInput(node: BundleBridgeSource[PMBusPort])(implicit val p: Parameters)
case class PMBusOverlayOutput()
trait PMBusShellPlacer[Shell] extends ShellPlacer[PMBusDesignInput, PMBusShellInput, PMBusOverlayOutput]

case object PMBusOverlayKey extends Field[Seq[DesignPlacer[PMBusDesignInput, PMBusShellInput, PMBusOverlayOutput]]](Nil)

class ShellPMBusPortIO extends Bundle {
  val clk = Analog(1.W)
  val data = Analog(1.W)
}

abstract class PMBusPlacedOverlay(
  val name: String, val di: PMBusDesignInput, val si: PMBusShellInput)
    extends IOPlacedOverlay[ShellPMBusPortIO, PMBusDesignInput, PMBusShellInput, PMBusOverlayOutput]
{
  implicit val p = di.p

  def ioFactory = new ShellPMBusPortIO

  val tlpmbusSink = sinkScope { di.node.makeSink }

  def overlayOutput = PMBusOverlayOutput()
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
