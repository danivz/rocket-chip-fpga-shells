package sifive.fpgashells.devices.xilinx.xilinxvc709mig

import Chisel._
import org.chipsalliance.cde.config._
import freechips.rocketchip.subsystem.BaseSubsystem
import freechips.rocketchip.diplomacy.{LazyModule, LazyModuleImp, AddressRange}
import freechips.rocketchip.tilelink.{TLWidthWidget}

case object MemoryXilinxDDRKey extends Field[XilinxVC709MIGParams]

trait HasMemoryXilinxVC709MIG { this: BaseSubsystem =>
  val module: HasMemoryXilinxVC709MIGModuleImp

  val xilinxvc709mig = LazyModule(new XilinxVC709MIG(p(MemoryXilinxDDRKey)))

  mbus.coupleTo("xilinxvc709mig") { xilinxvc709mig.node := TLWidthWidget(mbus.beatBytes) := _ }
}

trait HasMemoryXilinxVC709MIGBundle {
  val xilinxvc709mig: XilinxVC709MIGIO
  def connectXilinxVC709MIGToPads(pads: XilinxVC709MIGPads) {
    pads <> xilinxvc709mig
  }
}

trait HasMemoryXilinxVC709MIGModuleImp extends LazyModuleImp
    with HasMemoryXilinxVC709MIGBundle {
  val outer: HasMemoryXilinxVC709MIG
  val ranges = AddressRange.fromSets(p(MemoryXilinxDDRKey).address)
  require (ranges.size == 1, "DDR range must be contiguous")
  val depth = ranges.head.size
  val xilinxvc709mig = IO(new XilinxVC709MIGIO(depth))

  xilinxvc709mig <> outer.xilinxvc709mig.module.io.port
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
