package sifive.fpgashells.ip.microsemi.polarfiretxpll

import chisel3._
import chisel3.experimental.{Analog,attach}
import freechips.rocketchip.util.{ElaborationArtefacts}
import freechips.rocketchip.util.GenericParameterizedBundle
import org.chipsalliance.cde.config._

// Black Box for Microsemi:SgCore:PF_TX_PLL:1.0.109

trait PolarFireTxPLLIOPads extends Bundle {

    val REF_CLK         = Input(Clock())
    val BIT_CLK         = Output(Clock())
    val CLK_125         = Output(Clock())
    val REF_CLK_TO_LANE = Output(Clock())
    val LOCK            = Output(Bool())
    val PLL_LOCK        = Output(Bool())
}

//scalastyle:off
//turn off linter: blackbox name must match verilog module
class PolarFireTxPLL(implicit val p:Parameters) extends BlackBox
{
  override def desiredName = "transmit_pll"

  val io = new PolarFireTxPLLIOPads {
  }

  ElaborationArtefacts.add(
    "Libero.polarfire_tx_pll.libero.tcl",
    """ 
create_design -id Actel:SgCore:PF_TX_PLL:2.0.002 -design_name {transmit_pll} -config_file {} -params {} -inhibit_configurator 0
open_smartdesign -design {transmit_pll}
configure_design -component {transmit_pll} -library {}
configure_vlnv_instance -component {transmit_pll} -library {} -name {transmit_pll_0} -params {"TxPLL_REF:100" "TxPLL_OUT:2500"} -validate_rules 0 
fix_vlnv_instance -component {transmit_pll} -library {} -name {transmit_pll_0} 
open_smartdesign -design {transmit_pll}
configure_design -component {transmit_pll} -library {} 
"""
  )
  
}
//scalastyle:on

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
